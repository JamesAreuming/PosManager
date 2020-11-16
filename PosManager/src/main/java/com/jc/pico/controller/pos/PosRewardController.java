package com.jc.pico.controller.pos;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.service.pos.PosBaseService;
import com.jc.pico.service.pos.PosRewardService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosStampSave;
import com.jc.pico.utils.bean.SingleMap;

/**
 * POS 연동부 중 쿠폰/스탬프 컨트롤러
 * 
 * @author hyo
 *
 */
@RestController
@RequestMapping(value = "/api/pos/store")
public class PosRewardController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PosRewardService posRewardService;

	@Autowired
	private PosBaseService posBaseService;

	@Autowired
	private PosUtil posUtil;

	private final ObjectMapper objectMapper = new ObjectMapper();

	/**
	 * 스탬프 적립 (임의 적립)
	 * 
	 * @param jsonParam
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/S_STAMP_SAVE", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sStampSave(@RequestParam(value = "PARAM") String jsonParam, Authentication authentication) {

		logger.debug("sStampSave: " + jsonParam);

		// 결과값
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...

		try {

			PosStampSave param = objectMapper.readValue(jsonParam, PosStampSave.class);

			// 응답시 아래 정보를 항상 포함. 포스측에서 응답 구분에 사용
			result.put("CD_COMPANY", param.getCdCompany());
			result.put("CD_STORE", param.getCdStore());
			result.put("YMD_SALE", param.getYmdSale());
			result.put("NO_POS", param.getNoPos());
			result.put("CD_MEMBER", param.getCdMember());
			result.put("NO_RCP", param.getNoRcp());
			result.put("YN_SAVE", param.getYnSave());

			// 권한 확인
			Map<String, Object> posInfo = posBaseService.checkPermissionAndGetPosInfo(authentication, param.getCdCompany(), param.getCdStore());

			SingleMap saveResult = posRewardService.saveStamp(param, posInfo);
			result.putAll(saveResult);

			result.put("RESULT_MSG", "Saved stamp. stamp count is " + saveResult.get("CNT_STAMP"));
			result.put("SUCCESS", true);

		} catch (InvalidParameterException e) {
			logger.error("[{}] {}", PosUtil.EPO_0005_CODE, "Failed save stamp.", e);
			result.put("RESULT_MSG", e.getMessage());
			result.put("ERR_CODE", PosUtil.EPO_0005_CODE);

		} catch (RequestResolveException e) {
			logger.error("[{}] {}", e.getCode(), "Failed save stamp.", e);
			result.put("RESULT_MSG", e.getMessage());
			result.put("ERR_CODE", e.getCode());

		} catch (Throwable e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
			result.put("ERR_CODE", PosUtil.EPO_0000_CODE);
		}

		return result;
	}

	/**
	 * 쿠폰 조회
	 * 
	 * @param jsonParam
	 * @param authentication
	 * @return
	 */
	@RequestMapping(value = "/S_COUPON_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sCouponInfo(@RequestParam(value = "PARAM") String jsonParam, Authentication authentication) {

		logger.debug("sCouponInfo: " + jsonParam);

		// 결과값
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...

		try {

			SingleMap param = objectMapper.readValue(jsonParam, SingleMap.class);

			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posInfo = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			result.put("DATAS", posRewardService.getCouponInfoWithValidation(param, posInfo));
			result.put("RESULT_MSG", "Success.");
			result.put("SUCCESS", true);

		} catch (InvalidParameterException e) {
			logger.error("[{}] {}", PosUtil.EPO_0005_CODE, "Failed save stamp.", e);
			result.put("RESULT_MSG", e.getMessage());
			result.put("ERR_CODE", PosUtil.EPO_0005_CODE);

		} catch (RequestResolveException e) {
			logger.error("[{}] {}", e.getCode(), "Failed save stamp.", e);
			result.put("RESULT_MSG", e.getMessage());
			result.put("ERR_CODE", e.getCode());

		} catch (Throwable e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
			result.put("ERR_CODE", PosUtil.EPO_0000_CODE);
		}

		return result;
	}

}
