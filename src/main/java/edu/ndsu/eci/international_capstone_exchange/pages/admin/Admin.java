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
package edu.ndsu.eci.international_capstone_exchange.pages.admin;

import org.apache.cayenne.ObjectContext;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import edu.ndsu.eci.international_capstone_exchange.persist.CapstoneDomainMap;
import edu.ndsu.eci.international_capstone_exchange.util.ProposalStatus;
import edu.ndsu.eci.international_capstone_exchange.util.Status;

@Import(library = { "../js/adminPage.js" })
public class Admin {
 
  /** Cayenne database reference */
  @Inject
  private ObjectContext context;
  
  /** JavaScript Support */
  @Inject
  private JavaScriptSupport javaScriptSupport;
  
  /** Holds count of pending proposals. */
  @Property
  private int proposalCount;
  
  /** Holds count of users needing approval. */
  @Property
  private int userCount;
  
  /** Database map reference */
  private CapstoneDomainMap map = CapstoneDomainMap.getInstance();
  
  /**
   * Setup Render used to get pending task information.
   */
  public void setupRender() {
    proposalCount = map.performProposalsByStatus(context, ProposalStatus.PENDING).size();
    userCount = map.performUsersByStatus(context, Status.PENDING).size();
  }
  
}
