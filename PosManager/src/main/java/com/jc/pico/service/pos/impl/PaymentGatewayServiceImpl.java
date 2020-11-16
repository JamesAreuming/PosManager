package com.jc.pico.service.pos.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.bean.SvcBrand;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderPay;
import com.jc.pico.bean.SvcOrderPayExample;
import com.jc.pico.bean.SvcPayLog;
import com.jc.pico.bean.SvcStoreSet;
import com.jc.pico.bean.UserCard;
import com.jc.pico.ext.pg.PGHandler;
import com.jc.pico.ext.pg.PayBean;
import com.jc.pico.mapper.SvcBrandMapper;
import com.jc.pico.mapper.SvcOrderPayMapper;
import com.jc.pico.mapper.SvcPayLogMapper;
import com.jc.pico.service.pos.PaymentGatewayService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.customMapper.pos.PosStoreMapper;

@Service
public class PaymentGatewayServiceImpl implements PaymentGatewayService {

	private static final Logger logger = LoggerFactory.getLogger(PaymentGatewayServiceImpl.class);

	/**
	 * 결제 대기
	 */
	private static final String PAYMENT_ST_PENDING = "415002";

	/**
	 * 결제 성공
	 */
	private static final String PAYMENT_ST_PAY_SUCCESS = "415003";

	/**
	 * 결제 실패
	 */
	private static final String PAYMENT_ST_PAY_FAIL = "415004";

	/**
	 * 결제 취소 성공
	 */
	private static final String PAYMENT_ST_REFUND_SUCCESS = "415005";

	/**
	 * 결제 취소 실패
	 */
	private static final String PAYMENT_ST_REFUND_FAIL = "415006";

	/**
	 * 결제 해야하는 대상 상태 목록
	 */
	private static final List<String> sPayablePaymentStList = new ArrayList<>();
	static {
		sPayablePaymentStList.add(PAYMENT_ST_PENDING);
		sPayablePaymentStList.add(PAYMENT_ST_PAY_FAIL);
	}

	/**
	 * 결제 취소 해야하는 대상 상태 목록
	 */
	private static final List<String> sRefundablePaymentStList = new ArrayList<>();
	static {
		sRefundablePaymentStList.add(PAYMENT_ST_PAY_SUCCESS);
		sRefundablePaymentStList.add(PAYMENT_ST_REFUND_FAIL);
	}

	@Autowired
	private SvcOrderPayMapper svcOrderPayMapper;

	@Autowired
	private SvcBrandMapper svcBrandMapper;

	@Autowired
	private SvcPayLogMapper svcPaymentLogMapper;

	@Autowired
	private PosStoreMapper posStoreMapper;

	@Autowired
	private PosUtil posUtil;

	private ObjectMapper objectMapper;

	@PostConstruct
	public void init() {
		objectMapper = new ObjectMapper();
	}

