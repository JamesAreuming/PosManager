package com.jc.pico.controller.pos;

import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.service.pos.PosEtcService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosResult;
import com.jc.pico.utils.bean.SingleMap;

/**
 * POS 연동부 중 기타;
 * 
 * 
 *
 */
@RestController
@RequestMapping(value = "/api/pos/store")
public class PosEtcController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PosEtcService posEtcService;

	private ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		objectMapper = new ObjectMapper();
	}

	@RequestMapping(value = "/E_APP_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult eAppInfo(@RequestParam(value = "PARAM") String jsonString) { // 통합 파라미터
		logger.debug("eAppInfo : " + jsonString);
		PosResult posResult = new PosResult();
		try {
			SingleMap param = objectMapper.readValue(jsonString, SingleMap.class);
			List<SingleMap> result = posEtcService.getAppInfo(param);
			posResult.setSuccess(true);
			posResult.setResultMsg("Success");
			posResult.setRows(result.size());
			posResult.setDatas(result);

		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg("Failed cause by Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	@RequestMapping(value = "/E_STORE_ALIVE", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult eStoreAlive(@RequestParam(value = "PARAM") String jsonString) { // 통합 파라미터
		logger.debug("eStoreAlive : " + jsonString);
		PosResult posResult = new PosResult();
		try {
			SingleMap param = objectMapper.readValue(jsonString, SingleMap.class);
			posEtcService.updateStoreAlive(param);
			posResult.setSuccess(true);
			posResult.setResultMsg("Success");

		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg("Failed cause by Exceptions... " + e.getMessage());
		}
		return posResult;
	}

}
