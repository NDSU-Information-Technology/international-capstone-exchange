// Copyright 2018 North Dakota State University
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package edu.ndsu.eci.international_capstone_exchange.persist;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.googlecode.tapestry5cayenne.annotations.Label;

import edu.ndsu.eci.international_capstone_exchange.persist.auto._Country;

@JsonIgnoreProperties({"objectContext", "persistenceState", "dataContext", "objEntity", "snapshotVersion", "objectId", "institutions"})
public class Country extends _Country {

  @Label
  @Override
  public String getName() {
    return super.getName();
  }
  
  @Override
  public String toString() {
    return getName();
  }
  
  /**
   * Country ID getter
   * Used to get the country Id value 
   * @return country id
   */
  public int getCountryId() {
    return (Integer) super.getObjectId().getIdSnapshot().get("pk");
  }
}
