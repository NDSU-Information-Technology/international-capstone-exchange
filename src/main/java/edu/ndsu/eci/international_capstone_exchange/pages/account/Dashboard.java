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
package edu.ndsu.eci.international_capstone_exchange.pages.account;

import edu.ndsu.eci.international_capstone_exchange.pages.Privacy;
import edu.ndsu.eci.international_capstone_exchange.services.VelocityEmailService;
import org.apache.cayenne.ObjectContext;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

import edu.ndsu.eci.international_capstone_exchange.auth.ILACRealm;
import edu.ndsu.eci.international_capstone_exchange.persist.CapstoneDomainMap;
import edu.ndsu.eci.international_capstone_exchange.persist.Pairing;
import edu.ndsu.eci.international_capstone_exchange.persist.Proposal;
import edu.ndsu.eci.international_capstone_exchange.persist.ProposalType;
import edu.ndsu.eci.international_capstone_exchange.persist.Subject;
import edu.ndsu.eci.international_capstone_exchange.persist.User;
import edu.ndsu.eci.international_capstone_exchange.services.UserInfo;
import edu.ndsu.eci.international_capstone_exchange.util.ProposalStatus;
import edu.ndsu.eci.international_capstone_exchange.util.Status;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * User's dashboard to direct them to after login.
 *
 */
public class Dashboard {

  /** user info service */
  @Inject
  private UserInfo userInfo;

  /** logged in user */
  @Property
  private User user;
  
  /** cayenne context */
  @Inject
  private ObjectContext context;

  @Inject
  private AlertManager alerts;
  
  /** tml row for subjects */
  @Property
  private Subject subjectRow;
  
  /** tml row for proposals */
  @Property
  private Proposal proposalRow;
  
  @Property
  private ProposalType propTypeRow;
  
  @Property
  private List<Proposal> proposals;
  
  @Property
  private List<Pairing> pairings;
  
  @Property
  private Pairing pairRow;

  @Inject
  private JavaScriptSupport javaScriptSupport;

  @InjectPage
  private Dashboard dashboard;

  @InjectPage
  private Privacy privacy;

  @Inject
  private VelocityEmailService emailService;

  /** form object */
//  @Property
//  private Proposal renewProposal;


  
  /**
   * Setup render, get logged in user
   */
  public void setupRender() {
    //user = userInfo.getUser();
    user = (User) context.localObject(userInfo.getUser().getObjectId(), null);
    proposals = user.getProposals();
    pairings = new ArrayList<>();
    for (Proposal proposal : proposals) {
      if (proposal.getProposalStatus() == ProposalStatus.PAIRED) {
        pairings.add(proposal.getPairing());
      }
    }
  }
  
  void afterRender() {
    javaScriptSupport.require("bootstrap/tab");
  }
  
  /**
   * List of approved subjects to display their meaning
   * @return list of subjects
   */
  public List<Subject> getSubjects() {
    return CapstoneDomainMap.getInstance().performSubjectsByStatus(context, Status.APPROVED);
  }
  
  /**
   * List of approved proposal types to display their meaning
   * @return list of proposal types
   */
  public List<ProposalType> getProposalTypes() {
    return CapstoneDomainMap.getInstance().performPropTypesByStatus(context, Status.APPROVED);
  }
  
  @RequiresPermissions(ILACRealm.PROPOSAL_EDIT_INSTANCE)
  public void onDelete(Proposal proposal) {
    if (!proposal.isDeletable()) {
      return;
    }
    context.deleteObject(proposal);
    context.commitChanges();
  }

