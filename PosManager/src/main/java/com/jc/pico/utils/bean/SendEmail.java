/*
 * Filename	: SendEmail.java
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
import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcMailAccount;

import java.util.List;
import java.util.Map;

public class SendEmail extends SendMessage {
  @JsonIgnore private SvcMailAccount account;
  private String from;
  private String contentType;
  private List<String> to;
  private List<String> cc;
  private List<String> bcc;

  @JsonGetter("account")
  public Map<String, Object> getAccountMap() {
    Map<String, Object> map = Maps.newHashMap();
    map.put("host", account.getHost());
    map.put("port", account.getPort());
    map.put("useSsl", account.getUseSsl());
    map.put("username", account.getUsername());
    map.put("password", account.getPassword());
    return map;
  }

  public SvcMailAccount getAccount() {
    return account;
  }

  public void setAccount(SvcMailAccount account) {
    this.account = account;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public List<String> getTo() {
    return to;
  }

  public void setTo(List<String> to) {
    this.to = to;
  }

  public List<String> getCc() {
    return cc;
  }

  public void setCc(List<String> cc) {
    this.cc = cc;
  }

  public List<String> getBcc() {
    return bcc;
  }

  public void setBcc(List<String> bcc) {
    this.bcc = bcc;
  }
}
