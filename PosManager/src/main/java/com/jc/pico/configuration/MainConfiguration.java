/*
 * Filename	: MainConfiguration.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@EnableAsync
@EnableScheduling
@ComponentScan(basePackages = { "com.jc.pico.*" })
public class MainConfiguration extends WebMvcConfigurerAdapter implements AsyncConfigurer, SchedulingConfigurer {

	@Autowired
	ModelAndViewSetLocaleInterceptor modelAndViewSetLocaleInterceptor;

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/app-resource/**").addResourceLocations("file:" + Globals.APP_RESOURCE + "/");

		registry.addResourceHandler("/image-resource/**").addResourceLocations("file:" + Globals.IMG_RESOURCE + "/");

		registry.addResourceHandler("/admin/_static/**").addResourceLocations("/admin/_static/");

		registry.addResourceHandler("/mobile/_static/**").addResourceLocations("/mobile/_static/");

		registry.addResourceHandler("/bin-resource/**").addResourceLocations("file:" + Globals.BIN_RESOURCE + "/");

		super.addResourceHandlers(registry);
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}

	@Bean(name = "localeResolver")
	public LocaleResolver cookieLocaleResolver() {
		CookieLocaleResolver localeResolver = new CookieLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("ko"));

		localeResolver.setCookieName("lang");
		localeResolver.setCookieMaxAge(100000);

		return localeResolver;
	}

	@Bean
	public FileSystemResource fileSystemResource() {
		return new FileSystemResource(Globals.UPLOAD_TEMP);
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver commonsMultipartResolver() throws IOException {
		ToolkitCommonsMultipartResolver commonsMultipartResolver = new ToolkitCommonsMultipartResolver();

		commonsMultipartResolver.setMaxUploadSize(73400320);
		commonsMultipartResolver.setUploadTempDir(fileSystemResource());

		return commonsMultipartResolver;
	}

	@Bean
	public MessageSource messageSource() {
		ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
		messageSource.setBasename("classpath:/i18n/messages");
		messageSource.setDefaultEncoding("UTF-8");
		messageSource.setCacheSeconds(5);
		return messageSource;
	}

	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());
		registry.addInterceptor(modelAndViewSetLocaleInterceptor);
	}

	@Bean
	public ViewResolver viewResolver() {
		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setViewClass(JstlView.class);
		viewResolver.setPrefix("/WEB-INF/views/");
		viewResolver.setSuffix(".jsp");
		return viewResolver;
	}

	// Login page intercept
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/mobile/login").setViewName("index");
		registry.addViewController("/admin/login").setViewName("admin/login");
		registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
	}

	@Bean
	public MappingJackson2HttpMessageConverter customJackson2HttpMessageConverter() {
		MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.setDateFormat(new SimpleDateFormat(Globals.JSON_DATETIME_FORMAT));
		jsonConverter.setObjectMapper(objectMapper);
		return jsonConverter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(customJackson2HttpMessageConverter());
	}

	/**
	 * 결제 처리용 비동기 실행자
	 * 
	 * @return
	 */
	@Bean(name = "paymentExecutor")
	public Executor getPaymentAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setThreadNamePrefix("PaymentExecutor-");
		executor.initialize();
		return executor;
	}

	/**
	 * 결제 처리용 비동기 실행자
	 * 
	 * @return
	 */
	@Bean(name = "mqttExecutor")
	public Executor getMqttAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(1);
		executor.setMaxPoolSize(1);
		executor.setThreadNamePrefix("MqttExecutor-");
		executor.initialize();
		return executor;
	}

	/**
	 * default async executor
	 */
	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(40);
		executor.setThreadNamePrefix("DefaultAsyncExecutor-");
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new GlobalAsyncUncaughtExceptionHandler();
	}

	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(getSchedulerExecutor());

	}

	/**
	 * 스케쥴러 실행자
	 * 
	 * @return
	 */
	@Bean(name = "schedulerExecutor")
	public Executor getSchedulerExecutor() {
		return Executors.newScheduledThreadPool(10);
	}

	/**
	 * 주문 싱크 처리자
	 * 
	 * @return
	 */
	@Bean(name = "orderSyncExecutor")
	public Executor getOrderSyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(10);
		executor.setMaxPoolSize(20);
		executor.setThreadNamePrefix("OrderSyncExecutor-");
		executor.initialize();
		return executor;
	}

}
