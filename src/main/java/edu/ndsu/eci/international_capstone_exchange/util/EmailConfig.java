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
package edu.ndsu.eci.international_capstone_exchange.util;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * Email configuration class
 *
 */
public class EmailConfig {

  /** from address for the application */
  private String fromAddress;
  
  /** Whether emails should be sent out. True to send emails, false otherwise. */
  private boolean enabled;

  /**
   * Factory creator method.
   * @param environmentContext The file path within the environment set in the Jetty Env config file.
   * @return A EmailConfig file with config settings assigned.
   * @throws NamingException Parsing error from configuration.
   */
  public static EmailConfig createConfig(final String environmentContext) throws NamingException {
    Context initCtx = new InitialContext();
    Context envCtx = (Context) initCtx.lookup("java:comp/env");
    return (EmailConfig) envCtx.lookup(environmentContext);
  }
  
  /**
   * Get from address
   * @return from address
   */
  public String getFromAddress() {
    return fromAddress;
  }

  /**
   * Set from address
   * @param fromAddress from address
   */
  public void setFromAddress(String fromAddress) {
    this.fromAddress = fromAddress;
  }
  
  /**
   * Getter for enabled.
   * @return Enabled variable.
   */
  public boolean isEnabled() {
    return enabled;
  }
  
  /**
   * Setter for enabled. Gives value as String since value is taken from the jetty-env.xml file.
   * @param enabled True if emails should be sent, false otherwise.
   */
  public void setEnabled(String enabled) {
    this.enabled = ("true" == enabled.toLowerCase());
  }
  
  
}