	@Override
	public boolean purchase(SvcOrder order) {

		// 결제 대상 목록 조회
		SvcOrderPayExample example = new SvcOrderPayExample();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(order.getId()) // 해당 주문				
				.andPayStIn(sPayablePaymentStList) // 결제 해야하는 상태 만
				.andCardInfoIsNotNull() // 카드 정보 없음 제외
				.andCardInfoNotEqualTo(StringUtils.EMPTY); // 카드 저옵 없음 제외
		List<SvcOrderPay> pays = svcOrderPayMapper.selectByExample(example);

		logger.info("Payment purchase " + order.getId() + ", pays=" + pays.size());

		SvcBrand brand = svcBrandMapper.selectByPrimaryKey(order.getBrandId());
		SvcStoreSet storeSet = getPaymentGatewayStoreSet(order.getStoreId());

		for (SvcOrderPay pay : pays) {

			try {
				UserCard card = posUtil.parseCardInfo(pay.getCardInfo());

				PayBean payBean = new PayBean();

				payBean.setPgKind(storeSet.getPgKind());
				payBean.setMerchantID(storeSet.getPgMerchantId());
				payBean.setPrivateKey(storeSet.getPgPrivateKey());
				payBean.setPaymentKey(storeSet.getPgPaymentKey());

				payBean.setAmount(pay.getAmount());
				payBean.setCardCvv(card.getSecretCode());
				payBean.setCardExpDate(card.getExpireMonth() + card.getExpireYear());
				payBean.setCardHolderName(card.getOwnerName());
				payBean.setCardNumber(card.getCardNo());
				payBean.setCurrency(brand.getCurrency());
				payBean.setOrderId(String.valueOf(order.getId()));
				payBean.setPayMethod(pay.getPayMethod());

				HashMap<String, String> payResult = PGHandler.purchase(payBean);

				// 결제 실패
				if (!"200".equals(payResult.get("Code"))) {
					logger.error("Failed payment bill. resultCode={}, payId={}, amount={}", payResult.get("Code"), pay.getId(), pay.getAmount());

					pay.setPaySt(PAYMENT_ST_PAY_FAIL);

					SvcOrderPay record = new SvcOrderPay();
					record.setId(pay.getId());
					record.setPaySt(pay.getPaySt());
					record.setPgKind(storeSet.getPgKind());
					svcOrderPayMapper.updateByPrimaryKeySelective(record);

					addPayLog(pay, objectMapper.writeValueAsString(payResult));

					return false;
				}

				// 결제 성공
				pay.setTranNo(payResult.get("Id"));
				pay.setPaySt(PAYMENT_ST_PAY_SUCCESS);
				pay.setPgKind(storeSet.getPgKind());

				// 결제 번호, 상태 갱신
				SvcOrderPay record = new SvcOrderPay();
				record.setId(pay.getId());
				record.setCardInfo(""); // 카드 정보는 결제가 완료되면 보안을 이유로삭제 하기로 정책으로 정해짐.
				record.setPaySt(pay.getPaySt());
				record.setTranNo(pay.getTranNo());
				record.setPgKind(storeSet.getPgKind());
				svcOrderPayMapper.updateByPrimaryKeySelective(record);

				addPayLog(pay, objectMapper.writeValueAsString(payResult));

				logger.info("Success payment bill. payId={}, amount={}, tranNo={}", pay.getId(), pay.getAmount(), pay.getTranNo());

			} catch (Exception e) {
				logger.error("Failed payment bill. payId={}, amount={}", pay.getId(), pay.getAmount(), e);

				// 결제 실패
				SvcOrderPay record = new SvcOrderPay();
				record.setId(pay.getId());
				record.setPaySt(PAYMENT_ST_PAY_FAIL);
				record.setPgKind(storeSet.getPgKind());
				svcOrderPayMapper.updateByPrimaryKeySelective(record);

				addPayLog(pay, e.getMessage());

				return false;
			}
		}

		return true;
	}

