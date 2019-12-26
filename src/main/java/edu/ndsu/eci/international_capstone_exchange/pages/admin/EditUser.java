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

import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.InjectPage;
import org.apache.tapestry5.annotations.PageActivationContext;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.BeanEditForm;
import org.apache.tapestry5.ioc.annotations.Inject;

import com.googlecode.tapestry5cayenne.annotations.CommitAfter;

import edu.ndsu.eci.international_capstone_exchange.persist.User;

/**
 * Admin edit of user. Currently just institution.
 *
 */
public class EditUser {

  /** context */
  @PageActivationContext
  @Property
  private User user;
  
  /** page to go back to */
  @InjectPage
  private Users usersPage;
  
  /** alert service, which appears to not be included in the template */
  @Inject
  private AlertManager alerts;
  
  /** form */
  @Component
  private BeanEditForm form;
  
  /**
   * On success persist
   * @return go back to users
   */
  @CommitAfter
  public Object onSuccessFromForm() {
    alerts.success("Changed institution");
    return usersPage;
  }
}
