package com.jc.pico.configuration;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

public class GlobalAsyncUncaughtExceptionHandler implements AsyncUncaughtExceptionHandler {

	protected static final Logger logger = LoggerFactory.getLogger(GlobalAsyncUncaughtExceptionHandler.class);

	@Override
	public void handleUncaughtException(Throwable ex, Method method, Object... params) {
		logger.error("AsyncUncaughtException method=" + method.getName(), ex);
	}

}
