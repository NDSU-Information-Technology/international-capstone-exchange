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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.access.DataContext;

import edu.ndsu.eci.international_capstone_exchange.persist.CapstoneDomainMap;
import edu.ndsu.eci.international_capstone_exchange.persist.Proposal;
import edu.ndsu.eci.international_capstone_exchange.util.CORSResponseFilter;
import edu.ndsu.eci.international_capstone_exchange.util.ProposalStatus;

/**
 * Get list of institutions in countries.
 * 
 * /rest/institutions
 *
 */
@Path("/institutions")
public class InstList {

  /** response context */
  @Context
  private HttpServletResponse response;
  
  /** request context */
  @Context
  private HttpServletRequest request;
  
  /** cors filter for ndsu.edu and ndsu.nodak.edu */
  private CORSResponseFilter filter = new CORSResponseFilter();
  
  @GET
  @Produces("application/json")
  public Object getAllInstitutionsResource() {
    ObjectContext context = DataContext.createDataContext();
    CapstoneDomainMap map = CapstoneDomainMap.getInstance();
    
    filter.doFilter(request, response);
    
    List<Proposal> proposals =  map.performProposalsByStatus(context, ProposalStatus.PAIRED);
    
    Map<String, Set<String>> countries = new HashMap<>();
    
    for (Proposal prop : proposals) {
      String country = prop.getInstitution().getCountry().getName();
      if (!countries.containsKey(country)) {
        countries.put(country, new HashSet<>());
      }
      countries.get(country).add(prop.getInstitution().getName());
    }
    
    return countries;
  }
}
