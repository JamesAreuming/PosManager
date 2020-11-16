/*
 * Filename	: SendPush.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendPush extends SendMessage {
  @JsonIgnore
  private Map<String, List<String>> pushIds; // key: os, val: pushId

  private String os;

  private String type;
  private Map<String, Object> extra;

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
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

    this.extra.put(key, val);
  }

  public void setPushIds(Map<String, List<String>> pushIds) {
    this.pushIds = pushIds;
  }

  public void setOsType(String os) {
    this.os = os;
  }

  public boolean hasMessage(String os) {
    if (pushIds == null || pushIds.size() == 0 || os == null || os.length() == 0) {
      return false;
    } else {
      List<String> ids = pushIds.get(os);
      return (ids != null && ids.size() > 0);
    }
  }

  @JsonGetter("pushIds")
  public List<String> getPushIds() {
    if (pushIds == null || pushIds.size() == 0 || os == null || os.length() == 0) {
      return Lists.newArrayList();
    } else {
      return pushIds.get(os);
    }
  }


  @JsonIgnore
  @Override
  public String getSubject() {
    return super.getSubject();
  }

  @JsonIgnore
  @Override
  public String getMessage() {
    return super.getMessage();
  }

  @JsonGetter("message")
  public Map<String, Object> getGcmMessage() {
    String subject = getSubject();
    String msg = getMessage();

    Map<String, Object> map = Maps.newHashMap();
    if ("android".equals(os)) {
      map.put("title", subject);
      map.put("content", msg);
      map.put("type", type);
      map.put("extra", extra);
    } else if ("ios".equals(os)) {
      Map<String, Object> aps = Maps.newHashMap();
      HashMap<Object, Object> alert = Maps.newHashMap();
      alert.put("title", subject);
      alert.put("body", msg);
      aps.put("alert",  alert);
      aps.put("content-available", "1");
      aps.put("sound", "");
      aps.put("badge",  "0");
      map.put("extra", extra);
      map.put("type", type);
      map.put("aps",  aps);
    }

    return map;
  }
  
}
