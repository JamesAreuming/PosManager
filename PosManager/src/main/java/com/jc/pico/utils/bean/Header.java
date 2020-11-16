/*
 * Filename : Header.java
 * Function :
 * Comment  :
 * History  : 
 *
 * Version  : 1.0
 * Author   : 
 */
package com.jc.pico.utils.bean;

import java.util.Map;
import java.util.TimeZone;

import com.google.common.collect.Maps;

public class Header {
 
  private String os;
  private String lang;
  private String  errCd;
  private String errMsg;
  private String timezone;
  private Map<String, Object> extra;
  
  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getLang() {
    return lang;
  }

  public void setLang(String lang) {
    this.lang = lang;
  }

  public String getErrCd() {
    return errCd;
  }

  public void setErrCd(String errCd) {
    this.errCd = errCd;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public void setErrMsg(String errMsg) {
    this.errMsg = errMsg;
  }
  
  public static Header newError(String errCd, String errMsg) {
    Header header = new Header();
	header.setErrCd(errCd);
	header.setErrMsg(errMsg);
	return header;
  }

  public String getTimezone() {
    return timezone;
  }

  public void setTimezone(String timezone) {
    this.timezone = timezone;
  }

 public Map<String, Object> getExtra() {
    return extra;
  }

  public void setExtra(Map<String, Object> extra) {
    this.extra = extra;
  }

public void addExtra(String key, Object val) {
   if (this.extra == null) {
     this.extra = Maps.newHashMap();
   }
   this.extra.put(key,  val);
 }
  
}
