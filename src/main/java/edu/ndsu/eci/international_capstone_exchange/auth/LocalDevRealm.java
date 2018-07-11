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
package edu.ndsu.eci.international_capstone_exchange.auth;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.realm.AuthenticatingRealm;
import org.apache.shiro.subject.SimplePrincipalCollection;

import edu.ndsu.eci.international_capstone_exchange.util.SingleAuthToken;

public class LocalDevRealm extends AuthenticatingRealm {

  private final String password;
  
  public LocalDevRealm(String password) {
    setAuthenticationTokenClass(UsernamePasswordToken.class);
    this.password = password;
  }
  
  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
    SimplePrincipalCollection collect = new SimplePrincipalCollection(token.getPrincipal(), getName());
    collect.add(new SingleAuthToken(token.getPrincipal().toString()), getName());
    
    return new SimpleAuthenticationInfo(collect, token.getCredentials());
  }

//  @Override
//  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//    SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
//    authorizationInfo.addRole(FederatedAccountsRealm.ADMIN_ROLE);
//    authorizationInfo.addRole(FederatedAccountsRealm.APPROVED_USER_ROLE);
//        
//    return authorizationInfo;
//  }

}
