/*
 * Filename	: AdminAuthenticationProvider.java
 * Function	:
 * Comment 	:
 * History	: 
 *
 * Version	: 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcUserMapping;
import com.jc.pico.bean.SvcUserMappingExample;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserExample;
import com.jc.pico.bean.UserGroup;
import com.jc.pico.bean.UserGroupExample;
import com.jc.pico.mapper.SvcUserMappingMapper;
import com.jc.pico.mapper.UserGroupMapper;
import com.jc.pico.mapper.UserMapper;


@Component
public class AdminAuthenticationProvider implements AuthenticationProvider {
	
	private Logger logger = LoggerFactory.getLogger(AdminAuthenticationProvider.class);
	private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Autowired UserMapper userMapper;
	@Autowired UserGroupMapper userGroupMapper;
	@Autowired
	SvcUserMappingMapper svcUserMappingMapper;
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {

	    HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	    
	    String username = authentication.getName();
	    String passsword = authentication.getCredentials().toString(); 
	    String client_id = request.getParameter("client_id");
	
	    List<GrantedAuthority> grantedAuths = new ArrayList<>();  
	    
    	logger.debug("★★★★★★★★★★★  AdminAuthenticationProvider ★★★★★★★★★★"); 
        logger.debug(" username : " + username);  
        logger.debug(" passsword : " + passsword);  
        logger.debug(" client_id : " + client_id);  
        logger.debug(" grantedAuths : " + grantedAuths);
        logger.debug("★★★★★★★★★★★★★★★★★★★★★");
        // ▶ 인증관리자 페이지 : installman / 1111  ---> username, client_id : agent
	    
	
	    if("pos".equals(client_id) || "mobile".equals(client_id) || "store".equals(client_id)  || "agent".equals(client_id)){
	
	      grantedAuths.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
	
	      return new UsernamePasswordAuthenticationToken(username, passsword, grantedAuths);
	      
	    } else {
	    	UserExample example = new UserExample();
	        
	        example.createCriteria().andUsernameEqualTo(username); 
	        List<User> user = userMapper.selectByExample(example);  
	        
	        if(user.size() == 1){
	        	if(!"301001".equals(user.get(0).getStatus())){
	        		throw new BadCredentialsException("사용중인 아이디가 아닙니다.");
	        	}
	        	
	        	if(passwordEncoder.matches(passsword, user.get(0).getPassword())){
	        		
	            	logger.debug("★★★★★★★★★★★  AdminAuthenticationProvider ★★★★★★★★★★");
	                logger.debug(" user.get(0).getUserRole() : " + user.get(0).getUserRole());   
	                logger.debug("★★★★★★★★★★★★★★★★★★★★★"); 
	                
	                if(user.get(0).getUserRole() != null && user.get(0).getUserRole() != ""){
	                	user.get(0).setUserRole("4");//기본설정 : store 
	                } 
	                
	        		if(user.get(0).getUserRole() != ""){
	        			
		        		UserGroupExample _example = new UserGroupExample();
		        		
		        		_example.createCriteria().andIdEqualTo(Long.valueOf(user.get(0).getUserRole()));
		            
		        		List<UserGroup> group = userGroupMapper.selectByExampleWithRowbounds(_example, new RowBounds(0, 1));  
		                
		        		if(group.size() == 1 ){
		        			grantedAuths.add(new SimpleGrantedAuthority(group.get(0).getGroupRole()));
		        		} else {
		        			throw new BadCredentialsException("권한이 올바르지 않습니다.");
		        		} 	        			
	        		}
	    	        
	        	} else {
	        		throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
	        	}
	        } else {
	        	throw new UsernameNotFoundException("아이디를 찾을수 없습니다."); // --> 어드민 페이지
	        } 
	        
			Map<String, Object> test = Maps.newHashMap();
			
			Map<Object, Object> userInfo = Maps.newHashMap();
			userInfo.put("name", user.get(0).getName());
			userInfo.put("userName", user.get(0).getUsername());
			userInfo.put("type", user.get(0).getType());
			userInfo.put("userRole", user.get(0).getUserRole());
			userInfo.put("userId", user.get(0).getId());
			
			Map<Object, Object> userMapping = Maps.newHashMap();
			
			SvcUserMapping svcUserMapping = new SvcUserMapping();

			SvcUserMappingExample svcUserMappingExample = new SvcUserMappingExample();

			svcUserMappingExample.createCriteria().andUserIdEqualTo(user.get(0).getId());
			
			List<SvcUserMapping> svcUserInfo = svcUserMappingMapper.selectByExample(svcUserMappingExample);
			
			if(!svcUserInfo.isEmpty()){
				userInfo.put("franId", svcUserInfo.get(0).getFranId());
				userInfo.put("brandId", svcUserInfo.get(0).getBrandId());
				userInfo.put("storeId", svcUserInfo.get(0).getStoreId());
			}
			
			logger.debug("★★★★★★★★★★  Login ★★★★★★★★");
			logger.debug(" UserInfo " + userInfo);
			logger.debug(" svcUserInfo " + svcUserInfo);
			logger.debug("★★★★★★★★★★★★★★★★★★");
			
			HttpSession session = request.getSession();
			session.setAttribute("userInfo", userInfo);
			
	        return new UsernamePasswordAuthenticationToken(username, passsword, grantedAuths);
	    }
	}

  @Override
  public boolean supports(Class<?> authentication) {
    // TODO Auto-generated method stub
    return true;
  }

}