  // FIXME use an actual CSV library and don't write to local system
//  String fileName = System.getProperty("user.home")+"/Desktop/ALL CSV.csv";
//  private static final String NEW_LINE_SEPARATOR = "\n";
//
//  public void onCsvdownload() {
//
//    FileWriter fileWriter = null;
//    setupRender();
//
//    try {
//      fileWriter = new FileWriter(fileName);
//
//      //Write the CSV file header
//      fileWriter.append("PROJECT_NAME_1, ");
//      fileWriter.append("USER SUBMITTED_1, ");
//      fileWriter.append("COST_PROJECT_1,");
//      fileWriter.append("TEAM-SIZE_1,");
//      fileWriter.append("WEEKLY_INDIVIDUAL_WORKLOAD_1,");
//      fileWriter.append("DURATION_1,");
//      fileWriter.append("POTENTIAL_START_DATE_1,");
//      fileWriter.append("UNI_1, ");
//
//      fileWriter.append("PROJECT_NAME_2, ");
//      fileWriter.append("USER SUBMITTED_2, ");
//      fileWriter.append("COST_PROJECT_2,");
//      fileWriter.append("TEAM-SIZE_2,");
//      fileWriter.append("WEEKLY_INDIVIDUAL_WORKLOAD_2,");
//      fileWriter.append("DURATION_2,");
//      fileWriter.append("POTENTIAL_START_DATE_2,");
//      fileWriter.append("UNI_2 ");
//
//      fileWriter.append(NEW_LINE_SEPARATOR);
//
//      for (Proposal proposal : proposals) {
//        if (proposal.getProposalStatus() == ProposalStatus.PAIRED) {
//
//          //--PROPOSAL 1--//
//          fileWriter.append(proposal.getName()+ ", ");
//          fileWriter.append(proposal.getUser().getName()+ ", ");
//          fileWriter.append(proposal.getCost()+ ", ");
//          fileWriter.append(proposal.getTeamSize() +", ");
//          fileWriter.append(proposal.getPerStudentWeekly()+", ");
//          fileWriter.append(proposal.getDurationInWeeks()+ ", ");
//          fileWriter.append(proposal.getPotentialStart()+", ");
//          fileWriter.append(proposal.getInstitution() + ", " );
//
//          //--PROPOSAL 2--//
//          fileWriter.append( proposal.getPaired().getName()+ ", ");
//          fileWriter.append(proposal.getPaired().getUser().getName()+ ", ");
//          fileWriter.append(proposal.getPaired().getCost()+ ", ");
//          fileWriter.append(proposal.getPaired().getTeamSize() +", ");
//          fileWriter.append(proposal.getPaired().getPerStudentWeekly()+", ");
//          fileWriter.append(proposal.getPaired().getDurationInWeeks()+ ", ");
//          fileWriter.append(proposal.getPaired().getPotentialStart()+", ");
//          fileWriter.append(proposal.getPaired().getInstitution().toString());
//
//        }
//        fileWriter.append(NEW_LINE_SEPARATOR);
//      }
//
//      System.out.println("CSV file was created successfully !!!");
//
//    } catch (Exception e) {
//      System.out.println("Error in CsvFileWriter !!!");
//      e.printStackTrace();
//    } finally {
//
//      try {
//        fileWriter.flush();
//        fileWriter.close();
//      } catch (IOException e) {
//        System.out.println("Error while flushing/closing fileWriter !!!");
//        e.printStackTrace();
//      }
//
//    }
//  }


  // FIXME doesn't work and doesn't have any permissions
  //Pairing Renew, allow users to create duplicate row
  //@RequiresPermissions(ILACRealm.PAIRING_VIEW_INSTANCE)
  //@RequiresPermissions(ILACRealm.PROPOSAL_EDIT_INSTANCE)
//  public Object onRenew(Pairing pairing) throws ResourceNotFoundException, ParseErrorException, Exception{
//
//    for (Proposal prop : pairing.getProposals()) {
//      if (StringUtils.equals(prop.getUser().getFederatedId(), userInfo.getUser().getFederatedId())) {
//        renewProposal = new Proposal();
//
//        if (renewProposal.getCreated() == null) {
//          renewProposal.setCreated(new Date());
//        }
//        renewProposal.setLastModified(new Date());
//        renewProposal.setShare(prop.getShare());
//        renewProposal.setProposalStatus(ProposalStatus.PendingRenewal);
//        renewProposal.setDescription(prop.getDescription());
//        renewProposal.setUser((User) context.localObject(userInfo.getUser().getObjectId(), null));
//        renewProposal.setPerStudentWeekly(prop.getPerStudentWeekly());
//        renewProposal.setTeamSize(prop.getTeamSize());
//        renewProposal.setDurationInWeeks(prop.getDurationInWeeks());
//        renewProposal.setName(prop.getName());
//        renewProposal.setPotentialStart(prop.getPotentialStart());
//        renewProposal.setCost(prop.getCost());
//
//        Set<ProposalType> existingProposals = new HashSet<>(prop.getTypes());
//        for (ProposalType propType : existingProposals) {
//          renewProposal.addToTypes(propType);
//        }
//
//        Set<Subject> existingSubjects = new HashSet<>(prop.getSubjects());
//        for (Subject subject : existingSubjects) {
//          renewProposal.addToSubjects(subject);
//        }
//
//        context.commitChanges();
//        alerts.success("Renewal Proposal submitted");
//        //notifyAdmins();
//        return dashboard;
//      }
//    }
//    return dashboard;
//  }
  
//  private void notifyAdmins() throws ResourceNotFoundException, ParseErrorException, Exception {
//    VelocityContext velContext = new VelocityContext();
//    velContext.put("proposal", renewProposal);
//    emailService.sendAdminEmail(velContext, "proposal-submitted.vm", "Proposal submission");
//  }
}
