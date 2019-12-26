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
package edu.ndsu.eci.international_capstone_exchange.pages.admin;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.Persistent;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.googlecode.tapestry5cayenne.PersistentEntitySelectModel;
import com.googlecode.tapestry5cayenne.annotations.Cayenne;

import edu.ndsu.eci.international_capstone_exchange.persist.CapstoneDomainMap;
import edu.ndsu.eci.international_capstone_exchange.persist.Proposal;
import edu.ndsu.eci.international_capstone_exchange.persist.ProposalType;
import edu.ndsu.eci.international_capstone_exchange.persist.Subject;
import edu.ndsu.eci.international_capstone_exchange.services.HtmlCleaner;
import edu.ndsu.eci.international_capstone_exchange.util.Status;

public class EditProposal {

  @Property
  private Proposal proposal;
  
  @InjectPage
  private Requests requestsPage;
  
  /** alert service, which appears to not be included in the template */
  @Inject
  private AlertManager alerts;
  
  /** cayenne context */
  @Inject
  private ObjectContext context;
  
  /** form */
  @Component
  private BeanEditForm form;
  
  /** selected subjects from palette */
  @Property
  List<Subject> selectedSubjects;

  /** selected proposal types */
  @Property
  List<ProposalType> selectedPropTypes;
  
  /** html cleaner */
  @Inject
  private HtmlCleaner cleaner;
  
  /** encoder for palette */
  @Inject
  @Cayenne
  @Property
  private ValueEncoder<Persistent> encoder;
  
  public Object onActivate(Proposal proposal) {
    if (!proposal.isEditable()) {
      return requestsPage;
    }
    this.proposal = proposal;
    if (selectedSubjects == null && proposal.getSubjects() != null) {
      selectedSubjects = new ArrayList<>(proposal.getSubjects());
    }
    if (selectedPropTypes == null && proposal.getTypes() != null) {
      selectedPropTypes = new ArrayList<>(proposal.getTypes());
    }
    return null;
  }
  
  public Proposal onPassivate() {
    return proposal;
  }
  
  /**
   * On success persist
   * @return go back to users
   */
  public Object onSuccessFromForm() {
    proposal.setLastModified(new Date());
    proposal.setDescription(cleaner.cleanCapstone(proposal.getDescription()));
    fixupSubjects();
    fixupPropTypes();

    context.commitChanges();
    alerts.success("Admin updated proposal");
    return requestsPage;
  }
  
  private void fixupSubjects() {
    Set<Subject> existing = new HashSet<>(proposal.getSubjects());
    Set<Subject> newSubjects = new HashSet<>(selectedSubjects);

    SetView<Subject> newView = Sets.difference(newSubjects, existing);

    for (Subject subject : newView) {
      proposal.addToSubjects(subject);
    }

    SetView<Subject> oldView = Sets.difference(existing, newSubjects);

    for (Subject subject : oldView) {
      proposal.removeFromSubjects(subject);
    }
  }

  private void fixupPropTypes() {
    Set<ProposalType> existing = new HashSet<>(proposal.getTypes());
    Set<ProposalType> newPropTypes = new HashSet<>(selectedPropTypes);

    SetView<ProposalType> newView = Sets.difference(newPropTypes, existing);

    for (ProposalType propType : newView) {
      proposal.addToTypes(propType);
    }

    SetView<ProposalType> oldView = Sets.difference(existing, newPropTypes);

    for (ProposalType propType : oldView) {
      proposal.removeFromTypes(propType);
    }
  }

  /**
   * Subject checklist model
   * @return checklist model
   */
  public SelectModel getSubjectsModel() {
    return new PersistentEntitySelectModel<>(Subject.class, getSubjects());
  }

  /**
   * Subject checklist model
   * @return checklist model
   */
  public SelectModel getPropTypesModel() {
    return new PersistentEntitySelectModel<>(ProposalType.class, getProposalTypes());
  }

  /**
   * Possible subjects
   * @return sort approved subjects
   */
  private List<Subject> getSubjects() {
    return CapstoneDomainMap.getInstance().performSubjectsByStatus(context, Status.APPROVED);
  } 


  /**
   * Possible proposal types
   * @return sort approved proposal types
   */
  private List<ProposalType> getProposalTypes() {
    return CapstoneDomainMap.getInstance().performPropTypesByStatus(context, Status.APPROVED);
  } 
}
