/*
 * Filename	: OAuth2ServerConfig.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.TokenApprovalStore;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import com.jc.pico.exception.OAuth2LoginException;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.AppJsonResult;
import com.jc.pico.utils.bean.Header;
import com.jc.pico.utils.bean.PosResult;

public class OAuth2ServerConfig {
  protected static Logger logger = LoggerFactory.getLogger(OAuth2ServerConfig.class);
  private static final String SERVER_RESOURCE_ID = "oauth2server";

  @Configuration
  @EnableResourceServer // 토근과 함께 호출하는 API에 대한 것을 검증하는 필터같은 역활
  protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    @Autowired
    @Qualifier("oAuth2AuthenticationEntryPoint")
    private OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint;

    @Override
    public void configure(final ResourceServerSecurityConfigurer resources) throws Exception {
      resources
          .resourceId(SERVER_RESOURCE_ID)
          .authenticationEntryPoint(oAuth2AuthenticationEntryPoint)
          .accessDeniedHandler(new OAuth2AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException authException) throws IOException, ServletException {
              String uri = request.getRequestURI();
              response.addHeader("Access-Control-Allow-Origin", "*");
              response.addHeader("Content-Type", "application/json;charset=utf-8");

              if (uri.startsWith("/api/pos")) {
                logger.error("POS");
                PosResult result = new PosResult();
                result.setSuccess(false);
                String msg = "[" + PosUtil.EPO_0004_CODE + "][" + PosUtil.EPO_0004_MSG + "] Unauthorized(Access Denied)";
                result.setResultMsg(msg);
                response.getWriter().write(JsonConvert.toJson(result));
                super.handle(request, response, authException);
              } else {
                AppJsonResult result = new AppJsonResult();
                result.setHeader(Header.newError(Globals.ERROR_UNAUTHORIZED, "Unauthorized(Access Denied)"));
                response.getWriter().write(JsonConvert.toJson(result));
                super.handle(request, response, authException);
              }
            }
          });
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
      http
          .regexMatcher("^((\\/api)|(\\/app)|(\\/store)|(\\/clerk))\\/.*")
          .authorizeRequests()
          .antMatchers(HttpMethod.OPTIONS, "/api/**", "/app/public/**", "/app/model/**", "/clerk/api/**").permitAll() // for CORS - preflight
          .antMatchers("/api/pos/store/M_LICENSE_SAVE").access("hasRole('ROLE_AGENT')")
          .antMatchers("/api/**").access("#oauth2.hasScope('read')")
          .antMatchers("/app/public/**").access("#oauth2.hasScope('read')")
          .antMatchers("/app/model/**").access("#oauth2.hasScope('read')")
          .antMatchers("/store/api/**").access("#oauth2.hasScope('read')")
          .antMatchers("/clerk/api/**").access("#oauth2.hasScope('read')");
    }

  }

  @Configuration
  @ComponentScan("com.jc.pico.configuration")
  @EnableAuthorizationServer // 토근을 발행하고 발행된 토큰을 검증하는 역활
  protected static class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    @Qualifier("dataSource")
    private DataSource dataSource;

    @Autowired
    @Qualifier("authenticationManager")
 // Spring Boot에 의해 생성, 이 것은 application.yml 마다 "user"라는 이름으로 "password"라는 패스워드와 함계 싱글유저를 가짐
    private AuthenticationManager authenticationManager;

    @Autowired
    @Qualifier("oAuth2AuthenticationEntryPoint")
    private OAuth2AuthenticationEntryPoint oAuth2AuthenticationEntryPoint;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
      clients
          .jdbc(dataSource);
      //      .inMemory() /* inMemory TokenStore */
        /*
       //* Client Create Data

      .passwordEncoder(passwordEncoder)
      .withClient("mobile")
      .resourceIds(SERVER_RESOURCE_ID)
      .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit", "client_credentials")
      .authorities("ROLE_CLIENT")
      .scopes("read", "write","trust")
      .accessTokenValiditySeconds(1000 * 60 * 60 * 24)
      .secret("secret")
      .redirectUris("redirect_uri");
      */
    }

    @Bean
    public TokenStore tokenStore() {
      /* inMemory TokenStore */
      //      return new InMemoryTokenStore();
      /* JDBC TokenStore */
      return new JdbcTokenStore(dataSource);
    }

    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
      return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
      endpoints
          .authorizationCodeServices(authorizationCodeServices())
          .authenticationManager(authenticationManager)
          .tokenStore(tokenStore())
          // AcceceToken Request Path Mapping
          .pathMapping("/oauth/token", "/api/login")
          .exceptionTranslator(new WebResponseExceptionTranslator() {
            @Override
            public ResponseEntity<OAuth2Exception> translate(Exception e) throws AuthenticationException {
              e.printStackTrace();
              logger.debug("WebResponseExceptionTranslator - " + e);
              throw new OAuth2LoginException(e.getMessage()); // exception renderer 에서 처리됨
            }
          })
          .approvalStoreDisabled();
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
      oauthServer
          .authenticationEntryPoint(oAuth2AuthenticationEntryPoint)
          .allowFormAuthenticationForClients();
    }

    @Bean
    public ApprovalStore approvalStore() throws Exception {
      TokenApprovalStore store = new TokenApprovalStore();
      store.setTokenStore(tokenStore());
      return store;
    }
  }
}
