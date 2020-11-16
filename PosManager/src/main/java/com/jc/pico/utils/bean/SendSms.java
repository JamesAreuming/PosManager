/*
 * Filename	: SendSms.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import java.util.List;

public class SendSms extends SendMessage {
  private Long logId;
  private String from;
  private String to;
  private List<String> images;


  public Long getLogId() {
    return logId;
  }

  public void setLogId(long logId) {
    this.logId = logId;
  }

  public String getFrom() {
    return from;
  }

  public void setFrom(String from) {
    this.from = from;
  }

  public String getTo() {
    return to;
  }

  public void setTo(String to) {
    this.to = to;
  }

  public List<String> getImages() {
    return images;
  }

  public void setImages(List<String> images) {
    this.images = images;
  }
}
