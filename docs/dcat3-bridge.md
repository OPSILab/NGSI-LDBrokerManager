# Bridge NGSI-LD per DCAT-AP 3.0.1

Il bridge copia nel broker NGSI-LD (Orion-LD) i cataloghi federati da Idra: catalogo, dataset,
distribuzioni, servizi dati, serie, record di catalogo e agenti. Condivide con Idra i bean DCAT
(`it.eng.idra.beans.dcat`): **Idra e bridge vanno installati insieme**.

## Mapping

- `NgsiDcatMapper` converte ogni bean in un'entità NGSI-LD. Gli ID Dataset/Distribution di Idra sono
  mantenuti; i nodi collegati hanno ID deterministici per catalogo e l'IRI originale è in
  `resourceUri`. Ogni entità ha `idraCatalogueId` (ownership).
- Il context `/dcat3-context.jsonld` associa i termini a DCAT, DCT, PROV, ADMS, ODRL e DCAT-AP; gli
  attributi tecnici sono nel namespace del progetto. `DCAT_CONTEXT_URL` deve essere raggiungibile dal
  broker.
- `metadataEnvelope` (le triple originali della fonte, versionate) viaggia come Property senza essere
  appiattito; il lettore NGSI di Idra lo ricostruisce.
- Metadati del catalogo sorgente, `dct:source` del record, nomi degli agenti con lingua e nomi
  multilingua dei servizi sono trasportati come in Idra.
- Valori che Idra non inventa più (date 1970, IRI segnaposto della licenza) sono ripuliti anche
  quando arrivano da dati salvati con versioni precedenti.

## Ciclo di vita

La sincronizzazione usa upsert con sostituzione, batch di 200 entità e inventario paginato delle
entità del catalogo. Le entità obsolete sono eliminate solo dopo il successo di tutti gli upsert. Una
risposta HTTP 207 è un successo solo se non contiene errori e conferma tutti gli ID. La cancellazione
di un catalogo (`CatalogueService.pruneOwned`) inventaria le entità per `idraCatalogueId` fino a
pagina vuota, verifica e ripete fino a 3 giri; se restano entità risponde 502. Il client HTTP
condiviso non viene chiuso dalle singole richieste.

## Test

```powershell
mvn -f NGSI-LDBrokerManager/pom.xml package
```

`NgsiDcatMapperTest`, `CatalogueLifecycleTest` (1.001 dataset, cambio ID dei figli, errori parziali,
isolamento fra cataloghi), `CatalogueBatchTest` e `DcatMetadataEnvelopeBridgeTest`. Il mapper produce
`target/dcat3-entities.json`, usato dai test Idra.

Prova manuale su un broker locale: in `src/test/integration` ci sono un'API Idra sintetica
(`dcat3-fake-idra.py`, porta 18080) e gli script PowerShell 7 `dcat3-live.ps1` e
`dcat3-sync-live.ps1` (broker su 1026, catalogo sintetico 987654). Gli script verificano che le
identità di prova siano libere e rimuovono le proprie entità alla fine.

## Limiti

- Le API `/api/...` usate da Idra non sono autenticate: il bridge va esposto solo sulla rete interna
  dei container.
- Orion-LD ha mostrato un crash (`free(): invalid pointer`) con due cancellazioni concorrenti di
  cataloghi grandi: eseguire le rimozioni una alla volta.
