/*
 * Filename : CustomAuthenticationManager.java
 * Function :
 * Comment  :
 * History  : 
 *
 * Version  : 1.0
 * Author   : 
 */

package com.jc.pico.configuration;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderNotFoundException;
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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.jc.pico.bean.SvcBrand;
import com.jc.pico.bean.SvcBrandExample;
import com.jc.pico.bean.SvcDeviceLicense;
import com.jc.pico.bean.SvcDeviceLicenseExample;
import com.jc.pico.bean.SvcStaff;
import com.jc.pico.bean.SvcStaffExample;
import com.jc.pico.bean.User;
import com.jc.pico.bean.UserExample;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcDeviceLicenseMapper;
import com.jc.pico.mapper.SvcStaffMapper;
import com.jc.pico.mapper.UserMapper;
import com.jc.pico.service.pos.PosBaseService;
import com.jc.pico.service.store.StoreCommonService;
import com.jc.pico.utils.AES256Cipher;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.CustomMapper;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.ClerkResult;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StaffUserDetail;
import com.jc.pico.utils.bean.StoreResult;

@Component("authenticationManager")
public class CustomAuthenticationManager implements AuthenticationManager {

  private Logger logger = LoggerFactory.getLogger(CustomAuthenticationManager.class);
  
  @Autowired
  CustomMapper customMapper;

  @Autowired
  UserMapper mapper;

  @Autowired
  CodeUtil codeUtil;

  @Autowired
  private SvcDeviceLicenseMapper svcDeviceLicenseMapper;

  @Autowired
  private SvcBrandMapper svcBrandMapper;

  @Autowired
  private SvcStaffMapper svcStaffMapper;

  @Autowired
  private StoreCommonService storeCommonService;
  
  @Autowired
  private PosBaseService posBaseService;

  private BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    try {
      String userType = "";
      String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");
  
      HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
  
      String username = AES256Cipher.decodeAES256(authentication.getName());
      String passsword = AES256Cipher.decodeAES256(authentication.getCredentials().toString());
      String clientId = request.getParameter("client_id");
      String deviceId = request.getParameter("deviceId");
  
      User user = null;
      List<GrantedAuthority> grantedAuths = Lists.newArrayList();
      grantedAuths.add(new SimpleGrantedAuthority("ROLE_USER"));
      if ("mobile".equals(clientId)) {
        userType = codeUtil.getBaseCodeByAlias("user");
        
      } else if ("pos".equals(clientId)) {
        return handelPos(request, authentication, grantedAuths);
        
      } else if ("agent".equals(clientId)) {        
        return handleAgent(request, authentication, grantedAuths);
        
      } else if ("store".equals(clientId)) {  
        return handleStoreManager(request, authentication, grantedAuths);
  
      } else if ("staff".equals(clientId)) {
        return handleStaff(request, authentication, grantedAuths);
        
      } else {
        throw new ProviderNotFoundException("client_id(" + clientId + ") not support.");
      }
  
      logger.info("username : {}, clientId : {}, deviceId: {}", username, clientId, deviceId);
  
      UserExample example = new UserExample();
      UserExample.Criteria criteria = example.createCriteria();
      criteria.andTypeEqualTo(userType);
      criteria.andStatusEqualTo(statusNormal);
      criteria.andUsernameEqualTo(username);
  
      List<User> users = mapper.selectByExample(example);
      if (users != null && users.size() > 0) {
        user = users.get(0);
        if ("mobile".equals(clientId)) {
          Map<String, Object> data = Maps.newHashMap();
          data.put("error", Globals.ERROR_LOGIN_FAIL);
          data.put("clientId", clientId);
          if (user == null) {
            throw new UsernameNotFoundException(JsonConvert.toJson(data));
          }
          user = users.get(0);
          Map<String, Object> checkParam = Maps.newHashMap();
          byte loginFailCnt = user.getLoginFailCnt();
  
          // wrong password          
          logger.debug("login check 1 : " + passsword);
          logger.debug("login check 2 : " +user.getPassword());
          logger.debug("login check 3 : " +bcryptEncoder.matches(passsword,  user.getPassword()));
          if (!bcryptEncoder.matches(passsword, user.getPassword())) {
            checkParam.put("loginFailCnt", loginFailCnt + 1);
            checkParam.put("userId", user.getId());
            customMapper.updateLoginFailCount(checkParam);
            data.put("user", user);
            throw new UsernameNotFoundException(JsonConvert.toJson(data));
          }
  
          // user status
          if (!statusNormal.equals(user.getStatus())) {
            checkParam.put("loginFailCnt", loginFailCnt + 1);
            checkParam.put("userId", user.getId());
            customMapper.updateLoginFailCount(checkParam);
            data.put("user", user);
            throw new UsernameNotFoundException(JsonConvert.toJson(JsonConvert.toJson(data)));
          }
  
        } else {
          // wrong password
          if (!bcryptEncoder.matches(passsword, user.getPassword())) {
            throw new UsernameNotFoundException("check PASS fail !!");
          }
  
          // user status
          if (!statusNormal.equals(user.getStatus())) {
            throw new UsernameNotFoundException("check STATUS fail !!");
          }
        }
  
      } else {
        // not found id
        if (user == null) {
          throw new UsernameNotFoundException("check ID fail !!");
        }
      }
  
  //    if (user.getStoreId() == null) {
  //      throw new UsernameNotFoundException("Chack Store ID fail.");
  //    }
  
      if ("mobile".equals(clientId)) { // mobile의 경우 사용자별 token 발행
        user.setLastLogin(new Date());
        user.setPassword(null);
        mapper.updateByPrimaryKeySelective(user);
        return new UsernamePasswordAuthenticationToken(username, passsword, grantedAuths);
      } else { // pos의 경우 deviceId(포스 식별값) 별로 token 발행(ID/PW는 모든 포스가 동일하게 사용, 개별 사용 상관 없음.)
        return new UsernamePasswordAuthenticationToken(deviceId + "_-_" + username, passsword, grantedAuths);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new UsernameNotFoundException(e.getMessage(), e);
    }
  }

