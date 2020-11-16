/*
 * Filename	: WebApplicationInitializerConfiguration.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

// web.xml 을 대신한다.
public class WebApplicationInitializerConfiguration implements WebApplicationInitializer {
  
  public void onStartup(ServletContext container) throws ServletException {

    AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
//    ctx.register(MainConfiguration.class);
//    ctx.register(MybatisConfiguration.class);
    ctx.setServletContext(container);
    
    container.addListener(new RequestContextListener());
    
    DispatcherServlet dispatcherServlet =  new DispatcherServlet(ctx);
    dispatcherServlet.setDetectAllHandlerAdapters(true);
    dispatcherServlet.setDetectAllHandlerExceptionResolvers(true);
    dispatcherServlet.setDetectAllHandlerMappings(true);
    dispatcherServlet.setDetectAllViewResolvers(true);
    dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
    
    ServletRegistration.Dynamic servlet = container.addServlet("dispatcher", dispatcherServlet);
    servlet.setLoadOnStartup(1);
    servlet.addMapping("/");
    
    
    FilterRegistration.Dynamic encodingFilter = container.addFilter("encoding-filter", new CharacterEncodingFilter());
    encodingFilter.setInitParameter("encoding", "UTF-8");
    encodingFilter.setInitParameter("forceEncoding", "true"); 
    encodingFilter.addMappingForUrlPatterns(null, true, "/*");
    
    
    FilterRegistration.Dynamic hiddenMethodFilter = container.addFilter("hiddenHttpMethodFilter", new HiddenHttpMethodFilter());
    hiddenMethodFilter.addMappingForUrlPatterns(null ,true, "/*");
  }

}
