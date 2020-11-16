package com.jc.pico.controller.pos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.service.pos.PosOrderService;
import com.jc.pico.service.pos.PosPaymentGatewayService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosOrderPayment;
import com.jc.pico.utils.bean.PosSalesOrderInfo;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcOrderExtended;

/**
 * POS 연동부 중 주문정보 저장 컨트롤러
 * 
 * @author green
 *
 */
@RestController
@RequestMapping(value = "/api/pos/store")
public class PosSalesOrderController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PosOrderService posOrderService;

	@Autowired
	private PosPaymentGatewayService posPaymentService;

	@Autowired
	private PosUtil posUtil;

	private ObjectMapper mapper;

	@PostConstruct
	public void init() {
		mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * POS 연동 (11. S_ORDER_SAVE : 주문정보 저장)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">YMD_ORDER</b> String <b>주문일자(yyyymmdd) (신규주문일때는
	 *            빈값)</b></li>
	 *            <li><b style="color:#FF0F65;">NO_ORDER</b> Long <b>주문번호 (신규주문일때는 0)</b></li>
	 *            <li><b style="color:#FF0F65;">DS_ORDER</b> Integer <b>주문형태 (1:테이블, 2:배달,
	 *            3:포장)</b></li>
	 *            <li><b style="color:#FF0F65;">DT_ADMISSION</b> String <b>입장시간</b></li>
	 *            <li><b style="color:#FF0F65;">DT_EXIT</b> String <b>퇴장시간</b></li>
	 *            <li><b style="color:#FF0F65;">CD_CUSTOMER_TYPE</b> Integer <b>고객형태코드</b></li>
	 *            <li><b style="color:#FF0F65;">CD_CUSTOMER_AGE</b> Integer <b>고객연령대코드</b></li>
	 *            <li><b style="color:#FF0F65;">CD_CUSTOMER_SEX</b> Integer <b>고객성별코드</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_CUSTOMER</b> Integer <b>객수</b></li>
	 *            <li><b style="color:#FF0F65;">CD_TOUR</b> String <b>여행사코드</b></li>
	 *            <li><b style="color:#FF0F65;">NO_GUIDE</b> Long <b>가이드번호</b></li>
	 *            <li><b style="color:#FF0F65;">YN_FOREIGNER</b> Integer <b>외국인유무</b></li>
	 *            <li><b style="color:#FF0F65;">CD_MEMBER</b> String <b>회원코드</b></li>
	 *            <li><b style="color:#FF0F65;">NM_MEMBER</b> String <b>회원명</b></li>
	 *            <li><b style="color:#FF0F65;">CD_MEMBER_DELIVERY</b> String <b>배달고객번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE</b> String <b>사원코드</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_ORDER</b> Double <b>주문금액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SUPPLY</b> Double <b>공급가</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_TAX</b> Double <b>부가세</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_DC</b> Double <b>할인금액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_TIP</b> Double <b>봉사료</b></li>
	 *            <li><b style="color:#FF0F65;">MEMO</b> String <b>메모</b></li>
	 *            <li><b style="color:#FF0F65;">DS_STATUS</b> Integer <b>주문상태(1:주문 2:전체취소
	 *            3:합석)</b></li>
	 *            <li><b style="color:#FF0F65;">YN_SALE</b> Integer <b>정산 완료 여부(1:예 0:아니오)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_VENDOR</b> String <b>거래처 코드</b></li>
	 *            <li><b style="color:#FF0F65;">YMD_BOOKING</b> String <b>예약일</b></li>
	 *            <li><b style="color:#FF0F65;">NO_BOOKING</b> Long <b>예약번호</b></li>
	 *            <li><b style="color:#FF0F65;">NO_ORDER_PRINT</b> Long <b>주문번호출력</b></li>
	 *            <li><b style="color:#FF0F65;">DT_INSERT</b> String <b>등록일시</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE_INSERT</b> String <b>등록자</b></li>
	 *            <li><b style="color:#FF0F65;">DT_UPDATE</b> String <b>수정일시</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE_UPDATE</b> String <b>수정자</b></li>
	 *            <li><b style="color:#FF0F65;">NO_TABLE</b> Long <b>테이블번호</b></li>
	 *            <li><b style="color:#FF0F65;">NO_PARTITION</b> Long <b>파티션번호(고정값 0)</b></li>
	 *            <li><b style="color:#FF0F65;">DETAIL</b> ArrayList <b>상세 내역</b></li>
	 *            </ol>
	 * @Example <b>MASTER :</b>
	 *          {"CD_COMPANY": "", "CD_STORE": "", "YMD_ORDER": "", "NO_ORDER": 0, "DS_ORDER": 0,
	 *          "DT_ADMISSION": "", "DT_EXIT": "", "CD_CUSTOMER_TYPE": 0, "CD_CUSTOMER_AGE": 0,
	 *          "CD_CUSTOMER_SEX": 0, "CNT_CUSTOMER": 0, "CD_TOUR": "", "NO_GUIDE": 0,
	 *          "YN_FOREIGNER": 0, "CD_MEMBER": "", "NM_MEMBER": "", "CD_MEMBER_DELIVERY": "",
	 *          "CD_EMPLOYEE": "", "AMT_ORDER": 0, "AMT_SUPPLY": 0, "AMT_TAX": 0, "AMT_DC": 0,
	 *          "AMT_TIP": 0, "MEMO": "", "DS_STATUS": 0, "YN_SALE": 0, "CD_VENDOR": "",
	 *          "YMD_BOOKING": "", "NO_BOOKING": 0, "NO_ORDER_PRINT": 0, "DT_INSERT": "",
	 *          "CD_EMPLOYEE_INSERT": "", "DT_UPDATE": "", "CD_EMPLOYEE_UPDATE": "", "NO_TABLE": 0,
	 *          "NO_PARTITION": 0, "DETAIL": []}
	 * @param DETAIL
	 *            상세 내역
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">SEQ</b> Long <b>일련번호</b></li>
	 *            <li><b style="color:#FF0F65;">DS_ORDER</b> Integer <b>주문취소여부 (0:정상, 1:취소)</b></li>
	 *            <li><b style="color:#FF0F65;">DS_SALE</b> Integer <b>판매형태 (0:일반, 1:폐기, 2:서비스,
	 *            3:자가소비)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_SALE_TYPE</b> Integer <b>판매유형 (0:일반 1:고정셋트 3:코스
	 *            5:0원단가변동(금액형) 6:중량형)</b></li>
	 *            <li><b style="color:#FF0F65;">YN_SET_SUB</b> Integer <b>셋트구성품여부</b></li>
	 *            <li><b style="color:#FF0F65;">NO_GROUP</b> Long <b>세트코스그룹번호</b></li>
	 *            <li><b style="color:#FF0F65;">QTY_ITEM</b> Integer <b>셋트단위수량</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE_MNG</b> String <b>통계사업단위</b></li>
	 *            <li><b style="color:#FF0F65;">CD_GOODS</b> String <b>품번</b></li>
	 *            <li><b style="color:#FF0F65;">NM_GOODS</b> String <b>품명</b></li>
	 *            <li><b style="color:#FF0F65;">DS_TAX</b> Integer <b>매출과세구분</b></li>
	 *            <li><b style="color:#FF0F65;">PR_BUY</b> Double <b>매입단가</b></li>
	 *            <li><b style="color:#FF0F65;">PR_SALE</b> Double <b>판매단가</b></li>
	 *            <li><b style="color:#FF0F65;">QTY_ORDER</b> Integer <b>주문수량</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_ORDER</b> Double <b>주문금액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SUPPLY</b> Double <b>공급가</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_TAX</b> Double <b>부가세</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_DC</b> Double <b>할인금액</b></li>
	 *            <li><b style="color:#FF0F65;">MEMO</b> String <b>메모</b></li>
	 *            <li><b style="color:#FF0F65;">CD_NO_SALE_REASON</b> Integer <b>비매출 사유</b></li>
	 *            <li><b style="color:#FF0F65;">DT_ORDER</b> String <b>주문일시 (신규주문일때는 빈값)</b></li>
	 *            <li><b style="color:#FF0F65;">YN_PACKING</b> Integer <b>포장여부</b></li>
	 *            <li><b style="color:#FF0F65;">NO_GIFT_CARD</b> String <b>기프트카드번호</b></li>
	 *            <li><b style="color:#FF0F65;">DT_INSERT</b> String <b>등록일시</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE_INSERT</b> String <b>등록자</b></li>
	 *            <li><b style="color:#FF0F65;">DT_UPDATE</b> String <b>수정일시</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE_UPDATE</b> String <b>수정자</b></li>
	 *            <li><b style="color:#FF0F65;">DISCOUNT</b> ArrayList <b>할인 내역</b></li>
	 *            <li><b style="color:#FF0F65;">HISTORY</b> ArrayList <b>주문 변동 이력</b></li>
	 *            </ol>
	 * @Example <b>DETAIL :</b> {"SEQ": 0, "DS_ORDER": 0, "DS_SALE": 0, "CD_SALE_TYPE": 0,
	 *          "YN_SET_SUB": 0, "NO_GROUP": 0, "QTY_ITEM": 0, "CD_STORE_MNG": "", "CD_GOODS": "",
	 *          "NM_GOODS": "", "DS_TAX": 0, "PR_BUY": 0, "PR_SALE": 0, "QTY_ORDER": 0, "AMT_ORDER":
	 *          0, "AMT_SUPPLY": 0, "AMT_TAX": 0, "AMT_DC": 0, "MEMO": "", "CD_NO_SALE_REASON": 0,
	 *          "DT_ORDER": "", "YN_PACKING": 0, "NO_GIFT_CARD": "", "DT_INSERT": "",
	 *          "CD_EMPLOYEE_INSERT": "", "DT_UPDATE": "", "CD_EMPLOYEE_UPDATE": "", "DISCOUNT": [],
	 *          "HISTORY": []}
	 * @param DISCOUNT
	 *            할인내역
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_DC_TYPE</b> Integer <b>할인형태</b></li>
	 *            <li><b style="color:#FF0F65;">NO_DC</b> Long <b>할인번호</b></li>
	 *            <li><b style="color:#FF0F65;">DS_APPLY</b> Integer <b>적용구분</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SET</b> Double <b>설정금액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_DC</b> Double <b>할인금액</b></li>
	 *            <li><b style="color:#FF0F65;">NO_ADD</b> String <b>부가번호</b></li>
	 *            <li><b style="color:#FF0F65;">NO_ADD2</b> String <b>부가번호2</b></li>
	 *            <li><b style="color:#FF0F65;">REMARK</b> String <b>비고</b></li>
	 *            <li><b style="color:#FF0F65;">PNT_USE</b> Integer <b>포인트사용여부</b></li>
	 *            <li><b style="color:#FF0F65;">DT_INSERT</b> String <b>등록일시</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE_INSERT</b> String <b>등록자</b></li>
	 *            <li><b style="color:#FF0F65;">DT_UPDATE</b> String <b>수정일시</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE_UPDATE</b> String <b>수정자</b></li>
	 *            </ol>
	 * @Example <b>DISCOUNT :</b> {"CD_DC_TYPE": 0, "NO_DC": 0, "DS_APPLY": 0, "AMT_SET": 0,
	 *          "AMT_DC": 0, "NO_ADD": "", "NO_ADD2": "", "REMARK": "", "PNT_USE": 0, "DT_INSERT":
	 *          "", "CD_EMPLOYEE_INSERT": "", "DT_UPDATE": "", "CD_EMPLOYEE_UPDATE": ""}
	 * @param HISTORY
	 *            주문 변동 이력
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">SEQ1</b> Long <b>일련번호1</b></li>
	 *            <li><b style="color:#FF0F65;">QTY_ORDER</b> Integer <b>주문수량</b></li>
	 *            <li><b style="color:#FF0F65;">MEMO</b> String <b>메모</b></li>
	 *            <li><b style="color:#FF0F65;">DS_STATUS</b> Integer <b>상태(사용안함)</b></li>
	 *            <li><b style="color:#FF0F65;">ORDER_SORT</b> Integer <b>주문순서</b></li>
	 *            <li><b style="color:#FF0F65;">CD_CANCEL_REASON</b> Integer <b>취소사유코드</b></li>
	 *            <li><b style="color:#FF0F65;">DT_ORDER</b> String <b>주문일시 (신규주문일때는 빈값)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE</b> String <b>사원코드</b></li>
	 *            <li><b style="color:#FF0F65;">DT_INSERT</b> String <b>등록일시</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE_INSERT</b> String <b>등록자</b></li>
	 *            <li><b style="color:#FF0F65;">DT_UPDATE</b> String <b>수정일시</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE_UPDATE</b> String <b>수정자</b></li>
	 *            </ol>
	 * @Example <b>HISTORY :</b> {"SEQ1": 0, "QTY_ORDER": 0, "MEMO": "", "DS_STATUS": 0,
	 *          "ORDER_SORT": 0, "CD_CANCEL_REASON": 0, "DT_ORDER": "", "CD_EMPLOYEE": "",
	 *          "DT_INSERT": "", "CD_EMPLOYEE_INSERT": "", "DT_UPDATE": "", "CD_EMPLOYEE_UPDATE":
	 *          ""}
	 * @see PosOrderService#saveOrder(Map)
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_ORDER_SAVE", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> sOrderSave(@RequestParam(value = "PARAM") String jsonString) // 통합 파라미터
	{
		logger.debug("sOrderSave : " + jsonString);

		PosSalesOrderInfo posSalesOrderSave = null;

		// 결과값
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId   = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

			posSalesOrderSave = mapper.readValue(jsonString, PosSalesOrderInfo.class);

			// Validation
			// 1. 들어온 주문정보에 cdCompany 와 cdStore 가 oAuth 정보와 다르면 부정확한 정보로 튕겨내기.
			String cdCompany = posSalesOrderSave.getCdCompany(); // 회사코드(주문기일때 미사용)
			String cdStore   = posSalesOrderSave.getCdStore(); // 매장코드(주문기일때 미사용)

			if (storeId == null || storeId.longValue() == 0 || !storeId.toString().equals(cdStore) || companyId == null || companyId.longValue() == 0
					|| !companyId.toString().equals(cdCompany)) {
				logger.error("[{}][{}] authenticated store: {} vs. requested store: {}, authenticated company: {} vs. requested company: {}",
						PosUtil.EPO_0004_CODE, PosUtil.EPO_0004_MSG, storeId, cdStore, companyId, cdCompany);
				result.put("RESULT_MSG", "StoreID(" + cdStore + ") or CompanyID(" + cdCompany + ") is not valid in oAuth");
				return result;
			}

			SvcOrderExtended savedOrder = posOrderService.saveOrder(posBaseKeyMap, posSalesOrderSave);

			result.put("CD_COMPANY", posSalesOrderSave.getCdCompany());
			result.put("CD_STORE", posSalesOrderSave.getCdStore());
			result.put("YMD_ORDER", posSalesOrderSave.getYmdOrder());
			result.put("NO_ORDER", posSalesOrderSave.getNoOrder());
			result.put("ORDER_ID", savedOrder.getId());
			
			result.put("SUCCESS", true);
			result.put("RESULT_MSG", "ORDER_ID= " + savedOrder.getId() + ", NO_ORDER=" + savedOrder.getOrderNo());

		} catch (Throwable e) {
			logger.error("[{}] Failed order save from pos. Cause by {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}
		
		return result;
	}

	/**
	 * PG 결제/취소 요청을 처리한다.
	 * 결제 요청이면 해당 중문에 PG를 토한 선결제가 필요한 내용이 있으면 결제 처리 한다.
	 * 취소 요청이면 결제된 건에 대해 취소 처리 한다.
	 * 
	 * 
	 * 주문정보 > 13. S_PG_PAYMENT
	 * 
	 * @param jsonString
	 * 
	 *            <pre>
	 * 	- CD_COMPANY 회사ID (franchise id)
	 * 	- CD_STORE 매장 ID
	 * 	- YMD_ORDER 개점일
	 * 	- NO_ORDER 주문 번호
	 *  - DS_MODE 동작 (1: 결제, 2:취소)
	 *            </pre>
	 * 
	 * @return
	 */
	@RequestMapping(value = "/S_ORDER_PG", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sOrderPg(@RequestParam(value = "PARAM") String jsonString) { // 통합 파라미터

		logger.debug("sOrderPg: " + jsonString);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {

			SingleMap param = mapper.readValue(jsonString, SingleMap.class);

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

			// 권한 확인
			String cdStore = param.getString("CD_STORE");
			String cdCompany = param.getString("CD_COMPANY");
			if (!StringUtils.equals(cdCompany, String.valueOf(companyId)) || !StringUtils.equals(cdStore, String.valueOf(storeId))) {
				logger.error("[{}][{}] authenticated store: {} vs. requested store: {}, authenticated company: {} vs. requested company: {}",
						PosUtil.EPO_0004_CODE, PosUtil.EPO_0004_MSG, storeId, cdStore, companyId, cdCompany);
				result.put("RESULT_MSG", "StoreID(" + cdStore + ") or CompanyID(" + cdCompany + ") is not valid in oAuth");
				return result;
			}

			int dsMode = param.getInt("DS_MODE");

			List<PosOrderPayment> datas;
			if (dsMode == 1) { // 결제 처리
				datas = posPaymentService.purchase(param);
			} else { // == 2 취소 처리
				datas = posPaymentService.refundFromOrder(param);
			}

			result.put("SUCCESS", true);
			result.put("RESULT_MSG", "Success");
			result.put("DATAS", datas);

		} catch (RequestResolveException e) {
			logger.error("[{}] Failed pg payment. {}", e.getCode(), e.getMessage(), e);
			result.put("RESULT_MSG", "RequestResolveException: " + e.getMessage());
		} catch (Throwable e) {
			logger.error("[{}][{}] Failed pg payment. {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}

		return result;
	}

}
