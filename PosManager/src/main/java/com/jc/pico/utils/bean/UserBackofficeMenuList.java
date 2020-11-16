package com.jc.pico.utils.bean;

import java.util.List;

import com.jc.pico.bean.UserBackofficeMenu;

public class UserBackofficeMenuList extends UserBackofficeMenu {
  List<UserBackofficeMenuList> nodes;

  public List<UserBackofficeMenuList> getNodes() {
    return nodes;
  }

  public void setNodes(List<UserBackofficeMenuList> nodes) {
    this.nodes = nodes;
  }
  
  
}
