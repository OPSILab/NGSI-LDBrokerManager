package it.eng.idra.management;

import it.eng.idra.beans.odms.OdmsCatalogue;
import it.eng.idra.beans.odms.OdmsCatalogueNotFoundException;
import org.apache.commons.lang3.StringUtils;

public final class FederationCore {

  private FederationCore() {
  }

  public static OdmsCatalogue getOdmsCatalogue(int id) throws OdmsCatalogueNotFoundException {
    throw new OdmsCatalogueNotFoundException(
        "ODMS catalogue lookup is not available in NGSI-LDBrokerManager runtime");
  }

  public static boolean isDcatTheme(String value) {
    return StringUtils.isNotBlank(value);
  }
}
