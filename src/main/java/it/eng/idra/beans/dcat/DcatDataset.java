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

import java.io.Serializable;
import java.util.List;





import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * Represents a DCAT Dataset.
 *
 * @author
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DcatDataset implements Serializable {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The id. */
  // Custom fields
  //@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "type")
  private String id;

  /** The title. */
  // Mandatory

  /** The description. */
  private String description;

  /** The distributions. */
  // Recommended
  private List<DcatDistribution> distributions;
  
 

//  /** The theme. */
//  private List<SkosConcept> theme;
//
//  /** The publisher. */
//  private FoafAgent publisher;
//
//  /** The contact point. */
//  private List<VcardOrganization> contactPoint;
//
//  /** The keywords. */
//  private List<String> keywords;
//
//  /** The access rights. */
//  // Optional
//  private String accessRights;
//
//  /** The conforms to. */
//  @JsonIgnore
//  private List<DctStandard> conformsTo;
//
//  /** The documentation. */
//  private List<String> documentation;
//
//  /** The frequency. */
//  private String frequency;
//
//  /** The has version. */
//  private List<String> hasVersion;
//
//  /** The is version of. */
//  private List<String> isVersionOf;
//
//  /** The landing page. */
//  private String landingPage;
//
//  /** The language. */
//  private List<String> language;
//
//  /** The provenance. */
//  private List<String> provenance;
//
//  /** The related resource. */
//  private List<String> relatedResource;
//
//  /** The release date. */
//  private String releaseDate;
//
//  /** The update date. */
//  private String updateDate;
//
//  /** The identifier. */
//  private String identifier;
//
//  /** The other identifier. */
//  private List<String> otherIdentifier;
//
//  /** The sample. */
//  private List<String> sample;
//
//  /** The source. */
//  private List<String> source;
//
//  /** The spatial coverage. */
//  private DctLocation spatialCoverage;
//
//  /** The temporal coverage. */
//  private DctPeriodOfTime temporalCoverage;
//
//  /** The type. */
//  private String type;
//
//  /** The version. */
//  private String version;
//
//  /** The version notes. */
//  private List<String> versionNotes;
//
//  /** The rights holder. */
//  private FoafAgent rightsHolder;
//
//  /** The creator. */
//  private FoafAgent creator;
//
//  /** The subject. */
//  private List<SkosConcept> subject;

}
