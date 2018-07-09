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

import org.apache.commons.mail.SimpleEmail;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import edu.ndsu.eci.international_capstone_exchange.services.AppModule;
import edu.ndsu.eci.international_capstone_exchange.services.UserInfo;
import edu.ndsu.eci.international_capstone_exchange.services.VelocityEmailService;

public class Admin {
  /** user info service */
  @Inject
  private UserInfo userInfo;
 

  @Inject
  private VelocityEmailService emailService;
  
  public void onSendTestEmail() throws ResourceNotFoundException, ParseErrorException, Exception {
    VelocityContext context = new VelocityContext();
    context.put("user", userInfo.getUser().getSsoEmail());
    
    SimpleEmail email = emailService.setupSimpleEmail(context, "test.vm", "Test message");
    email.setFrom(AppModule.FROM_ADDRESS);
    email.addTo(userInfo.getUser().getSsoEmail());
    email.addTo(userInfo.getUser().getEmail());
    email.send();
  }

}
