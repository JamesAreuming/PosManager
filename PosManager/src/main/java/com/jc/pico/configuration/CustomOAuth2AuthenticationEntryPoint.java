/*
 * Filename	: CustomOAuth2AuthenticationEntryPoint.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.oauth2.provider.error.DefaultOAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletWebRequest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.User;
import com.jc.pico.exception.InvalidJsonException;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.bean.AppJsonResult;
import com.jc.pico.utils.bean.ClerkResult;
import com.jc.pico.utils.bean.Header;
import com.jc.pico.utils.bean.StoreResult;

@Component("oAuth2AuthenticationEntryPoint")
public class CustomOAuth2AuthenticationEntryPoint extends OAuth2AuthenticationEntryPoint {
  
  @Autowired
  MessageSource messageSource;
  
  public CustomOAuth2AuthenticationEntryPoint() {
    this.setExceptionRenderer(oauth2ExceptionRenderer());
  }

  private OAuth2ExceptionRenderer oauth2ExceptionRenderer() {
    return new DefaultOAuth2ExceptionRenderer() {
      @Override
      public void handleHttpEntityResponse(HttpEntity<?> responseEntity, ServletWebRequest webRequest) throws Exception {
        logger.debug("OAuth2ExceptionRenderer!!");
        ResponseEntity<?> entity = null;
        if (responseEntity != null) {
          logger.debug("responseEntity - " + responseEntity.getBody().getClass());
          HttpServletResponse servletResponse = (HttpServletResponse) webRequest.getNativeResponse();
          ServletServerHttpResponse response = new ServletServerHttpResponse(servletResponse);

          Map<String, String> body = (Map<String, String>) JsonConvert.JsonConvertObject(JsonConvert.toJson(responseEntity.getBody()), new TypeReference<Map<String, String>>(){});
          String errDescription = body.get("error_description");
          logger.debug("kdy 22 : " + JsonConvert.toJson(body));
          
          Map<String, Object> errDesc;
          try {
        	  errDesc = (Map<String, Object>) JsonConvert.JsonConvertObject(errDescription, new TypeReference<Map<String, Object>>(){});
          } catch(InvalidJsonException e) {
        	  errDesc = Collections.emptyMap();
          }
          
          String clientId = "";
          String errCd = "";
          User user = new User();
          
          if (errDesc.get("clientId") != null) {
            clientId = (String) errDesc.get("clientId");
          }
          if (errDesc.get("error") != null) {
            errCd = (String) errDesc.get("error");
          }
          if (errDesc.get("user") != null) {
            user =  (User) JsonConvert.JsonConvertObject(JsonConvert.toJson(errDesc.get("user")), new TypeReference<User>(){});
          }
          
          if ("mobile".equals(clientId)) {
            AppJsonResult result = new AppJsonResult();
            Map<String, Object> data = Maps.newHashMap();
            Header header = new Header();
            header.setErrCd(errCd);
            header.setErrMsg(messageSource.getMessage(errCd, new String[]{}, webRequest.getLocale()));
            result.setHeader(header);
            
            data.put("loginFailCnt",  user.getLoginFailCnt());
            result.setData(data);
            entity = new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
          } else if(StoreResult.ErrorCode.ERROR_LOGIN_LIMIT_EXCEEDED.code.equals(errCd)) {        	  
        	  
        	  StoreResult result = new StoreResult();
        	  result.setError(errCd, (String) errDesc.get("message"));        	  
        	  
        	  if(Objects.equals("android", errDesc.get("osType"))) {        	  
            	  entity = new ResponseEntity<>(result, ((ResponseEntity<?>) responseEntity).getStatusCode());
        	  } else {
            	  // 2016.12.05 : Store iOS 앱에서 http status code가 200이 아니면 파싱 처리를 못하는 상황이라 200으로 내려 보냄        	  
            	  entity = new ResponseEntity<>(result, HttpStatus.OK);
        	  }

        	  
          } else if(Globals.ERROR_USER_LOGIN_LIMIT_EXCEEDED.equals(errCd)) {
        	  
        	  ClerkResult result = new ClerkResult();        	  
        	  result.setError(errCd, (String) errDesc.get("message"));
        	  entity = new ResponseEntity<>(result, ((ResponseEntity<?>) responseEntity).getStatusCode());
        	  
          } else {
            HttpHeaders headers = response.getHeaders();
            if (!responseEntity.getHeaders().isEmpty()) {
              headers.putAll(responseEntity.getHeaders());
            }
            headers.put("Access-Control-Allow-Origin", Lists.newArrayList("*"));
            headers.put("Content-Type", Lists.newArrayList("application/json;charset=utf-8"));

            logger.debug("kdy 22");
            AppJsonResult result = new AppJsonResult();
            result.setHeader(Header.newError(Globals.ERROR_UNAUTHORIZED, "Unauthorized(Access Denied)"));
            entity = new ResponseEntity<>(result, headers, ((ResponseEntity<?>) responseEntity).getStatusCode());
          }
        }
        super.handleHttpEntityResponse((entity != null ? entity : responseEntity), webRequest);
      }
    };
  }
}
