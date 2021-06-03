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
package edu.ndsu.eci.international_capstone_exchange.util;

import java.io.IOException;
import java.io.InputStream;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.services.Response;

public class BinaryStreamResponse implements StreamResponse {
  /** Attachment disposition type for popping up the save as dialog */
  public static final String ATTACHMENT_DISPOSITION = "attachment";
  /** Inline disposition type for displaying content in the browser */
  public static final String INLINE_DISPOSITION = "inline";
  
  /** default filename */
  protected String filename = "default";
  /** input stream */
  protected InputStream stream = null;
  /** default content type */
  private String contentType = "application/octet-stream";
  /** set download */
  private String disposition = ATTACHMENT_DISPOSITION;
  
  /**
   * EciStreamResponse
   * Configure the response with the given input stream and arguments
   * @param stream input stream
   * @param args parameters
   */
  public BinaryStreamResponse(InputStream stream, String... args) {
    this.stream = stream;
    if (args.length > 0) {
      this.filename = args[0];
    }
  }

  /**
   * getFilename()
   *
   * @return file name
   */
  public String getFilename() {
    return filename;
  }

  /**
   * setContentType()
   *
   * @param type file content type
   */
  public void setContentType(String type) {
    contentType = type;
  }

  /**
   * setFilename()
   *
   * @param filename file name
   */
  public void setFilename(String filename) {
    this.filename = filename;
  }
  
  /**
   * The Content-Disposition header type.
   * @param disposition typically attachment or inline.
   */
  public void setDisposition(String disposition) {
    this.disposition = disposition;
  }

  /**
   * Content-Disposition header type
   * @return header type, typically attachment or inline.
   */
  public String getDisposition() {
    return this.disposition;
  }
  
  @Override
  public String getContentType() {
    return contentType;
  }

  @Override
  public InputStream getStream() throws IOException {
    return stream;
  }

  @Override
  public void prepareResponse(Response arg0) {
    arg0.setHeader("Content-Disposition", disposition + "; filename=" + filename);
  }
}
