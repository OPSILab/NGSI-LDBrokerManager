/*******************************************************************************
 * Idra - Open Data Federation Platform
 * Copyright (C) 2021 Engineering Ingegneria Informatica S.p.A.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 ******************************************************************************/

package it.eng.idra.beans.dcat;
import com.google.gson.annotations.SerializedName;
import it.eng.idra.beans.DistributionAdditionalConfiguration;
//import it.eng.idra.cache.CacheContentType;
import it.eng.idra.utils.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.sparql.vocabulary.FOAF;
import org.apache.jena.vocabulary.DCAT;
import org.apache.jena.vocabulary.DCTerms;
import org.apache.jena.vocabulary.RDFS;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonDeserialize;
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrInputDocument;
import org.json.JSONArray;
import org.json.JSONObject;

// TODO: Auto-generated Javadoc
/**
 * Represents a DCAT Distribution.
 *
 * @author
 */

// @Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcatDistribution implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant RDFClass. */
  private static final Resource RDFClass = DCAT.Distribution;

  /** The id. */
  private String id;

  private String accessURL;

  /** The description. */
  // Recommended
  private String description;

  /** The format. */
  @JsonIgnore
  private String format;

  /** The license. */
  private DctLicenseDocument license;

  /** The byte size. */
  // Optional
  private String byteSize;

  /** The checksum. */
  private SpdxChecksum checksum;

  /** The documentation. */
  private List<String> documentation;

  /** The download url. */
  @SerializedName(value = "downloadURL")
  @JsonDeserialize()
  private String downloadURL;

  /** The language. */
  private List<String> language;

  /** The linked schemas. */
  @JsonIgnore
  private List<DctStandard> linkedSchemas;

  /** The media type. */
  private String mediaType;

  /** The release date. */
  private String releaseDate;

  /** The update date. */
  private String updateDate;

  /** The rights. */
  private String rights;

  /** The status. */
  private SkosConcept status;

  /** The title. */
  private String title;

}
