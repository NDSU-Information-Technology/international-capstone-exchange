// Copyright 2021 North Dakota State University
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
import java.util.Date;
import java.util.List;

import org.apache.cayenne.ObjectContext;
import org.apache.cayenne.query.SelectQuery;
import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.corelib.components.Form;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.upload.internal.services.UploadedFileItem;

import edu.ndsu.eci.international_capstone_exchange.persist.SponsoredProject;

public class SponsoredProjects {

  @Component
  private Form form;
  
  @Inject
  private ObjectContext context;
  
  @Property
  private SponsoredProject project;
  
  @Property
  private UploadedFileItem document;
  
  @Property
  private String company;
  
  @Property
  private String title;
  
  @Property
  private SponsoredProject row;
  
  public List<SponsoredProject> getProjects() {
    return context.performQuery(new SelectQuery(SponsoredProject.class));
  }
  
  public void onSuccessFromForm() {
    SponsoredProject proj = context.newObject(SponsoredProject.class); 
    proj.setProjectTitle(title);
    proj.setCompany(company);
    proj.setAdded(new Date());
    proj.setOpen(true);
    proj.setFilename(document.getFileName());
    try {
      proj.setProjectFile(IOUtils.toByteArray(document.getStream()));
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      context.rollbackChanges();
      return;
    }
    context.commitChanges();
  }
  
  public void onDelete(SponsoredProject project) {
    context.deleteObject(project);
    context.commitChanges();
  }
  
  public void onToggle(SponsoredProject project) {
    project.setOpen(!project.getOpen());
    context.commitChanges();
  }
  
}
