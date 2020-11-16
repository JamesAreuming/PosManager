/*
 * Filename	: UserGroupAuthProcedure.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.jc.pico.bean.UserGroupAuth;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupAuthProcedure extends UserGroupAuth {

  private String upCd;

  private String title;

  private String gaId;

  private List<UserGroupAuthProcedure> nodes;

public String getUpCd() {
	return upCd;
}

public void setUpCd(String upCd) {
	this.upCd = upCd;
}

public String getTitle() {
	return title;
}

public void setTitle(String title) {
	this.title = title;
}

public String getGaId() {
	return gaId;
}

public void setGaId(String gaId) {
	this.gaId = gaId;
}

public List<UserGroupAuthProcedure> getNodes() {
	return nodes;
}

public void setNodes(List<UserGroupAuthProcedure> nodes) {
	this.nodes = nodes;
}


}