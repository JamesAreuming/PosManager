/*
 * Filename	: ToolkitCommonsMultipartResolver.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

public class ToolkitCommonsMultipartResolver extends CommonsMultipartResolver {

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  protected MultipartParsingResult parseRequest(final HttpServletRequest request) {

    String encoding = determineEncoding(request);
    FileUpload fileUpload = prepareFileUpload(encoding);

    List fileItems;

    try {
      fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
    } 
    catch (FileUploadBase.SizeLimitExceededException ex) {
      request.setAttribute("fileSizeExceeded", ex);
      fileItems = Collections.EMPTY_LIST;            
    } 
    catch (FileUploadException ex) {
      throw new MultipartException("Could not parse multipart servlet request", ex);
    }

    return parseFileItems(fileItems, encoding);
  }
}