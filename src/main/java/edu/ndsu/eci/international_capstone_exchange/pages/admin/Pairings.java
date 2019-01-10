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

import edu.ndsu.eci.international_capstone_exchange.persist.Pairing;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import edu.ndsu.eci.international_capstone_exchange.persist.Proposal;
import edu.ndsu.eci.international_capstone_exchange.util.ProposalStatus;

public class Pairings {

  @Inject
  private ObjectContext context;
  
  @Property
  private Pairing row;

  @Property
  private List<Proposal> proposals;

  @Property
  private List<Pairing> ps;

  String fileName = System.getProperty("user.home")+"/Desktop/ADMIN ALL CSV.csv";
  
  public List<Pairing> getPairings() {
    return context.performQuery(new SelectQuery(Pairing.class));
  }

  //Delimiter used in CSV file
  private static final String NEW_LINE_SEPARATOR = "\n";

  public void onCsvdownload() {

    ps = new ArrayList<>();
    proposals = new ArrayList<>();
    ps = context.performQuery(new SelectQuery(Pairing.class));
    for (int i = 0; i < ps.size(); i++) {
      proposals.add(ps.get(i).getProposals().get(0));
      proposals.add(ps.get(i).getProposals().get(1));
    }

    FileWriter fileWriter = null;

    try {
      fileWriter = new FileWriter(fileName);

      //Write the CSV file header
      fileWriter.append("PROJECT_NAME_1, ");
      fileWriter.append("USER SUBMITTED_1, ");
      fileWriter.append("COST_PROJECT_1,");
      fileWriter.append("TEAM-SIZE_1,");
      fileWriter.append("WEEKLY_INDIVIDUAL_WORKLOAD_1,");
      fileWriter.append("DURATION_1,");
      fileWriter.append("POTENTIAL_START_DATE_1,");
      fileWriter.append("UNI_1, ");

      fileWriter.append("PROJECT_NAME_2, ");
      fileWriter.append("USER SUBMITTED_2, ");
      fileWriter.append("COST_PROJECT_2,");
      fileWriter.append("TEAM-SIZE_2,");
      fileWriter.append("WEEKLY_INDIVIDUAL_WORKLOAD_2,");
      fileWriter.append("DURATION_2,");
      fileWriter.append("POTENTIAL_START_DATE_2,");
      fileWriter.append("UNI_2 ");

      fileWriter.append(NEW_LINE_SEPARATOR);

      for (Proposal proposal : proposals) {
        if (proposal.getProposalStatus() == ProposalStatus.PAIRED) {

          //--PROPOSAL 1--//
          fileWriter.append(proposal.getName()+ ", ");
          fileWriter.append(proposal.getUser().getName()+ ", ");
          fileWriter.append(proposal.getCost()+ ", ");
          fileWriter.append(proposal.getTeamSize() +", ");
          fileWriter.append(proposal.getPerStudentWeekly()+", ");
          fileWriter.append(proposal.getDurationInWeeks()+ ", ");
          fileWriter.append(proposal.getPotentialStart()+", ");
          fileWriter.append(proposal.getInstitution() + ", " );

          //--PROPOSAL 2--//
          fileWriter.append( proposal.getPaired().getName()+ ", ");
          fileWriter.append(proposal.getPaired().getUser().getName()+ ", ");
          fileWriter.append(proposal.getPaired().getCost()+ ", ");
          fileWriter.append(proposal.getPaired().getTeamSize() +", ");
          fileWriter.append(proposal.getPaired().getPerStudentWeekly()+", ");
          fileWriter.append(proposal.getPaired().getDurationInWeeks()+ ", ");
          fileWriter.append(proposal.getPaired().getPotentialStart()+", ");
          fileWriter.append(proposal.getPaired().getInstitution().toString());

        }
        fileWriter.append(NEW_LINE_SEPARATOR);
      }

      System.out.println("CSV file was created successfully !!!");

    } catch (Exception e) {
      System.out.println("Error in CsvFileWriter !!!");
      e.printStackTrace();
    } finally {

      try {
        fileWriter.flush();
        fileWriter.close();
      } catch (IOException e) {
        System.out.println("Error while flushing/closing fileWriter !!!");
        e.printStackTrace();
      }

    }
  }
}
