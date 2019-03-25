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
package edu.ndsu.eci.international_capstone_exchange.util;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

public class CORSResponseFilter {
  /** set of allowed domains */
  private Set<String> allowedDomains = new HashSet<>();
  
  /**
   * Constructor with default list of allowed domains.
   */
  public CORSResponseFilter() {
   allowedDomains.add("ndsu.edu");
   allowedDomains.add("ndsu.nodak.edu");
  }
  
  /**
   * Constructor that uses the specified list of domains
   * @param allowedDomains provided list of allowed domains
   */
  public CORSResponseFilter(Set<String> allowedDomains) {
    this.allowedDomains = new HashSet<>(allowedDomains);
  }
  
  /**
   * Setting the origin response headers given the allowed origin domains
   * @param request request object
   * @param response response object
   */   
  public void doFilter(HttpServletRequest request, HttpServletResponse response) {
    String requestedDomain = request.getHeader("Origin");

    if (StringUtils.isBlank(requestedDomain)) {
      return;
    }

    for (String domain : allowedDomains) {
      if (requestedDomain.contains(domain)) {
        response.addHeader("Access-Control-Allow-Origin", requestedDomain);
        response.addHeader("Access-Control-Allow-Credentials", "true");
        response.addHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, OPTIONS, HEAD");
        response.addHeader("Access-Control-Allow-Headers", "Origin, Content-Type, Accept, X-Requested-With");
        return;
      }
    }   
  }
}
