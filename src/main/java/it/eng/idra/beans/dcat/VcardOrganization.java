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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;
import org.apache.jena.vocabulary.VCARD4;
//import org.apache.solr.common.SolrDocument;
//import org.apache.solr.common.SolrInputDocument;

// TODO: Auto-generated Javadoc
/**
 * Represents a DCAT VCard, the dataset contacts information.
 *
 * @author
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VcardOrganization {

  /** The id. */
  private String id;

  /** The resource uri. */
  private String resourceUri;

  /** The property uri. */
  private String propertyUri;

  /** The fn. */
  private String fn;

  /** The has email. */
  private String hasEmail;

  /** The has url. */
  private String hasUrl;

  /** The has telephone value. */
  private String hasTelephoneValue;

  /** The has telephone type. */
  private String hasTelephoneType;

}
