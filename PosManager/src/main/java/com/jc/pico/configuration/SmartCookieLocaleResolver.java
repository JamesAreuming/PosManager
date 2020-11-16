/*
 * Filename	: SmartCookieLocaleResolver.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class SmartCookieLocaleResolver extends CookieLocaleResolver {

  private SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
  
  @Override
  protected Locale determineDefaultLocale(HttpServletRequest request) {
    return sessionLocaleResolver.resolveLocale(request);
  }
  
  @Override
  public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
    super.setLocale(request, response, locale);
    sessionLocaleResolver.setLocale(request, response, locale);
  }
  
  @Override
  public void setDefaultLocale(Locale defaultLocale) {
    sessionLocaleResolver.setDefaultLocale(defaultLocale);
  }
  
}
