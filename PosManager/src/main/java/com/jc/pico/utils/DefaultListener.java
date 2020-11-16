/*
 * Filename	: DefaultListener.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.1
 * Author   : 
 */

package com.jc.pico.utils;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.commons.validator.GenericValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//mport org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.ClassPathResource;

import com.jc.pico.configuration.Globals;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;
import ch.qos.logback.core.util.StatusPrinter;


@WebListener
public class DefaultListener implements ServletContextListener {
	protected static Logger logger = LoggerFactory.getLogger(DefaultListener.class);

	// 사용자별 설정 파일 사용시 name 변경 해서 사용.(절대 경로도 사용 가능)
	// private static final String CONF_FILE = "config_env_real.properties";
	private static final String CONF_FILE = "config.properties";
	// private static final String CONF_FILE = "config_env_dev.properties";

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		logger.debug("DefaultListener - contextInitialized!!");
		String absPath = getConfigFilePath(servletContextEvent, CONF_FILE);
		logger.info("config file = " + absPath);
		init(absPath);
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		logger.debug("DefaultListener - contextDestroyed!!");
	}

	private void init(String configFile) {
		logger.info("configFile = " + configFile);
		if (configFile == null) {
			logger.info("DefaultListener : configFile parameter is null");
		} else {
			Config.init(configFile);
			logger.info("DefaultListener : Initialize executed succesfully...");
		}

		try {
			// 
			// 체크 : 오류를 해결하면 로그가 콘솔로 출력이 안 됨
			// 나중에 다시 체크 바람
			// 로그 출력 제어
			 configureLogback();
		} catch (Exception e) {
			logger.warn("logback init fail!", e);
		}
	}

	private String getConfigFilePath(ServletContextEvent servletContextEvent, String configFile) {
		if (configFile.startsWith(File.separator)) {
			return configFile;
		} else {
			String basePath = servletContextEvent.getServletContext().getRealPath("/");
			// 수정
			String runMode = (GenericValidator.isBlankOrNull(System.getProperty("config.run.mode")) ? "real"
					: System.getProperty("config.run.mode"));
			// String runMode =
			// (GenericValidator.isBlankOrNull(System.getProperty("config.run.mode")) ?
			// "dev" : System.getProperty("config.run.mode"));
			String filename = String.format("config_env_%s.properties", runMode);
			return basePath + "WEB-INF" + File.separator + "classes" + File.separator + filename;
		}
	}

	private void configureLogback() throws IOException {

		// assume SLF4J is bound to logback in the current environment
	    LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
	    try {
	      JoranConfigurator jc = new JoranConfigurator();
	      jc.setContext(context);
	      context.reset(); // override default configuration
	      // inject the name of the current application as "application-name"
	      // property of the LoggerContext
	      context.putProperty("log.dir", Globals.LOG_DIR);
	      jc.doConfigure(new ClassPathResource("pico-logback.xml").getInputStream());
	    } catch (JoranException je) {
	      // StatusPrinter will handle this
	    }
	    StatusPrinter.printInCaseOfErrorsOrWarnings(context);

	  }

}
