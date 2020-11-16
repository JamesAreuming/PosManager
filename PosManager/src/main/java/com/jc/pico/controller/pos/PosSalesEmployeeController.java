package com.jc.pico.controller.pos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.bean.SvcStaff;
import com.jc.pico.bean.SvcStaffAttend;
import com.jc.pico.bean.SvcStaffAttendExample;
import com.jc.pico.bean.SvcStaffExample;
import com.jc.pico.mapper.SvcStaffAttendMapper;
import com.jc.pico.mapper.SvcStaffMapper;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosResult;
import com.jc.pico.utils.customMapper.pos.PosSalesEmployeeAttendMapper;

/**
 * POS 연동부 중 주문정보 조회 컨트롤러
 * @author green
 *
 */
@RestController
@RequestMapping(value="/api/pos/store")
public class PosSalesEmployeeController {
    Logger logger = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
	private SvcStaffMapper svcStaffMapper;    

    @Autowired
	private SvcStaffAttendMapper svcStaffAttendMapper;
    
    @Autowired
	private PosSalesEmployeeAttendMapper posSalesEmployeeAttendMapper;
    
    
	@Autowired
	private PosUtil posUtil;
	
	/**
	 * POS 연동 (1. S_EMPLOYEE_INFO : 사원 정보)
	 * @param jsonString "PARAM": 통합 파라미터
	 * <ol style="line-height: 1.5em;">
	 * <li><b style="color:#FF0F65;">DS_MODE</b> String <b>처리구분</b></li>
	 * <li><b style="color:#FF0F65;">CD_EMPLOYEE</b> String <b>사원코드</b></li>
	 * <li><b style="color:#FF0F65;">NM_EMPLOYEE</b> String <b>사원명</b></li>
	 * <li><b style="color:#FF0F65;">USER_ID</b> String <b>사용자ID</b></li>
	 * <li><b style="color:#FF0F65;">USER_PW</b> String <b>사용자암호</b></li>
	 * <li><b style="color:#FF0F65;">CD_GRADE</b> String <b>사용자등급</b></li>
	 * <li><b style="color:#FF0F65;">YN_USE</b> String <b>사용유무</b></li>
	 * </ol>
	 * @Example {"DS_MODE": "5","CD_EMPLOYEE": "23","NM_EMPLOYEE": "5","USER_ID": "23","USER_PW": "5","CD_GRADE": "23","YN_USE": "23" }
	 * @return {@link PosResult} 
	 */
	@RequestMapping(value="/M_EMPLOYEE_SAVE", method = {RequestMethod.GET, RequestMethod.POST})
	public PosResult sEmployeeInfo(@RequestParam(value="PARAM") String jsonString ) {
		
		logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
		logger.debug(jsonString);
		logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
		
		PosResult posResult = new PosResult();
		Map<String, Object> jsonObject = null;
		ObjectMapper mapper = new ObjectMapper();
		Integer dsMode = null;		// 처리 구분 (1:신규입력, 2:수정, 3:삭제)
		SvcStaff svcStaff = new SvcStaff();
		
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			
			dsMode = (Integer) jsonObject.get("DS_MODE");		// 처리 구분 (1:신규입력, 2:수정, 3:삭제)
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long brandId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_BRAND_ID) : 0L);
			
			if(dsMode == null){
				posResult.setSuccess(false);
				posResult.setResultMsg(SvcStaff.class.getName() + " is NULL");
			}else{
				Boolean resultValue = false;				
				
				svcStaff.setBrandId(brandId);
				svcStaff.setStoreId(storeId);				
				
				// (String) jsonObject.get("YN_USE") 사용유무(1:사용,0:미사용)
				
				if(dsMode == 1){
					svcStaff.setName((String) jsonObject.get("NM_EMPLOYEE"));		// 사원명
					svcStaff.setUsername((String) jsonObject.get("USER_ID"));		// 사용자 ID
					svcStaff.setPassword((String) jsonObject.get("USER_PW"));		// 사용자암호
					svcStaff.setMb((String) jsonObject.get("TEL_MOBILE"));			// 사용자휴대전화번호
					
					SvcStaffExample svcStaffExample = new SvcStaffExample();
					SvcStaffExample.Criteria svcStaffExampleCriteria = svcStaffExample.createCriteria();
					svcStaffExampleCriteria.andBrandIdEqualTo(svcStaff.getBrandId());
					svcStaffExampleCriteria.andStoreIdEqualTo(svcStaff.getStoreId());
					svcStaffExampleCriteria.andUsernameEqualTo(svcStaff.getUsername());
					
					int ynUse = (int) jsonObject.get("YN_USE"); // 사용유무(1:사용,0:미사용)
					
					if(ynUse == 0){
						svcStaff.setStatus("305003");
					}else if(ynUse == 1){
						svcStaff.setStatus("305001");
					}
					
					svcStaff.setStaffTp("304001");
					
					if(svcStaffMapper.countByExample(svcStaffExample) == 0){
						resultValue = svcStaffMapper.insertSelective(svcStaff) == 1 ? true : false;
					}else{
						posResult.setSuccess(false);
						posResult.setResultMsg(SvcStaff.class.getName() + " 사용중인 아이디 입니다.");
						return posResult;
					}
				}else if(dsMode == 2 ){
					svcStaff.setId(Long.valueOf((String) jsonObject.get("CD_EMPLOYEE")));
					svcStaff.setName((String) jsonObject.get("NM_EMPLOYEE"));		// 사원명
					svcStaff.setMb((String) jsonObject.get("TEL_MOBILE"));			// 사용자휴대전화번호
					
					int ynUse = (int) jsonObject.get("YN_USE"); // 사용유무(1:사용,0:미사용)
					
					if(ynUse == 0){
						svcStaff.setStatus("305003");
					}else if(ynUse == 1){
						svcStaff.setStatus("305001");
					}
					
					resultValue = svcStaffMapper.updateByPrimaryKeySelective(svcStaff) == 1 ? true : false;
					
				}else if(dsMode == 3){
					resultValue = svcStaffMapper.deleteByPrimaryKey(Long.valueOf((String) jsonObject.get("CD_EMPLOYEE"))) == 1 ? true : false;
				}else if(dsMode == 4){
					svcStaff.setId(Long.valueOf((String) jsonObject.get("CD_EMPLOYEE")));
					svcStaff.setPassword((String) jsonObject.get("USER_PW"));		// 사용자암호
					
					resultValue = svcStaffMapper.updateByPrimaryKeySelective(svcStaff) == 1 ? true : false;
				}
				
				if(resultValue == true){
					posResult.setSuccess(resultValue);
					posResult.setResultMsg("Success");
				}else{
					posResult.setSuccess(resultValue);
					posResult.setResultMsg(SvcStaff.class.getName() + " employee info is NULL");
				}
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
		}
		return posResult;
	}

	/**
	 * POS 연동 (2. S_EMPLOYEE_ATTEND : 근태 정보)
	 * @param jsonString "PARAM": 통합 파라미터
	 * <ol style="line-height: 1.5em;">
	 * <li><b style="color:#FF0F65;">DS_MODE</b> String <b>처리구분</b></li>
	 * <li><b style="color:#FF0F65;">CD_EMPLOYEE</b> String <b>사원코드</b></li>
	 * <li><b style="color:#FF0F65;">DT_ATTEND</b> String <b>출퇴근시간</b></li>
	 * </ol>
	 * @Example {"DS_MODE": "5","CD_EMPLOYEE": "23","DT_ATTEND": "5"}
	 * @return {@link PosResult} 
	 */
	@RequestMapping(value="/S_EMPLOYEE_ATTEND", method = {RequestMethod.GET, RequestMethod.POST})
	public Map<String, Object> sEmployeeAttend(@RequestParam(value="PARAM") String jsonString ) {
		
		logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
		logger.debug(jsonString);
		logger.debug("☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆☆");
		
		Map<String, Object> posResult = new HashMap<String, Object>();
		
		Map<String, Object> jsonObject = null;
		ObjectMapper mapper = new ObjectMapper();
		Integer dsMode  = null;		// 처리 구분 (1:신규입력, 2:수정, 3:삭제)
		SvcStaffAttend svcStaffAttend = new SvcStaffAttend();
		
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			
			dsMode = (Integer) jsonObject.get("DS_MODE");		// 처리 구분 (1:출근, 2:퇴근)	
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long brandId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_BRAND_ID) : 0L);
			
			if(dsMode == null){
				posResult.put("SUCCESS", false);
				posResult.put("RESULT_MSG", SvcStaffAttend.class.getName() + " is NULL");
			}else{
				Boolean resultValue = false;
				
				svcStaffAttend.setBrandId(brandId);
				svcStaffAttend.setStoreId(storeId);
				
				svcStaffAttend.setAttendTp( String.valueOf((Integer) jsonObject.get("DS_MODE")));		// 처리 구분 (1:출근, 2:퇴근)
				svcStaffAttend.setStaffId(Long.valueOf((String)jsonObject.get("CD_EMPLOYEE")));	// 사원코드
				svcStaffAttend.setWhen(posUtil.parseDateTime((String) jsonObject.get("DT_ATTEND"),null));			// 출/퇴근 시간				
				
				Map<String, Object> strParam = new HashMap<>();
				strParam.put("brandId", brandId);
				strParam.put("storeId", storeId);
				strParam.put("attendTp", String.valueOf((Integer) jsonObject.get("DS_MODE")));
				strParam.put("staffId", Long.valueOf((String)jsonObject.get("CD_EMPLOYEE")));
				
				String strWhen = posUtil.formatDate(posUtil.parseDateTime((String) jsonObject.get("DT_ATTEND"),null));
				strParam.put("when", strWhen);
				
				if(posSalesEmployeeAttendMapper.selectCountDefault(strParam) == 0){
					resultValue = svcStaffAttendMapper.insertSelective(svcStaffAttend) == 1 ? true : false;
				}else{
					posResult.put("SUCCESS", false);
					posResult.put("RESULT_MSG", SvcStaff.class.getName() + " is DuplicateKey");
					posResult.put("CD_COMPANY", jsonObject.get("CD_COMPANY"));
					posResult.put("CD_STORE", jsonObject.get("CD_STORE"));
					posResult.put("YMD_SALE", jsonObject.get("YMD_SALE"));
					posResult.put("NO_POS", jsonObject.get("NO_POS"));
					posResult.put("SEQ", jsonObject.get("SEQ"));
					return posResult;
				}					
				
				if(resultValue == true){
					posResult.put("SUCCESS", true);
					posResult.put("RESULT_MSG","Success");
					posResult.put("CD_COMPANY", jsonObject.get("CD_COMPANY"));
					posResult.put("CD_STORE", jsonObject.get("CD_STORE"));
					posResult.put("YMD_SALE", jsonObject.get("YMD_SALE"));
					posResult.put("NO_POS", jsonObject.get("NO_POS"));
					posResult.put("SEQ", jsonObject.get("SEQ"));
				}else{
					posResult.put("SUCCESS", false);
					posResult.put("RESULT_MSG",(SvcStaffAttend.class.getName() + " is NULL"));
					posResult.put("CD_COMPANY", jsonObject.get("CD_COMPANY"));
					posResult.put("CD_STORE", jsonObject.get("CD_STORE"));
					posResult.put("YMD_SALE", jsonObject.get("YMD_SALE"));
					posResult.put("NO_POS", jsonObject.get("NO_POS"));
					posResult.put("SEQ", jsonObject.get("SEQ"));
				}
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
		}
		return posResult;
	}
}