  /**
   * Staff 사용자를 인증을 위해 다음을 수행한다.
   * - device license 상태 확인
   * - staff 상태 확인
   * - username / password 확인.
   * 
   * @param request
   * @param authentication
   * @param grantedAuths
   * @return
   */
	private Authentication handleStaff(HttpServletRequest request, Authentication authentication, List<GrantedAuthority> grantedAuths)
			throws Exception {
		final String staffNormalStatus = codeUtil.getBaseCodeByAlias("staff-st-normal");
		final String licenseUseStatus = codeUtil.getBaseCodeByAlias("poslicense-status-use");
		final Date nowDate = new Date();

		final String username = AES256Cipher.decodeAES256(authentication.getName());
		final String password = AES256Cipher.decodeAES256(authentication.getCredentials().toString());
		final String hwInfo = request.getParameter("licenseHwInfo");
		final String licenseKey = request.getParameter("licenseKey");

		// license 상태 확인
		SvcDeviceLicenseExample licenseExample = new SvcDeviceLicenseExample();
		licenseExample.createCriteria() // 조건       
				.andHwInfoEqualTo(hwInfo) // 마지막에 등록한 디바이스만 유효함
				.andLicenseKeyEqualTo(licenseKey) // 라이센스 코드
				.andBeginLessThanOrEqualTo(nowDate) // 유효 기간 확인 (시작일 <= 현재 <= 만료일) 
				.andEndGreaterThanOrEqualTo(nowDate) // 유효 기간 확인
				.andStatusEqualTo(licenseUseStatus); // 라이센스 사용중
		List<SvcDeviceLicense> licenseList = svcDeviceLicenseMapper.selectByExample(licenseExample);
		if (licenseList.isEmpty()) {
			throw new UsernameNotFoundException("Invalid Device license");
		}

		// 현재 디바이스 정보
		final SvcDeviceLicense license = licenseList.get(0);

		// staff 존재 여부 및 상태 확인    
		// username은 매장 단위로 유니크 함.
		SvcStaffExample staffExample = new SvcStaffExample();
		staffExample.createCriteria() // 조건
				.andUsernameEqualTo(username) // 사용자 이름 (id)
				.andStatusEqualTo(staffNormalStatus) // 정상 상태
				.andStoreIdEqualTo(license.getStoreId()); // 라이센스와 로그인 staff가 동일 매장 
		List<SvcStaff> staffList = svcStaffMapper.selectByExample(staffExample);
		if (staffList.isEmpty()) {
			throw new UsernameNotFoundException("Invalid staff user.");
		}

		// 현재 staff 정보
		final SvcStaff staff = staffList.get(0);

		// 패스워드를 5회 이상 틀리면 로그인 차단
		final int loginFailCnt = staff.getLoginFailCnt();
		final int loginFailLimit = 5;
		if (loginFailCnt >= loginFailLimit) {

			// 실패 카운트 증가
			SvcStaff record = new SvcStaff();
			record.setId(staff.getId());
			record.setLoginFailCnt(loginFailCnt + 1);
			record.setLoginTryTm(nowDate);
			svcStaffMapper.updateByPrimaryKeySelective(record);

			SingleMap error = new SingleMap();
			error.put("error", Globals.ERROR_USER_LOGIN_LIMIT_EXCEEDED);
			error.put("message", "Login limit exceeded. limit=" + loginFailLimit + "," + "failCnt=" + loginFailCnt);
			throw new UsernameNotFoundException(JsonConvert.toJson(error));
		}

		// username / password 확인
		if (!bcryptEncoder.matches(password, staff.getPassword())) {

			// 실패 카운트 증가
			SvcStaff record = new SvcStaff();
			record.setId(staff.getId());
			record.setLoginFailCnt(loginFailCnt + 1);
			record.setLoginTryTm(nowDate);
			svcStaffMapper.updateByPrimaryKeySelective(record);

			throw new UsernameNotFoundException("Invalid staff user.");
		}

		// 인증 통과
		
		
		// 로그인에 성공하면 실패 카운트 초기화, 로그인 시간 기록
		SvcStaff record = new SvcStaff();
		record.setId(staff.getId());
		record.setLoginFailCnt(0);
		record.setLoginTryTm(nowDate);
		record.setLoginLastTm(nowDate);
		svcStaffMapper.updateByPrimaryKeySelective(record);		
		
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_STORE_STAFF"));

		StaffUserDetail staffUserDetail = new StaffUserDetail(username, password, grantedAuths);
		staffUserDetail.setBrandId(staff.getBrandId());
		staffUserDetail.setStoreId(staff.getStoreId());
		staffUserDetail.setStaffId(staff.getId());
		staffUserDetail.setLicenseId(license.getId());

		// 직원의 user name은 매장 단위에서 유니크, 디바이스 별로 다른 인증 토큰 사용 하도록 하기 위해 
		// username에 조합하여 설정 함 oauth_access_token 테이블의 user_name 에 저장됨.
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
				staff.getUsername() // staff user name
						+ PosUtil.BASE_INFO_DELIMITER + staff.getStoreId() // staff store 
						+ PosUtil.BASE_INFO_DELIMITER + license.getId() // license id
				, password, grantedAuths);
		token.setDetails(staffUserDetail);

