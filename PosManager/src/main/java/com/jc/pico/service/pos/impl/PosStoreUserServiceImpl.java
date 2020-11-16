package com.jc.pico.service.pos.impl;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jc.pico.service.pos.PosStoreUserService;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcSalesExtended;
import com.jc.pico.utils.customMapper.pos.PosStoreUserMapper;

/**
 * 매장별 사용자의 매출 통계를 갱신 처리한다
 * 
 * @author hyo
 *
 */
@Service
public class PosStoreUserServiceImpl implements PosStoreUserService {

	private static final Logger logger = LoggerFactory.getLogger(PosStoreUserServiceImpl.class);

	/**
	 * 사용자 등급 : VIP
	 */
	public static final String USER_LEVEL_VIP = "302002";

	/**
	 * 매출 상태 : 정상
	 */
	public static final String SALES_ST_NORMAL = "809001";

	/**
	 * 매출 상태 : 반품
	 */
	public static final String SALES_ST_RETURN = "809002";

	@Autowired
	private PosStoreUserMapper posStoreUserMapper;

	@Override
	@Transactional
	public void resolveStoreUserSales(SvcSalesExtended sales) {

		logger.info("resolveStoreUserSales storeId={}, userId={}, sales={}", sales.getStoreId(), sales.getUserId(), sales.getSales());

		if (sales.getUserId() == null || sales.getUserId().longValue() == 0) {
			// 사용자 정보가 없으면 무시
			return;
		}

		if (!StringUtils.equals(sales.getSalesSt(), SALES_ST_NORMAL) && !StringUtils.equals(sales.getSalesSt(), SALES_ST_RETURN)) {
			// 정상, 반품 매출만 처리 함
			return;
		}

		// 정상 매출이면 증가, 반품이면 감소
		final int salesCount = StringUtils.equals(sales.getSalesSt(), SALES_ST_NORMAL) ? 1 : -1;

		SingleMap param = new SingleMap();
		param.put("brandId", sales.getBrandId());
		param.put("storeId", sales.getStoreId());
		param.put("userId", sales.getUserId());
		param.put("salesAmount", sales.getSales()); // 최소 매출은 마이너스 금액으로 발생하므로 구분할 필요 없음
		param.put("salesCount", salesCount);

		// 매출 갱신 처리
		int count = posStoreUserMapper.updateSales(param);

		// 갱신 결과가 0건이면 신규 건 입력 처리
		if (count == 0) {
			param.put("userLevel", USER_LEVEL_VIP);
			count = posStoreUserMapper.insertSales(param);
		}
		
		logger.debug("resolveStoreUserSales count={}, data={}", count, param);
	}

}
