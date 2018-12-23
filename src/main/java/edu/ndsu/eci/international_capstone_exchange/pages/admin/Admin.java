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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.cayenne.ObjectContext;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Persist;
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
  
  /** Holds log level selection. */
  @Property
  private String levelSelect;
  
  /** Logger reference */
  private static final Logger LOGGER = Logger.getLogger(Admin.class);
  
  
  /** Database map reference */
  private CapstoneDomainMap map = CapstoneDomainMap.getInstance();
  
  /**
   * Setup Render used to get pending task information.
   */
  public void setupRender() {
    proposalCount = map.performProposalsByStatus(context, ProposalStatus.PENDING).size();
    userCount = map.performUsersByStatus(context, Status.PENDING).size();
  }
  
  /**
   * Get value of levelSelect.
   */
  public void onActivate(String value) {
    this.levelSelect = value;
  }
  
  /**
   * Set submission value of levelSelect.
   * @return Value of levelSelect.
   */
  public String onPassivate() {
    return levelSelect;
  }
  
  /**
   * On Success of Log Form submission, change log level as appropriate.
   * Based off this example: http://www.mooreds.com/wordpress/archives/238
   */
  public void onSuccess() {
    //Changes root logger settings, the easy way
    //LogManager.getRootLogger().setLevel(Level.toLevel(levelSelect));
    
    //Get log4j properties file for runtime changes
    Properties properties = new Properties();
    try {
      InputStream stream = getClass().getResourceAsStream("/log4j.properties");
      properties.load(stream);
      stream.close();
    } catch(IOException ioe) {
      LOGGER.warn("Could not load log4j properties file.", ioe);
    }
    
    //Specify which settings should be changed
    properties.setProperty("log4j.rootCategory", levelSelect + ", A1");
    //properties.setProperty("log4j.category.edu.ndsu.eci.international_capstone_exchange.auth.LocalDevRealm", levelSelect);
    //properties.setProperty("log4j.logger.org.apache.cayenne.access.QueryLogger", levelSelect);
    
    //Apply config changes
    LogManager.resetConfiguration();
    PropertyConfigurator.configure(properties);
  }
}
