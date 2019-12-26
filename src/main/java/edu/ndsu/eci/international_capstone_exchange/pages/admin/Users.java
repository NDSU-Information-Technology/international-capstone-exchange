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
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.BeanModelSource;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import com.googlecode.tapestry5cayenne.annotations.CommitAfter;

import edu.ndsu.eci.international_capstone_exchange.persist.CapstoneDomainMap;
import edu.ndsu.eci.international_capstone_exchange.persist.Pairing;
import edu.ndsu.eci.international_capstone_exchange.persist.Proposal;
import edu.ndsu.eci.international_capstone_exchange.persist.Role;
import edu.ndsu.eci.international_capstone_exchange.persist.User;
import edu.ndsu.eci.international_capstone_exchange.services.UserInfo;
import edu.ndsu.eci.international_capstone_exchange.services.VelocityEmailService;
import edu.ndsu.eci.international_capstone_exchange.util.Status;
import edu.ndsu.eci.international_capstone_exchange.util.UserRole;

/**
 * Manages User status and role.
 *
 */
public class Users {

  /** Cayenne database reference */
  @Inject
  private ObjectContext context;
  
  /** Logged in user information */
  @Inject
  private UserInfo userInfo;
  
  
  /** User row selection */
  @Property
  private User row;
  
  /** Holds proposal reference */
  @Property
  private Proposal proposal;
  
  /** Holds pairing reference */
  @Property
  private Pairing pairing;
  
  
  
  /** Email service */
  @Inject
  private VelocityEmailService emailService;
  
  /** Database map reference */
  private CapstoneDomainMap map = CapstoneDomainMap.getInstance();
  
  
  /** BeanModel */
  @Inject
  private BeanModelSource beanModelSource;
  
  /** Resources for the DataTables */
  @Inject
  private ComponentResources resources;
  
  /** BeanModel for the datatables */
  private BeanModel<User> bmodel;
  
  
  /** Required to use JavaScript files */
  @Inject
  private JavaScriptSupport javaScriptSupport;
  
  /**
   * Getter for Users with Pending status.
   * @return List of Users.
   */
  public List<User> getPending() {
    return map.performUsersByStatus(context, Status.PENDING);
  }
  
  /**
   * Getter for Users with Active status.
   * @return List of Users.
   */
  public List<User> getActive() {
    return map.performUsersByStatus(context, Status.APPROVED);
  }
  
  /**
   * Getter for Users with Decommissioned status.
   * @return List of Users.
   */
  public List<User> getDeactivated() {
    return map.performUsersByStatus(context, Status.DECOMMISSIONED);
  }
  
  /**
   * Getter for Users with Admin role.
   * @return List of Users.
   */
  public List<User> getAdmins() {
    return map.performUsersByRoleQuery(context, UserRole.ADMIN);
  }

  
  /**
   * Handles User approval and emails the update.
   * @param user The User being approved.
   * @throws ResourceNotFoundException when failing on velocity
   * @throws ParseErrorException when failing on velocity
   * @throws Exception when failing on velocity
   */
  @CommitAfter
  public void onApprove(User user) throws ResourceNotFoundException, ParseErrorException, Exception {
    user.setStatus(Status.APPROVED);
    VelocityContext velContext = new VelocityContext();
    emailService.sendUserEmail(velContext, "account-approved.vm", user, "International Capstone Exchange account approved.");
  }
  
  /**
   * Handles User denial.
   * @param user The User being denied.
   */
  @CommitAfter
  public void onDeny(User user) {
    user.setStatus(Status.DECLINED);
  }
  
  /**
   * Handles User deactivation.
   * @param user The User being deactivated.
   */
  @CommitAfter
  public void onDeactivateUser(User user) {
    user.setStatus(Status.DECOMMISSIONED);
  }
  
  /**
   * Handles User reactivation.
   * @param user The User being reactivated.
   */
  @CommitAfter
  public void onReactivateUser(User user) {
    user.setStatus(Status.APPROVED);
  }
  
  /**
   * Handles User being set to Admin role.
   * @param user The User being set to Admin.
   */
  @CommitAfter
  public void onMakeAdmin(User user) {
    // cop out to not have to do a check for the grid
    if (user.getRoles().contains(UserRole.ADMIN)) {
      return;
    }
    Role role = context.newObject(Role.class);
    role.setRole(UserRole.ADMIN);
    role.setUser(user);
   }
  
  /**
   * Handles User being removed of Admin role.
   * @param user The User being remove from Admin.
   */
  @CommitAfter
  public void onRemoveAdmin(User user) {
    // can't remove self
    if (userInfo.getUser().getFederatedId().equals(user.getFederatedId())) {
      return;
    }
    
    for (Role role : user.getRoles()) {
      if (role.getRole() == UserRole.ADMIN) {
        context.deleteObject(role);
        break;
      }
    }
  }
  
  /**
   * AfterRender code.
   */
  void afterRender() {
    javaScriptSupport.require("bootstrap/tab");
  }
  
  /**
   * Returns a JSONObject options for the datatable
   * 
   * @return JSONObject The options for the datatable
   */
  public JSONObject getOptions() {

    // The available options are documented at http://www.datatables.net/ref

    JSONObject options = new JSONObject();
    options.put("bJQueryUI", "true");
    //options.put("bDeferRender", "true");
    options.put("paginate", "false");

    return options;
  }
  
  /**
   * Returns the BeanModel for the datatable, needed to help with empty lists
   * 
   * @return beanmodel for datatable
   */
  public BeanModel<User> getBModel() {
    this.bmodel = beanModelSource.createDisplayModel(User.class, resources.getMessages());
    return bmodel;
  }
}
