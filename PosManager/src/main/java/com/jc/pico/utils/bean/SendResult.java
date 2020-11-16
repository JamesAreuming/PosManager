/*
 * Filename	: SendResult.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SendResult {
  private Long logId;
  private String queueName;
  private boolean success;
  private Map<String, Object> extra;
  private String logs;

  public Long getLogId() {
    return logId;
  }

  public void setLogId(long logId) {
    this.logId = logId;
  }

  public String getQueueName() {
    return queueName;
  }

  public void setQueueName(String queueName) {
    this.queueName = queueName;
  }

  public boolean isSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public Map<String, Object> getExtra() {
    return extra;
  }

  public void setExtra(Map<String, Object> extra) {
    this.extra = extra;
  }

  public String getLogs() {
    return logs;
  }

  public void setLogs(String logs) {
    this.logs = logs;
  }
}
