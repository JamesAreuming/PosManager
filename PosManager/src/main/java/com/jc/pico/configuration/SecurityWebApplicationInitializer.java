/*
 * Filename	: SecurityWebApplicationInitializer.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

// filter > springSecurityFilterChain > filter-mapping > url-pattern
public class SecurityWebApplicationInitializer
  extends AbstractSecurityWebApplicationInitializer {

  public SecurityWebApplicationInitializer() {
    super(SecurityConfig.class);
  }
}