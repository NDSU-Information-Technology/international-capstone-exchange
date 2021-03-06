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

import java.io.ByteArrayInputStream;

import edu.ndsu.eci.international_capstone_exchange.persist.SponsoredProject;
import edu.ndsu.eci.international_capstone_exchange.util.BinaryStreamResponse;

public class ProjectStore {

  
  public BinaryStreamResponse onActivate(SponsoredProject proj) {
    // TODO set the filename to something sane
    return new BinaryStreamResponse(new ByteArrayInputStream(proj.getProjectFile()), proj.getFilename());
  }
}
