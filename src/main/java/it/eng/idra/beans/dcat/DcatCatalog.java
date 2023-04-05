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

import java.util.List;

import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.DCAT;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// TODO: Auto-generated Javadoc
/**
 * The Class DcatCatalog.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DcatCatalog {

  /** The Constant RDFClass. */
  private static final transient Resource RDFClass = DCAT.Catalog;

  /** The datasets. */
  private List<DcatDataset> datasets;

  /** The titles. */
  private List<String> titles;

  /** The descriptions. */
  private List<String> descriptions;

  /** The publisher. */
  private FoafAgent publisher;

  /** The release date. */
  private String releaseDate;

  /** The update date. */
  private String updateDate;

  /** The theme taxonomy. */
  private List<String> themeTaxonomy;

  /** The languages. */
  private List<String> languages;

  /** The homepage. */
  private String homepage;

  /** The license. */
  private String license;

  /** The rigths. */
  private String rigths;

  /** The spatials. */
  private List<String> spatials;

}
