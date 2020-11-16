package com.jc.pico.controller.pos;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

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
import com.jc.pico.service.pos.PosBaseService;
import com.jc.pico.service.pos.PosCashbookService;
import com.jc.pico.service.pos.PosOrderService;
import com.jc.pico.service.pos.PosPaymentGatewayService;
import com.jc.pico.service.pos.PosSalesService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosCashBookInfo;
import com.jc.pico.utils.bean.PosCashbookSave;
import com.jc.pico.utils.bean.PosSalesClosingSave;
import com.jc.pico.utils.bean.PosSalesSaleSave;
import com.jc.pico.utils.bean.SingleMap;

/**
 * POS 연동부 중 매출정보 저장 컨트롤러
 * 출납부 조회 저장
 * 
 * @author green
 *
 */
@RestController
@RequestMapping(value = "/api/pos/store")
public class PosSalesSaleController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private PosSalesService posSalesService;

	@Autowired
	private PosCashbookService posCashbookService;

	@Autowired
	private PosBaseService posBaseService;

	@Autowired
	private PosPaymentGatewayService posPaymentGatewayService;

	@Autowired
	private PosUtil posUtil;

	private final ObjectMapper objectMapper = new ObjectMapper();
	
	@PostConstruct
	public void init() {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	/**
	 * POS 연동 (1. S_SALE_SAVE : 매출정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드</b></li>
	 *            <li><b style="color:#FF0F65;">YMD_SALE</b> String <b>날짜</b></li>
	 *            <li><b style="color:#FF0F65;">NO_POS</b> String <b>포스번호</b></li>
	 *            <li><b style="color:#FF0F65;">NO_RCP</b> Long <b>전표번호</b></li>
	 *            <li><b style="color:#FF0F65;">DS_SALE</b> Integer <b>판매형태코드(1:정상, 2:반품, 3:주문,
	 *            4:시식, 5:증정, 6:폐기, 7:접대, 8:손실, 9:Void, 10:점간이동, 11:배달, 12:선입금, 13:외상매출입금,
	 *            14:상품교환)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_MEMBER</b> String <b>고객번호</b></li>
	 *            <li><b style="color:#FF0F65;">NM_MEMBER</b> String <b>고객명</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE</b> String <b>결제자</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SALE</b> Double <b>금액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SUPPLY</b> Double <b>공급가액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_TAX</b> Double <b>부가세</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_DC</b> Double <b>할인금액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_TIP</b> Double <b>봉사료</b></li>
	 *            <li><b style="color:#FF0F65;">CD_CUSTOMER_TYPE</b> Integer <b>고객형태코드</b></li>
	 *            <li><b style="color:#FF0F65;">CD_CUSTOMER_AGE</b> Integer <b>고객연령대코드</b></li>
	 *            <li><b style="color:#FF0F65;">CD_CUSTOMER_SEX</b> Integer <b>고객성별코드</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_CUSTOMER</b> Integer <b>객수</b></li>
	 *            <li><b style="color:#FF0F65;">DT_SALE</b> String <b>매출일시</b></li>
	 *            <li><b style="color:#FF0F65;">DT_ADMISSION</b> String <b>입장일시</b></li>
	 *            <li><b style="color:#FF0F65;">DT_EXIT</b> String <b>퇴장일시</b></li>
	 *            <li><b style="color:#FF0F65;">NO_TABLE</b> Long <b>테이블번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_SECTION</b> Long <b>테이블섹션코드</b></li>
	 *            <li><b style="color:#FF0F65;">CD_TOUR</b> String <b>여행사코드</b></li>
	 *            <li><b style="color:#FF0F65;">NO_GUIDE</b> Long <b>가이드번호</b></li>
	 *            <li><b style="color:#FF0F65;">YN_FOREIGNER</b> Integer <b>외국인유무</b></li>
	 *            <li><b style="color:#FF0F65;">CD_CURRENCY</b> Integer <b>통화단위코드</b></li>
	 *            <li><b style="color:#FF0F65;">RT_EXCHANGE</b> Double <b>환율</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_FOREIGN_CURRENCY</b> Double <b>외화금액</b></li>
	 *            <li><b style="color:#FF0F65;">RT_EXCHANGE_USD</b> Double <b>달러환율</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_USD</b> Double <b>달러금액</b></li>
	 *            <li><b style="color:#FF0F65;">PNT_OCCUR</b> Double <b>적립 포인트</b></li>
	 *            <li><b style="color:#FF0F65;">PNT_REST</b> Double <b>잔여 포인트</b></li>
	 *            <li><b style="color:#FF0F65;">MEMO</b> String <b>메모</b></li>
	 *            <li><b style="color:#FF0F65;">CD_BRAND</b> Long <b>브랜드코드</b></li>
	 *            <li><b style="color:#FF0F65;">YMD_SALE_ORG</b> String <b>원 매출일자</b></li>
	 *            <li><b style="color:#FF0F65;">NO_POS_ORG</b> String <b>원 포스번호</b></li>
	 *            <li><b style="color:#FF0F65;">NO_RCP_ORG</b> Long <b>원 영수증번호</b></li>
	 *            <li><b style="color:#FF0F65;">NO_CLOSING</b> Long <b>마감번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_RETURN_REASON</b> Integer <b>반품사유코드</b></li>
	 *            <li><b style="color:#FF0F65;">YMD_ORDER</b> String <b>주문일자</b></li>
	 *            <li><b style="color:#FF0F65;">NO_ORDER</b> Long <b>주문번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_VENDOR</b> String <b>거래처코드</b></li>
	 *            <li><b style="color:#FF0F65;">NO_PAGER</b> Long <b>진동벨번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_BALJU</b> String <b>발주번호</b></li>
	 *            <li><b style="color:#FF0F65;">DETAIL</b> List <b>상세 내역</b></li>
	 *            <li><b style="color:#FF0F65;">PAY</b> List <b>결제 내역</b></li>
	 *            <li><b style="color:#FF0F65;">DISCOUNT</b> List <b>할인 내역</b></li>
	 *            <li><b style="color:#FF0F65;">CASH</b> List <b>현금영수증 내역</b></li>
	 *            <li><b style="color:#FF0F65;">CARD</b> List <b>신용카드 내역</b></li>
	 *            <li><b style="color:#FF0F65;">OKCASHBAG</b> List <b>캐쉬백 내역</b></li>
	 *            </ol>
	 * @Example <b>MASTER :</b>
	 *          {"CD_COMPANY": "5", "CD_STORE": "23", "YMD_SALE": "", "NO_POS": "", "NO_RCP": 0,
	 *          "DS_SALE": 0, "CD_MEMBER": "", "NM_MEMBER": "", "CD_EMPLOYEE": "", "AMT_SALE": 0,
	 *          "AMT_SUPPLY": 0, "AMT_TAX": 0, "AMT_DC": 0, "AMT_TIP": 0, "CD_CUSTOMER_TYPE": 0,
	 *          "CD_CUSTOMER_AGE": 0, "CD_CUSTOMER_SEX": 0, "CNT_CUSTOMER": 0, "DT_SALE": "",
	 *          "DT_ADMISSION": "", "DT_EXIT": "", "NO_TABLE": 0, "CD_SECTION": 0, "CD_TOUR": "",
	 *          "NO_GUIDE": 0, "YN_FOREIGNER": 0, "CD_CURRENCY": 0, "RT_EXCHANGE": 0,
	 *          "AMT_FOREIGN_CURRENCY": 0, "RT_EXCHANGE_USD": 0, "AMT_USD": 0, "PNT_OCCUR": 0,
	 *          "PNT_REST": 0, "MEMO": "", "CD_BRAND": 0, "YMD_SALE_ORG": "", "NO_POS_ORG": "",
	 *          "NO_RCP_ORG": 0, "NO_CLOSING": 0, "CD_RETURN_REASON": 0, "YMD_ORDER": "",
	 *          "NO_ORDER": 0, "CD_VENDOR": "", "NO_PAGER": 0, "CD_BALJU": "", "DETAIL": [], "PAY":
	 *          [], "DISCOUNT": [], "CASH": [], "CARD": [], "OKCASHBAG": []}
	 * @param DETAIL
	 *            상세 내역
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">SEQ</b> Long <b>일련번호</b></li>
	 *            <li><b style="color:#FF0F65;">DS_SALE</b> Integer <b>판매형태코드(일반, 폐기, 서비스, 자가소비, 증정,
	 *            접대, 손실, 스탬프, 상품교환(단품반품))</b></li>
	 *            <li><b style="color:#FF0F65;">CD_SALE_TYPE</b> Integer <b>세트코스구분코드</b></li>
	 *            <li><b style="color:#FF0F65;">NO_GROUP</b> Long <b>세트코스그룹번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE_MNG</b> String <b>통계사업단위</b></li>
	 *            <li><b style="color:#FF0F65;">CD_GOODS</b> String <b>품번</b></li>
	 *            <li><b style="color:#FF0F65;">NM_GOODS</b> String <b>품명</b></li>
	 *            <li><b style="color:#FF0F65;">DS_TAX</b> Integer <b>과세구분코드</b></li>
	 *            <li><b style="color:#FF0F65;">PR_BUY</b> Double <b>매입단가</b></li>
	 *            <li><b style="color:#FF0F65;">PR_SALE</b> Double <b>판매단가</b></li>
	 *            <li><b style="color:#FF0F65;">QTY_SALE</b> Integer <b>수량</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SALE</b> Double <b>금액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SUPPLY</b> Double <b>공급가액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_TAX</b> Double <b>부가세</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_DC</b> Double <b>할인금액</b></li>
	 *            <li><b style="color:#FF0F65;">RT_COMMISSION</b> Double <b>수수료율</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_COMMISSION</b> Double <b>인센티브(금액)</b></li>
	 *            <li><b style="color:#FF0F65;">MEMO</b> String <b>메모</b></li>
	 *            <li><b style="color:#FF0F65;">YN_SET_SUB</b> Integer <b>구성품유무</b></li>
	 *            <li><b style="color:#FF0F65;">CD_NO_SALE_REASON</b> Integer <b>비매출사유</b></li>
	 *            <li><b style="color:#FF0F65;">YN_PACKING</b> Integer <b>포장여부</b></li>
	 *            </ol>
	 * @Example <b>DETAIL :</b>
	 *          {"SEQ": 0, "DS_SALE": 0, "CD_SALE_TYPE": 0, "NO_GROUP": 0, "CD_STORE_MNG": "",
	 *          "CD_GOODS": "", "NM_GOODS": "", "DS_TAX": 0, "PR_BUY": 0, "PR_SALE": 0, "QTY_SALE":
	 *          0, "AMT_SALE": 0, "AMT_SUPPLY": 0, "AMT_TAX": 0, "AMT_DC": 0, "RT_COMMISSION": 0,
	 *          "AMT_COMMISSION": 0, "MEMO": "", "YN_SET_SUB": 0, "CD_NO_SALE_REASON": 0,
	 *          "YN_PACKING": 0}
	 * @param DISCOUNT
	 *            할인내역
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">SEQ</b> Long <b>일련번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_DC_TYPE</b> Integer <b>할인TYPE코드</b></li>
	 *            <li><b style="color:#FF0F65;">NO_DC</b> Long <b>할인번호</b></li>
	 *            <li><b style="color:#FF0F65;">DS_APPLY</b> Integer <b>금액여부</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SET</b> Double <b>셋팅금액</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_DC</b> Double <b>할인금액</b></li>
	 *            <li><b style="color:#FF0F65;">NO_ADD</b> String <b>할인부가번호</b></li>
	 *            </ol>
	 * @Example <b>DISCOUNT :</b>
	 *          {"SEQ": 0, "CD_DC_TYPE": 0, "NO_DC": 0, "DS_APPLY": 0, "AMT_SET": 0, "AMT_DC": 0,
	 *          "NO_ADD": ""}
	 * @see PosOrderService#saveOrder(Map)
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_SALE_SAVE", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> sSaleSave(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {

		logger.debug("S_SALE_SAVE : " + jsonString);

		PosSalesSaleSave posSalesSaleSave = null;

		// 결과값
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...

		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId   = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

			posSalesSaleSave = objectMapper.readValue(jsonString, PosSalesSaleSave.class);

			// 응답시 아래 정보를 항상 포함. 포스측에서 응답 구분에 사용
			result.put("CD_COMPANY", posSalesSaleSave.getCdCompany());
			result.put("CD_STORE", posSalesSaleSave.getCdStore());
			result.put("YMD_SALE", posSalesSaleSave.getYmdOrder());
			result.put("NO_POS", posSalesSaleSave.getNoPos());
			result.put("NO_RCP", posSalesSaleSave.getNoRcp());

			// Validation
			// 1. 들어온 주문정보에 cdCompany 와 cdStore 가 oAuth 정보와 다르면 부정확한 정보로 튕겨내기.
			String cdCompany = posSalesSaleSave.getCdCompany(); // 회사코드(주문기일때 미사용)
			String cdStore = posSalesSaleSave.getCdStore(); // 매장코드(주문기일때 미사용)

			if (storeId == null || storeId.longValue() == 0 || !storeId.toString().equals(cdStore) || companyId == null || companyId.longValue() == 0
					|| !companyId.toString().equals(cdCompany)) {
				logger.error("[{}][{}] authenticated store: {} vs. requested store: {}, authenticated company: {} vs. requested company: {}",
						PosUtil.EPO_0004_CODE, PosUtil.EPO_0004_MSG, storeId, cdStore, companyId, cdCompany);
				result.put("RESULT_MSG", "StoreID(" + cdStore + ") or CompanyID(" + cdCompany + ") is not valid in oAuth");
				return result;
			}

			// 주문 결제 완료
			// tb_svc_sales
			// tb_svc_sales_item
			// tb_svc_sales_pay
			// tb_svc_order_item_history (X)
			// tb_svc_cctv_log(X)
			String salesId = posSalesService.saveSale(posBaseKeyMap, posSalesSaleSave);

			if (salesId == null || salesId.isEmpty()) {
				result.put("RESULT_MSG", "Error on persistence layer");
			} else {
				result.put("SUCCESS", true);
				result.put("RESULT_MSG", "SALES_ID:" + salesId);
			}

		} catch (Throwable e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}
		return result;
	}

	/**
	 * POS 연동 (2. S_CLOSING_SAVE : 마감정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드</b></li>
	 *            <li><b style="color:#FF0F65;">YMD_CLOSE</b> String <b>판매일자</b></li>
	 *            <li><b style="color:#FF0F65;">NO_POS</b> String <b>포스번호</b></li>
	 *            <li><b style="color:#FF0F65;">YN_CLOSE</b> Integer <b>마감여부</b></li>
	 *            <li><b style="color:#FF0F65;">DT_OPEN</b> String <b>개점일시</b></li>
	 *            <li><b style="color:#FF0F65;">DT_CLOSE</b> String <b>마감일시</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_OPEN_RESERVE</b> Double <b>준비금</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SALE</b> Double <b>판매금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_SALE</b> Integer <b>판매건수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SALE_RETURN</b> Double <b>반품금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_SALE_RETURN</b> Integer <b>반품건수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_DC</b> Double <b>할인금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_DC</b> Integer <b>할인건수</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_CUSTOMER</b> Integer <b>객수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CASH_IN</b> Double <b>현금입금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_CASH_IN</b> Integer <b>현금입금건수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CASH_OUT</b> Double <b>현금출금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_CASH_OUT</b> Integer <b>현금출금건수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CASH_ON_HAND</b> Double <b>현금시재</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CASH_LACK</b> Double <b>과부족</b></li>
	 *            <li><b style="color:#FF0F65;">DETAIL</b> List <b>상세 내역</b></li>
	 *            </ol>
	 * @Example <b>Master :</b>
	 *          {"CD_COMPANY": "5", "CD_STORE": "23", "YMD_CLOSE": "", "NO_POS": "", "YN_CLOSE": 0,
	 *          "DT_OPEN": "", "DT_CLOSE": "", "AMT_OPEN_RESERVE": 0, "AMT_SALE": 0, "CNT_SALE": 0,
	 *          "AMT_SALE_RETURN": 0, "CNT_SALE_RETURN": 0, "AMT_DC": 0, "CNT_DC": 0,
	 *          "CNT_CUSTOMER": 0, "AMT_CASH_IN": 0, "CNT_CASH_IN": 0, "AMT_CASH_OUT": 0,
	 *          "CNT_CASH_OUT": 0, "AMT_CASH_ON_HAND": 0, "AMT_CASH_LACK": 0, "DETAIL": []}
	 * @param DETAIL
	 *            상세 내역
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">SEQ</b> Long <b>일련번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_EMPLOYEE</b> String <b>사원코드</b></li>
	 *            <li><b style="color:#FF0F65;">YN_CLOSE</b> Integer <b>마감여부</b></li>
	 *            <li><b style="color:#FF0F65;">DT_OPEN</b> String <b>개점일시</b></li>
	 *            <li><b style="color:#FF0F65;">DT_CLOSE</b> String <b>마감일시</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_OPEN_RESERVE</b> Double <b>준비금</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SALE</b> Double <b>판매금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_SALE</b> Integer <b>판매건수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_SALE_RETURN</b> Double <b>반품금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_SALE_RETURN</b> Integer <b>반품건수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_DC</b> Double <b>할인금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_DC</b> Integer <b>할인건수</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_CUSTOMER</b> Integer <b>객수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CASH_IN</b> Double <b>현금입금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_CASH_IN</b> Integer <b>현금입금건수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CASH_OUT</b> Double <b>현금출금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_CASH_OUT</b> Integer <b>현금출금건수</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CHECK</b> Double <b>수표금액</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_100000</b> Integer <b>10만원</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_50000</b> Integer <b>5만원</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_10000</b> Integer <b>1만원</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_5000</b> Integer <b>5천원</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_1000</b> Integer <b>1천원</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_500</b> Integer <b>500원</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_100</b> Integer <b>100원</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_50</b> Integer <b>50원</b></li>
	 *            <li><b style="color:#FF0F65;">CNT_10</b> Integer <b>10원</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CASH_ON_HAND</b> Double <b>현금시재</b></li>
	 *            <li><b style="color:#FF0F65;">AMT_CASH_LACK</b> Double <b>과부족</b></li>
	 *            </ol>
	 * @Example <b>Detail :</b>
	 *          {"SEQ": 1, "CD_EMPLOYEE": "", "YN_CLOSE": 0, "DT_OPEN": "", "DT_CLOSE": "",
	 *          "AMT_OPEN_RESERVE": 0, "AMT_SALE": 0, "CNT_SALE": 0, "AMT_SALE_RETURN": 0,
	 *          "CNT_SALE_RETURN": 0, "AMT_DC": 0, "CNT_DC": 0, "CNT_CUSTOMER": 0, "AMT_CASH_IN": 0,
	 *          "CNT_CASH_IN": 0, "AMT_CASH_OUT": 0, "CNT_CASH_OUT": 0, "AMT_CHECK": 0,
	 *          "CNT_100000": 0, "CNT_50000": 0, "CNT_10000": 0, "CNT_5000": 0, "CNT_1000": 0,
	 *          "CNT_500": 0, "CNT_100": 0, "CNT_50": 0, "CNT_10": 0, "AMT_CASH_ON_HAND": 0,
	 *          "AMT_CASH_LACK": 0}
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_CLOSING_SAVE", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Map<String, Object> sClosingSave(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		PosSalesClosingSave posSalesClosingSave = null;

		// 결과값
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

			posSalesClosingSave = mapper.readValue(jsonString, PosSalesClosingSave.class);

			// 응답시 아래 정보를 항상 포함. 포스측에서 응답 구분에 사용
			result.put("CD_COMPANY", posSalesClosingSave.getCdCompany());
			result.put("CD_STORE", posSalesClosingSave.getCdStore());
			result.put("YMD_CLOSE", posSalesClosingSave.getYmdClose());
			result.put("NO_POS", posSalesClosingSave.getNoPos());

			// Validation
			// 1. 들어온 주문정보에 cdCompany 와 cdStore 가 oAuth 정보와 다르면 부정확한 정보로 튕겨내기.
			String cdCompany = posSalesClosingSave.getCdCompany(); // 회사코드(주문기일때 미사용)
			String cdStore = posSalesClosingSave.getCdStore(); // 매장코드(주문기일때 미사용)

			if (storeId == null || storeId.longValue() == 0 || !storeId.toString().equals(cdStore) || companyId == null || companyId.longValue() == 0
					|| !companyId.toString().equals(cdCompany)) {
				logger.error("[{}][{}] authenticated store: {} vs. requested store: {}, authenticated company: {} vs. requested company: {}",
						PosUtil.EPO_0004_CODE, PosUtil.EPO_0004_MSG, storeId, cdStore, companyId, cdCompany);
				result.put("RESULT_MSG", "StoreID(" + cdStore + ") or CompanyID(" + cdCompany + ") is not valid in oAuth");
				return result;
			}

			Long svcClosingId = posSalesService.saveClosing(posBaseKeyMap, posSalesClosingSave);

			if (svcClosingId == null || svcClosingId.longValue() == 0L) {
				result.put("RESULT_MSG", "Error on persistence layer");
			} else {
				result.put("SUCCESS", true);
				result.put("RESULT_MSG", "CLOSING_ID:" + svcClosingId);
			}

		} catch (RequestResolveException e) {
			logger.error("[{}] {}", e.getCode(), e.getMessage(), e);
			result.put("ERR_CODE", e.getCode());
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		} catch (Throwable e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}
		return result;
	}

	/**
	 * 출납 조회
	 * 지정한 기간내 출납을 조회합니다.
	 * 
	 * @param param
	 *            CD_COMPANY : 회사 코드
	 *            CD_STORE : 매장 코드
	 *            DT_START : 조회 시작일 yyyymmdd
	 *            DT_END : 조회 종료일 yyyymmdd
	 * 
	 * @return
	 */
	@RequestMapping(value = "/S_CASHBOOK_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sCashbookInfo(@RequestParam(value = "PARAM") String jsonParam, Authentication authentication) {

		logger.debug("sCashbookInfo: " + jsonParam);

		// 결과값
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...

		try {

			SingleMap param = objectMapper.readValue(jsonParam, SingleMap.class);

			String cdCompany = param.getString("CD_COMPANY"); // 회사 코드
			String cdStore = param.getString("CD_STORE"); // 매장코드

			// 권한 확인
			posBaseService.checkPermissionAndGetPosInfo(authentication, cdCompany, cdStore);

			List<PosCashBookInfo> cashBookInfoList = posCashbookService.getCashBookInfo(param);
			result.put("DATAS", cashBookInfoList);
			result.put("RESULT_MSG", "Success. datas count is " + cashBookInfoList.size());
			result.put("SUCCESS", true);

		} catch (InvalidParameterException e) {
			logger.error("[{}] {}", PosUtil.EPO_0005_CODE, "Failed sCashbookInfo.", e);
			result.put("RESULT_MSG", e.getMessage());
			result.put("ERR_CODE", PosUtil.EPO_0005_CODE);

		} catch (RequestResolveException e) {
			logger.error("[{}] {}", e.getCode(), "Failed sCashbookInfo.", e);
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
	 * 금전 출납부 저장
	 * 
	 * @param param
	 *            CD_COMPANY : 회사코드
	 *            CD_STORE : 매장코드
	 *            YMD_SALE : 날짜 yyyyMMdd
	 *            NO_POS : 포스번호
	 *            SEQ : 일련번호
	 *            CD_ACCOUNT : 계정과목(1~11, 공통코드 30번 참조)
	 *            CD_CACHEBOOK_TYPE : 출납유형코드(1: 입금, 2: 출금)
	 *            AMT_CACHEBOOK : 금액
	 *            MEMO : 메모
	 *            DT_INSERT : 등록일시
	 *            CD_EMPLOYEE_INSERT : 등록자
	 *            DT_UPDATE : 수정일시
	 *            CD_EMPLOYEE_UPDATE : 수정자
	 * 
	 * @return
	 * 		SUCCESS : 성공/실패
	 *         RESULT_MSG : 결과 메시지 (디버깅용)
	 *         CD_COMPANY : 회사 코드
	 *         CD_STORE : 상점 코드
	 *         YMD_SALE : 날짜
	 *         NO_POS : 포스번호
	 *         SEQ : 순번
	 */
	@RequestMapping(value = "/S_CASHBOOK_SAVE", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sCashbookSave(@RequestParam(value = "PARAM") String jsonParam, Authentication authentication) {

		logger.debug("sCashbookSave: " + jsonParam);

		// 결과값
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...

		try {

			PosCashbookSave param = objectMapper.readValue(jsonParam, PosCashbookSave.class);

			// 응답시 아래 정보를 항상 포함. 포스측에서 응답 구분에 사용
			result.put("CD_COMPANY", param.getCdCompany());
			result.put("CD_STORE", param.getCdStore());
			result.put("YMD_SALE", param.getYmdSale());
			result.put("NO_POS", param.getNoPos());
			result.put("SEQ", param.getSeq());

			// 권한 확인
			Map<String, Object> posInfo = posBaseService.checkPermissionAndGetPosInfo(authentication, param.getCdCompany(), param.getCdStore());

			long cashBookId = posCashbookService.saveCashBook(param, posInfo);

			result.put("RESULT_MSG", "Saved cashbook. server id is " + cashBookId);
			result.put("SUCCESS", true);

		} catch (InvalidParameterException e) {
			logger.error("[{}] {}", PosUtil.EPO_0005_CODE, "Failed save cashbook.", e);
			result.put("RESULT_MSG", e.getMessage());
			result.put("ERR_CODE", PosUtil.EPO_0005_CODE);

		} catch (RequestResolveException e) {
			logger.error("[{}] {}", e.getCode(), "Failed save cashbook.", e);
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
	 * PG 취소 요청을 처리한다.
	 * 
	 * 매출 정보 > 3. S_SALE_PG
	 * 
	 * 
	 * @param jsonString
	 * 
	 *            <pre>
	 * 	- CD_COMPANY 회사ID (franchise id)
	 * 	- CD_STORE 매장 ID
	 * 	- YMD_SALE 개점일
	 *  - NO_POS 포스 번호
	* - NO_RCP 영수증 번호
	 *            </pre>
	 * 
	 * @return
	 */
	@RequestMapping(value = "/S_SALE_PG", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sSalePg(@RequestParam(value = "PARAM") String jsonString, Authentication authentication) { // 통합 파라미터

		logger.debug("sSalePg: " + jsonString);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {

			SingleMap param = objectMapper.readValue(jsonString, SingleMap.class);

			// 응답시 요청 (PK) 정보를 항상 포함. 포스측에서 응답 구분에 사용
			result.putAll(param);

			// 권한 확인
			posBaseService.checkPermissionAndGetPosInfo(authentication, param.getString("CD_COMPANY"), param.getString("CD_STORE"));

			// 취소 처리
			posPaymentGatewayService.refundFromSales(param);

			result.put("SUCCESS", true);
			result.put("RESULT_MSG", "Success");

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