	@Override
	public boolean refund(SvcOrder order) {

		// 결제 대상 목록 조회
		SvcOrderPayExample example = new SvcOrderPayExample();
		example.createCriteria() // 조건
				.andOrderIdEqualTo(order.getId()) // 해당 주문				
				.andPayStIn(sRefundablePaymentStList) // 결제 취소 해야하는 상태 만
				.andTranNoIsNotNull() // 결제 번호 없음 제외 
				.andTranNoNotEqualTo(StringUtils.EMPTY); // 결제 번호 없음 제외
		List<SvcOrderPay> pays = svcOrderPayMapper.selectByExample(example);

		logger.info("Payment refund " + order.getId() + ", pays=" + pays.size());

		SvcBrand brand = svcBrandMapper.selectByPrimaryKey(order.getBrandId());
		SvcStoreSet storeSet = getPaymentGatewayStoreSet(order.getStoreId());

		for (SvcOrderPay pay : pays) {

			try {

				PayBean payBean = new PayBean();

				payBean.setPgKind(storeSet.getPgKind());
				payBean.setMerchantID(storeSet.getPgMerchantId());
				payBean.setPrivateKey(storeSet.getPgPrivateKey());
				payBean.setPaymentKey(storeSet.getPgPaymentKey());

				payBean.setTransactionId(pay.getTranNo());
				payBean.setAmount(pay.getAmount());
				payBean.setCurrency(brand.getCurrency());

				HashMap<String, String> payResult = PGHandler.cancel(payBean);

				// 반품 실패
				if (!"200".equals(payResult.get("Code"))) {
					logger.error("Failed payment refund. payId={}, tranNo={}, amount={}, resultCode=", pay.getId(), pay.getTranNo(), pay.getAmount(),
							payResult.get("Code"));

					pay.setPaySt(PAYMENT_ST_REFUND_FAIL);

					SvcOrderPay record = new SvcOrderPay();
					record.setId(pay.getId());
					record.setPaySt(pay.getPaySt());
					record.setPgKind(storeSet.getPgKind());
					svcOrderPayMapper.updateByPrimaryKeySelective(record);

					addPayLog(pay, objectMapper.writeValueAsString(payResult));

					return false;
				}

				// 취소 성공
				pay.setTranNo(payResult.get("Id"));
				pay.setPaySt(PAYMENT_ST_REFUND_SUCCESS);

				// 결제 번호, 상태 갱신
				SvcOrderPay record = new SvcOrderPay();
				record.setId(pay.getId());
				record.setCardInfo(""); // 카드 정보는 결제가 완료되면 보안을 이유로삭제 하기로 정책으로 정해짐.
				record.setPaySt(pay.getPaySt());
				record.setTranNo(pay.getTranNo());
				record.setPgKind(storeSet.getPgKind());
				svcOrderPayMapper.updateByPrimaryKeySelective(record);

				addPayLog(pay, objectMapper.writeValueAsString(payResult));

				logger.info("Success payment refund. payId={}, transNo={}, amount={}", pay.getId(), pay.getTranNo(), pay.getAmount());

			} catch (Exception e) {
				logger.error("Failed payment refund. payId={}, tranNo={}, amount={}", pay.getId(), pay.getTranNo(), pay.getAmount(), e);

				// 취소 실패
				SvcOrderPay record = new SvcOrderPay();
				record.setId(pay.getId());
				record.setPaySt(PAYMENT_ST_REFUND_FAIL);
				record.setPgKind(storeSet.getPgKind());
				svcOrderPayMapper.updateByPrimaryKeySelective(record);

				addPayLog(pay, e.getMessage());

				return false;
			}
		}

		return true;

	}

	/**
	 * 결제 로그 기록
	 * 
	 * @param orderPayId
	 *            주문 결제 ID
	 * @param salesPayId
	 *            매출 결제 ID
	 * @param tranNo
	 *            결제 transaction 번호
	 * @param paySt
	 *            결제 상태 (415xxx)
	 * @param payLog
	 *            결제 로그 상세
	 */
	private void addPayLog(Long orderPayId, Long salesPayId, String tranNo, String paySt, String payLog) {
		SvcPayLog record = new SvcPayLog();
		record.setOrderPayId(orderPayId);
		record.setSalesPayId(salesPayId);
		record.setTranNo(tranNo == null ? "" : tranNo);
		record.setPaySt(paySt);
		record.setPayLog(payLog);
		svcPaymentLogMapper.insertSelective(record);
	}

	private void addPayLog(SvcOrderPay pay, String payLog) {
		addPayLog(pay.getId(), null, pay.getTranNo(), pay.getPaySt(), payLog);
	}

	/**
	 * PG 결제를 위한 설정 정보 조회
	 * 상점 설정 사용이 활성화 되어 있으면 상정의 PG 설정 정보 반환.
	 * 그외 브랜드 PG 설정 정보 반환.
	 * 그에 설정 정보는 담고 있지 않음.
	 * 
	 * @param storeId
	 * @return PG 결제 정보
	 */
	private SvcStoreSet getPaymentGatewayStoreSet(Long storeId) {
		return posStoreMapper.selectPaymentGatewayStoreSet(storeId);
	}

}