		// Note. 여기서 생성한 UsernamePasswordAuthenticationToken은 OAuth2Authentication으로 랩핑되며
		// OAuth2Authentication.getUserAuthentication() 하여 접근 가능.
		return token;
	}

  /**
   * 포스 로그인 처리
   * 
   * @param request
   * @param authentication
   * @param grantedAuths
   * @return
   */
  private Authentication handelPos(HttpServletRequest request, Authentication authentication, List<GrantedAuthority> grantedAuths) throws Exception {

    final String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");
    final String licenseUseStatus = codeUtil.getBaseCodeByAlias("poslicense-status-use");
    final Date nowDate = new Date();

    final String userType = codeUtil.getBaseCodeByAlias("store-user");
    final String username = AES256Cipher.decodeAES256(authentication.getName());
    final String password = AES256Cipher.decodeAES256(authentication.getCredentials().toString());
    final String deviceId = request.getParameter("deviceId"); // licenseKey
    final String hwInfo = request.getParameter("licenseHwInfo");

    if (StringUtils.isEmpty(hwInfo)) {
      throw new UsernameNotFoundException("licenseHwInfo is empty.");
    }

    // ID / Password 확인
    UserExample example = new UserExample();
    example.createCriteria() // 조건
        .andTypeEqualTo(userType) // 포스 타입 계정
        .andStatusEqualTo(statusNormal) // 계정 상태 정상
        .andUsernameEqualTo(username); // 사용자 이름 (username)
    List<User> users = mapper.selectByExample(example);
    if (users.isEmpty()) {
      throw new UsernameNotFoundException("check ID fail !!");
    }

    // 사용자 존재
    final User user = users.get(0);

    // wrong password
    if (!bcryptEncoder.matches(password, user.getPassword())) {
      throw new UsernameNotFoundException("check PASS fail !!");
    }

    // license 상태 확인
    SvcDeviceLicenseExample licenseExample = new SvcDeviceLicenseExample();
    licenseExample.createCriteria() // 조건       
        .andHwInfoEqualTo(hwInfo) // 마지막에 등록한 디바이스만 유효함
        .andLicenseKeyEqualTo(deviceId) // 라이센스 코드
        .andBeginLessThanOrEqualTo(nowDate) // 유효 기간 확인 (시작일 <= 현재 <= 만료일) 
        .andEndGreaterThanOrEqualTo(nowDate) // 유효 기간 확인
        .andStatusEqualTo(licenseUseStatus); // 라이센스 사용중
    List<SvcDeviceLicense> licenseList = svcDeviceLicenseMapper.selectByExample(licenseExample);

    // 라이센스 만료 혹은 다른 단말에서 재인증 받은
    if (licenseList.isEmpty()) {
      throw new UsernameNotFoundException("Invalid Device license.");
    }

    // 현재 디바이스 정보
    final SvcDeviceLicense svcDeviceLicense = licenseList.get(0);

    Long storeId = svcDeviceLicense.getStoreId();
    Long brandId = svcDeviceLicense.getBrandId();
    String posNo = svcDeviceLicense.getPosNo();
    Long tenantId = svcDeviceLicense.getTenantId();
    SvcBrandExample svcBrandExample = new SvcBrandExample();
    SvcBrandExample.Criteria svcBrandExampleCriteria = svcBrandExample.createCriteria();
    svcBrandExampleCriteria.andIdEqualTo(brandId);
    List<SvcBrand> svcBrands = svcBrandMapper.selectByExample(svcBrandExample);
    if (svcBrands == null || svcBrands.size() != 1) {
      String msg = "[" + PosUtil.EPO_0002_CODE + "][" + PosUtil.EPO_0002_MSG + "] There is NO SvcBrand. brandId=" + brandId;
      logger.error(msg);
      //throw new BadCredentialsException(msg);
      throw new UsernameNotFoundException(msg);
    }
    SvcBrand svcBrand = svcBrands.get(0); // 인증통과되었으므로 브랜드 첫번째 Row 를 세팅
    Long companyId = svcBrand.getFranId();

    // 인증 통과
    grantedAuths.add(new SimpleGrantedAuthority("ROLE_POS"));
    
    // 포스가 설정했던 락을 모두 해제
    posBaseService.releaseTableLock(storeId, posNo);

    // username 정보에 "_-_" delimiter 로 매장정보와 회사정보 담기
    return new UsernamePasswordAuthenticationToken(
        deviceId // 라이센스 번호
            + PosUtil.BASE_INFO_DELIMITER + storeId // 스토어
            + PosUtil.BASE_INFO_DELIMITER + brandId // 브랜드
            + PosUtil.BASE_INFO_DELIMITER + companyId // 프랜차이즈
            + PosUtil.BASE_INFO_DELIMITER + posNo // 포스 번호
            + PosUtil.BASE_INFO_DELIMITER + tenantId // 서비스
        , password, grantedAuths);
    // E: POS 연동부에서 인증통과하면 매장/POS 정보를 담기 위해...
  }

  /**
   * 스토어 매니저 로그인 처리
   * 로그인 및 연결된 디바이스 정보를 갱신 한다.
   * 
   * @param request
   * @param authentication
   * @param grantedAuths
   * @return
   */
  private Authentication handleStoreManager(HttpServletRequest request, Authentication authentication, List<GrantedAuthority> grantedAuths) throws Exception {

    final String userType = codeUtil.getBaseCodeByAlias("store-manager");
    final String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");
    final Date nowDate = new Date();

    final String username = AES256Cipher.decodeAES256(authentication.getName());
    final String passsword = AES256Cipher.decodeAES256(authentication.getCredentials().toString());
    String deviceId = request.getParameter("deviceId");
    String osType = request.getParameter("osType");

    UserExample example = new UserExample();
    UserExample.Criteria criteria = example.createCriteria();
    criteria.andTypeEqualTo(userType);
    criteria.andStatusEqualTo(statusNormal);
    criteria.andUsernameEqualTo(username);

    List<User> users = mapper.selectByExample(example);
    if (users.isEmpty()) {
      throw new UsernameNotFoundException("Not found user.");
    }
    User user = users.get(0);

    // 패스워드를 5회 이상 틀리면 로그인 차단  
    final int loginFailCnt = user.getLoginFailCnt();
    final int loginFailLimit = 5;
    if(loginFailCnt >= loginFailLimit) {
    	
    	// 실패 카운트 증가
        SingleMap param = new SingleMap();
        param.put("loginFailCnt", loginFailCnt + 1);
        param.put("userId", user.getId());        
        customMapper.updateLoginFailCount(param);
        
    	SingleMap error = new SingleMap();    	   			
    	error.put("error", StoreResult.ErrorCode.ERROR_LOGIN_LIMIT_EXCEEDED.code);
    	error.put("message", "Login limit exceeded. limit=" + loginFailLimit + "," + "failCnt=" + loginFailCnt);
    	error.put("osType", osType);
    	throw new UsernameNotFoundException(JsonConvert.toJson(error));
    }
    
    // wrong password
    if (!bcryptEncoder.matches(passsword, user.getPassword())) {
    	
      // 실패 카운트 증가
      SingleMap param = new SingleMap();
      param.put("loginFailCnt", loginFailCnt + 1);
      param.put("userId", user.getId());
      customMapper.updateLoginFailCount(param);
      
      throw new UsernameNotFoundException("Incorrect password.");
    }

    // user status
    if (!statusNormal.equals(user.getStatus())) {      
      throw new UsernameNotFoundException("Invalid user status. current=" + user.getStatus());
    }

    // 해당 디바이스와 연결된 사용자를 변경 한다. 
    if (!StringUtils.isEmpty(deviceId)) {
      storeCommonService.updateUserDeviceOwner(deviceId, user.getId());
    }
    
    // 로그인에 성공하면 실패 카운트 초기화
    User record = new User();
    record.setId(user.getId());
	record.setLoginFailCnt((byte) 0);
	record.setLastLogin(nowDate);
	record.setLoginTryTm(nowDate);
	mapper.updateByPrimaryKeySelective(record);

    // success login    
    grantedAuths.add(new SimpleGrantedAuthority("ROLE_STORE_MANAGER"));
    return new UsernamePasswordAuthenticationToken(username, passsword, grantedAuths);
  }
  
  /**
   * Agent 로그인 처리
   * 
   * @param request
   * @param authentication
   * @param grantedAuths
   * @return
   */
	private Authentication handleAgent(HttpServletRequest request, Authentication authentication, List<GrantedAuthority> grantedAuths)
			throws Exception {

		final String userType = codeUtil.getBaseCodeByAlias("agent-user");
		final String statusNormal = codeUtil.getBaseCodeByAlias("user-status-normal");
		final Date nowDate = new Date();

		final String username = AES256Cipher.decodeAES256(authentication.getName());
		final String passsword = AES256Cipher.decodeAES256(authentication.getCredentials().toString());		

		UserExample example = new UserExample();
		UserExample.Criteria criteria = example.createCriteria();
		criteria.andTypeEqualTo(userType);
		criteria.andStatusEqualTo(statusNormal);
		criteria.andUsernameEqualTo(username);

		List<User> users = mapper.selectByExample(example);
		if (users.isEmpty()) {
			throw new UsernameNotFoundException("Not found user.");
		}
		User user = users.get(0);

		// 패스워드를 5회 이상 틀리면 로그인 차단  
		final int loginFailCnt = user.getLoginFailCnt();
		final int loginFailLimit = 5;
		if (loginFailCnt >= loginFailLimit) {

			// 실패 카운트 증가
			SingleMap param = new SingleMap();
			param.put("loginFailCnt", loginFailCnt + 1);
			param.put("userId", user.getId());
			customMapper.updateLoginFailCount(param);

			SingleMap error = new SingleMap();
			error.put("error", Globals.ERROR_USER_LOGIN_LIMIT_EXCEEDED);
			error.put("message", "Login limit exceeded. limit=" + loginFailLimit + "," + "failCnt=" + loginFailCnt);
			throw new UsernameNotFoundException(JsonConvert.toJson(error));
		}

		// wrong password
		if (!bcryptEncoder.matches(passsword, user.getPassword())) {

			// 실패 카운트 증가
			SingleMap param = new SingleMap();
			param.put("loginFailCnt", loginFailCnt + 1);
			param.put("userId", user.getId());
			customMapper.updateLoginFailCount(param);

			throw new UsernameNotFoundException("Incorrect password.");
		}

		// 로그인에 성공하면 실패 카운트 초기화, 로그인 성공 시간 기록
		User record = new User();
		record.setId(user.getId());
		record.setLoginFailCnt((byte) 0);
		record.setLastLogin(nowDate);
		record.setLoginTryTm(nowDate);
		mapper.updateByPrimaryKeySelective(record);

		// success login    
		grantedAuths.add(new SimpleGrantedAuthority("ROLE_AGENT"));
		return new UsernamePasswordAuthenticationToken(username + PosUtil.BASE_INFO_DELIMITER + user.getId(), passsword, grantedAuths);
	}

}
