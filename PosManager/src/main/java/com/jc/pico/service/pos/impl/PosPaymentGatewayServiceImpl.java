package com.jc.pico.service.pos.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderExample;
import com.jc.pico.bean.SvcSales;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.service.pos.PaymentGatewayService;
import com.jc.pico.service.pos.PosPaymentGatewayService;
import com.jc.pico.service.pos.SalesService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosOrderPayment;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.pos.PosOrderPaymentMapper;

/**
 * POS로 부터 들어오는 PG 관련 결제 요청을 처리 한다.
 * 
 * 
 *
 */
@Service
public class PosPaymentGatewayServiceImpl implements PosPaymentGatewayService {

	private static final Logger logger = LoggerFactory.getLogger(PosPaymentGatewayServiceImpl.class);

	@Autowired
	private SvcOrderMapper svcOrderMapper;

	@Autowired
	private PosOrderPaymentMapper posOrderPaymentMapper;

	@Autowired
	private PaymentGatewayService paymentGatewayService;

	@Autowired
	private SalesService salesService;

	@Autowired
	private PosUtil posUtil;

	@Override
	public List<PosOrderPayment> purchase(SingleMap param) throws RequestResolveException {

		logger.debug("purchase : " + param);

		Long storeId = param.getLong("CD_STORE");
		Date openDt = posUtil.parseDate(param.getString("YMD_ORDER"), null);
		String orderNo = param.getString("NO_ORDER");

		SvcOrder order = getSvcOrder(storeId, openDt, orderNo);
		if (order == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, "Not found order.");
		}

		boolean isSuccess = paymentGatewayService.purchase(order);

		if (!isSuccess) {
			throw new RequestResolveException(PosUtil.EPO_0011_CODE_FAILED_PG_PAY, "Failed pg payment.");
		}

		return posOrderPaymentMapper.selectListByOrderId(order.getId());
	}

	@Override
	public List<PosOrderPayment> refundFromOrder(SingleMap param) throws RequestResolveException {

		logger.debug("refundFromOrder : " + param);

		Long storeId = param.getLong("CD_STORE");
		Date openDt = posUtil.parseDate(param.getString("YMD_ORDER"), null);
		String orderNo = param.getString("NO_ORDER");

		SvcOrder order = getSvcOrder(storeId, openDt, orderNo);
		if (order == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, "Not found order.");
		}

		boolean isSuccess = paymentGatewayService.refund(order);

		if (!isSuccess) {
			throw new RequestResolveException(PosUtil.EPO_0012_CODE_FAILED_PG_REFUND, "Failed pg refund.");
		}

		return posOrderPaymentMapper.selectListByOrderId(order.getId());
	}

	private SvcOrder getSvcOrder(Long storeId, Date openDt, String orderNo) {
		SvcOrderExample example = new SvcOrderExample();
		example.createCriteria() // 포스 PK 로 주문 조회
				.andStoreIdEqualTo(storeId) // 매장 ID
				.andOpenDtEqualTo(openDt) // 개점일
				.andOrderNoEqualTo(orderNo); // 주문 번호
		List<SvcOrder> orders = svcOrderMapper.selectByExample(example);

		return orders.isEmpty() ? null : orders.get(0);
	}

	@Override
	public void refundFromSales(SingleMap param) throws RequestResolveException {

		logger.debug("refundFromSales : " + param);

		Long storeId = param.getLong("CD_STORE");
		Date openDt = posUtil.parseDate(param.getString("YMD_SALE"), null);
		String posNo = param.getString("NO_POS");
		String receiptNo = param.getString("NO_RCP");

		// 매출 정보에는 결제 번호가 없으므로 매출로 부터 주문 정보를 조회하여 처리함

		// 매출 조회
		SvcSales sales = salesService.getSales(storeId, openDt, posNo, receiptNo);
		if (sales == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, "Not found sales.");
		}

		// 매출과 관련된 주문 조회
		if (sales.getOrderId() == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, "Not found related order.");
		}

		SvcOrder order = svcOrderMapper.selectByPrimaryKey(sales.getOrderId());

		if (order == null) {
			throw new RequestResolveException(PosUtil.EPO_0008_CODE_DATA_NOT_FOUND, "Not found order.");
		}

		// 취소 처리
		boolean isSuccess = paymentGatewayService.refund(order);

		if (!isSuccess) {
			throw new RequestResolveException(PosUtil.EPO_0012_CODE_FAILED_PG_REFUND, "Failed pg refund.");
		}

	}

}
