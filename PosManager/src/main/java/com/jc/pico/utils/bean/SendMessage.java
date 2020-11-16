/*
 * Filename	: SendMessage.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class SendMessage {
  public enum Token {
    SEC_TOKEN,
    USER_ID,
    USER_NAME,
    USER_EMAIL,
    USER_MB,
    USER_TEL,
    USER_ZIP,
    USER_COUNTRY,
    USER_REGION,
    USER_CITY,
    USER_ADDR1,
    USER_ADDR2,
    BRAND_NAME,
    ;

    public String getToken() {
      return String.format("\\[::%s::\\]", this.name());
    }
  }

  private Long logId;
  @JsonIgnore private String title;
  @JsonIgnore private String content;
  @JsonIgnore private Map<Token, String> replaceMap;

  // for log
  @JsonIgnore private Long brandId;

  public Long getLogId() {
    return logId;
  }

  public void setLogId(long logId) {
    this.logId = logId;
  }

  public String getSubject() {
    return replace(title, replaceMap);
  }

  public String getMessage() {
    return replace(content, replaceMap);
  }

  private static String replace(String text, Map<Token, String> replaceMap) {
    String result = text;
    if (replaceMap != null && replaceMap.size() > 0) {
      Set<Token> keys = replaceMap.keySet();
      Iterator<Token> itr = keys.iterator();
      while(itr.hasNext()) {
        Token token = itr.next();
        String val = (replaceMap.get(token) != null ? replaceMap.get(token) : "");
        result = result.replaceAll(token.getToken(), val);
      }
    }

    return result;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public Map<Token, String> getReplaceMap() {
    return replaceMap;
  }

  public void setReplaceMap(Map<Token, String> replaceMap) {
    this.replaceMap = replaceMap;
  }

  public Long getBrandId() {
    return brandId;
  }

  public void setBrandId(Long brandId) {
    this.brandId = brandId;
  }
}
