$ErrorActionPreference = 'Stop'
$taskEntities = Get-Content -Raw (Join-Path $PSScriptRoot '..\..\..\target\dcat3-entities.json') | ConvertFrom-Json -Depth 100 -AsHashtable
$taskIds = @($taskEntities | ForEach-Object { $_.id })
$taskBase = 'http://localhost:1026/ngsi-ld/v1/'
$taskHeaders = @{Link='<http://host.docker.internal:18082/dcat3-context.jsonld>; rel="http://www.w3.org/ns/json-ld#context"; type="application/ld+json"'}
# Never overwrite an existing fixture identity.
foreach ($taskId in $taskIds) {
  try {
    Invoke-WebRequest -Uri ($taskBase+'entities/'+[Uri]::EscapeDataString($taskId)) -Headers $taskHeaders | Out-Null
    throw "Fixture identity already exists: $taskId"
  } catch {
    if ($_.Exception.Response.StatusCode -ne 404) { throw }
  }
}
try {
  $taskResponse = Invoke-WebRequest -Method Post -Uri ($taskBase+'entityOperations/upsert?options=replace') -ContentType 'application/ld+json' -Body (ConvertTo-Json -InputObject $taskEntities -Depth 100 -Compress)
  if ($taskResponse.StatusCode -notin 200,201,204) { throw "Unexpected create status: $($taskResponse.StatusCode)" }
  $taskDataset = Invoke-RestMethod -Uri ($taskBase+'entities/'+[Uri]::EscapeDataString('urn:ngsi-ld:Dataset:id:fixture-dataset')) -Headers $taskHeaders
  if ($taskDataset.temporalResolution.value -ne 'P1Y') { throw 'Missing temporal resolution' }
  if (@($taskDataset.previousVersion.object)[0] -ne 'https://example.org/v1') { throw 'Missing previous version' }
  $taskSeries = Invoke-RestMethod -Uri ($taskBase+'entities/'+[Uri]::EscapeDataString(@($taskDataset.inSeries.object)[0])) -Headers $taskHeaders
  if ($taskSeries.resourceUri.value -ne 'https://example.org/series') { throw 'Missing series source IRI' }
  $taskQuery = [Uri]::EscapeDataString('idraCatalogueId=="987654"')
  $taskOwned = (Invoke-WebRequest -Uri ($taskBase+'entities?q='+$taskQuery+'&limit=1000') -Headers $taskHeaders).Content | ConvertFrom-Json -AsHashtable -NoEnumerate
  if ($taskOwned.Count -ne $taskIds.Count) { throw "Ownership query returned $($taskOwned.Count), expected $($taskIds.Count)" }
  [pscustomobject]@{created=$taskIds.Count;ownership=$taskOwned.Count;roundTrip='pass';context='pass'} | ConvertTo-Json
} finally {
  if ($taskIds.Count -gt 0) { Invoke-WebRequest -Method Post -Uri ($taskBase+'entityOperations/delete') -ContentType 'application/json' -Body (ConvertTo-Json -InputObject $taskIds -Compress) | Select-Object StatusCode }
}
