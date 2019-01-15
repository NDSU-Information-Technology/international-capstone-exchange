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

import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.eci.international_capstone_exchange.persist.CapstoneDomainMap;
import edu.ndsu.eci.international_capstone_exchange.persist.Proposal;
import edu.ndsu.eci.international_capstone_exchange.persist.ProposalType;
import edu.ndsu.eci.international_capstone_exchange.util.ProposalStatus;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import edu.ndsu.eci.international_capstone_exchange.persist.Subject;

/**
 * Admin display of Proposals.
 *
 */
public class Requests {

  /** Cayenne database reference */
  @Inject
  private ObjectContext context;
  
  /** Grid row */
  @Property
  private Proposal row;

  /** Subject of a proposal */
  @Property
  private Subject subject;
  
  /** Type of a proposal */
  @Property
  private ProposalType type;

  /** JavaScript Support */
  @Inject
  private JavaScriptSupport javaScriptSupport;
  
  /**
   * Getter for Pending Proposals.
   * @return List of Pending Proposals.
   */
  public List<Proposal> getPendingProposals() {
    return CapstoneDomainMap.getInstance().performProposalsByStatus(context, ProposalStatus.PENDING);
  }

  /**
   * Getter for Paired Proposals.
   * @return List of Paired Proposals.
   */
  public List<Proposal> getPairedProposals() {
    return CapstoneDomainMap.getInstance().performProposalsByStatus(context, ProposalStatus.PAIRED);
  }

  /**
   * Getter for Renewal Proposals.
   * @return List of Renewal Proposals.
   */
  public List<Proposal> getRenewalProposals() {
    return CapstoneDomainMap.getInstance().performProposalsByStatus(context, ProposalStatus.PendingRenewal);
  }
  
  /**
   * Getter for All Proposals.
   * @return List of All Proposals.
   */
  public List<Proposal> getAllProposals() {
    return context.performQuery(new SelectQuery(Proposal.class));
  }

  /**
   * After Render call to enable JavaScript.
   */
  void afterRender() {
    javaScriptSupport.require("bootstrap/tab");
  }
}
