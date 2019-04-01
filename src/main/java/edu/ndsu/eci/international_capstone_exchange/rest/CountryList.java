// Copyright 2019 North Dakota State University
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
package edu.ndsu.eci.international_capstone_exchange.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;

import edu.ndsu.eci.international_capstone_exchange.persist.CapstoneDomainMap;
import edu.ndsu.eci.international_capstone_exchange.util.Status;

@Path("/countries")
public class CountryList {
  
  @GET
  @Produces("application/json")
  public Object getAllCountriesResource() {
    ObjectContext context = DataContext.createDataContext();
    CapstoneDomainMap map = CapstoneDomainMap.getInstance();
    
    return map.performCountries(context, Status.APPROVED);
  }
}
