package com.jc.pico.utils.bean;

public class AppJsonResult {

  private Header header;
  private Object data;

  public AppJsonResult() {}
  
  public void setHeader(Header header) {
    this.header = header;
  }

  public Header getHeader() {
    return header;
  }

  public Object getData() {
    return data;
  }

  public void setData(Object data) {
    this.data = data;
  }

}

