/*
 * Filename	: Beacon.java
 * Function	:
 * Comment 	:
 * History	:
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Beacon {
  @JsonProperty("UUID")
  private String UUID;
  private int major;
  private int minor;
  private float distance;

  public String getUUID() {
    return UUID;
  }

  public void setUUID(String UUID) {
    this.UUID = UUID;
  }

  public int getMajor() {
    return major;
  }

  public void setMajor(int major) {
    this.major = major;
  }

  public int getMinor() {
    return minor;
  }

  public void setMinor(int minor) {
    this.minor = minor;
  }

  public float getDistance() {
    return distance;
  }

  public void setDistance(float distance) {
    this.distance = distance;
  }
}
