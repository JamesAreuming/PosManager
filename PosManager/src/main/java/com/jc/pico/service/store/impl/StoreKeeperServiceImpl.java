package com.jc.pico.service.store.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.pico.service.store.StoreKeeperService;
import com.jc.pico.utils.CodeUtil;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.store.StoreCctvMapper;
import com.jc.pico.utils.customMapper.store.StoreEventLogMapper;

@Service
public class StoreKeeperServiceImpl implements StoreKeeperService {

	@Autowired
	CodeUtil codeUtil;

	@Autowired
	private StoreCctvMapper cctvMapper;

	@Autowired
	private StoreEventLogMapper eventMapper;

	@Override
	public SingleMap getStoreCctvList(SingleMap param) {
		/*
		 * req
		 * storeId
		 * 
		 * response
		 * nvrInfo : cctv 접속을 위한 인증 정보
		 * 	- userId : nvr user id
		 *  - password : nvr password
		 *  - port : nvr port
		 *  - host : nvr host
		 * items : cctv 목록
		 * 	- cctvNo : cctv 번호
		 * 	- name : cctv 이름
		 * 	 
		 */
		SingleMap result = new SingleMap();
		result.put("nvrInfo", cctvMapper.selectStoreCctvInfo(param));
		result.put("items", cctvMapper.selectStoreCctvList(param));
		return result;
	}

	@Override
	public SingleMap getStoreEventList(SingleMap param) {
		/*
		 * req
		 * - storeId
		 * - eventType : 이벤트 종류 (결제, 결제취소, 주문취소, 반품)
		 * 
		 * res
		 *  items : 주문의 이벤트정보
		 * 	- eventType : 이벤트 타입
		 * - eventDate yyyy-MM-dd hh:mm:ss
		 * - orderNo : 주문번호 
		 * 
		 */
		SingleMap result = new SingleMap();
		String eventTp = param.getString("eventType", null);
		if (eventTp != null && eventTp.length() > 0) {
			param.put("eventType", codeUtil.getBaseCodeByAlias(eventTp));
		}
		result.put("items", eventMapper.selectStoreEventList(param));
		return result;
	}

}
