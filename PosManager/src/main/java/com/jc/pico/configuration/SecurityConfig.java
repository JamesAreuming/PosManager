/*
 * Filename	: SecurityConfig.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@ComponentScan(basePackages = "com.jc.pico.configuration")
public class SecurityConfig {
  
  /*
   *@EnableWebSecurity와 WebSecurityConfigurerAdapter은 웹 기반 보안을 위해 함께 작동
   * WebSecurityConfigurerAdapter을 상속받을 시 다음과 같은 것등을 할 수 있음
   *  - 해당 애플리케이션에 어떤 URL로 들어오던간에 사용자에게 인증 요구
   *  - 사용자 이름과 비밀번호 및 롤 기반으로 사용자 생성
   *  - HTTP Basic 과 폼 기반 인증 가능
   *  - Spring Security는 자동적으로 로그인 페이지, 인증실패 url을 제공 
   * */
  @Configuration
  @EnableWebSecurity
  @Order(1) // 같은 타입의 우선순위 (낮을수록 높음), Spring 4이상에서 쓰임
  public static class CustomerFormLoginWebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    MobileAuthenticationProvider authenticationProvider;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth
          .authenticationProvider(authenticationProvider);
    }

    @Override
    public void configure(WebSecurity web) throws Exception { //인증 무시, 항상 통과
      web
          .ignoring()
          .antMatchers("/app/public/**") // 가입 처리용 API 및 권한 불필요 API
          .antMatchers("/email/**") // email을 이용한 인증 처리
          .antMatchers("/app-resource/**") // app resource
          .antMatchers("/image-resource/**") // image resource
          .antMatchers("/bin-resource/**") // binary resource
          .antMatchers("/app/model/terms/**") // 이용약관 조회는 권한 불필요
          .antMatchers("/app/model/notice/**") // 공지사항 조회는 권한 불필요
          .antMatchers("/mobile/_static/**")
          .antMatchers("/api/solbi/**") // Demo 용
          .antMatchers("**/favicon.ico")
          .antMatchers("/store/api/appinfo") // Store API 중 권한 불필요한 항목
      	  .antMatchers("/clerk/api/appinfo") // Clerk API 중 권한 불필요한 항목
      	  .antMatchers("/order9/**") // 테스트중
      	  .antMatchers("/api/pos/store/E_APP_INFO"); // Pos API 중 권한 불필요한 항목      
    }

    protected void configure(HttpSecurity http) throws Exception { //페이지 권한 설정
      http
          //.antMatcher("/**")
          .regexMatcher("^((?!\\/app\\/public)(?!\\/app\\/model)(?!\\/api)(?!\\/admin)(?!\\/store)(?!\\/clerk))\\/.*")
          .authorizeRequests()
          .antMatchers("/admin/**").permitAll()
          .anyRequest().authenticated()
          .and()
          .formLogin()
          .loginPage("/admin/login")
          .failureUrl("/admin/login") // 의미 없는 url로 인한 핸들링
          .and()
          .csrf().disable()
          .httpBasic();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean()
        throws Exception {
      return super.authenticationManagerBean();
    }
  }

  @Configuration
  @EnableWebSecurity
  @Order(2)
  public static class AdminFormLoginWebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    AdminAuthenticationProvider authenticationProvider;

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
      auth
          .authenticationProvider(authenticationProvider);
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
      return new AuthSuccessHandler("/admin/");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
      web
          .ignoring()
          .antMatchers("/admin/_static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
      http
          .antMatcher("/admin/**") //  /admin/aaaa 등 매칭
          .regexMatcher("^((?!\\/api)(?!\\/app)(?!\\/model)(?!\\/store)(?!\\/clerk))\\/.*") // /admin/model 등 매칭
          .authorizeRequests()    // 인증 요구
          .antMatchers("/admin/**")
          .access("hasRole('BRAND') or hasRole('ADMIN') or hasRole('STORE') or hasRole('OWNER')")
          .anyRequest().authenticated() // 위의 롤의 권한을 가진 사람일 때 인증이 성공해야 접근 가능
          .and()
          .formLogin()
          .usernameParameter("username")
          .passwordParameter("password")
          .loginPage("/admin/login")
          .permitAll()
          .loginProcessingUrl("/admin/login/auth")
          .failureUrl("/admin/login")
          .successHandler(successHandler())
          .and()
          .logout()
          .logoutUrl("/admin/logout")
          .logoutSuccessUrl("/admin/login")
          .and()
          .exceptionHandling()
          .accessDeniedPage("/admin/login")
          .and()
          .csrf().disable()
          .httpBasic();
    }


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
    }
  }

}
