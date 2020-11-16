package com.jc.pico.service.store.impl;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.bean.SvcStoreSales;
import com.jc.pico.bean.SvcStoreSalesInfo;
import com.jc.pico.service.store.StoreCommonService;
import com.jc.pico.service.store.StoreSalesService;
import com.jc.pico.utils.AuthenticationUtils;
import com.jc.pico.utils.JsonConvert;
import com.jc.pico.utils.MathUtil;
import com.jc.pico.utils.Page;
import com.jc.pico.utils.Paginate;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.StaffUserDetail;
import com.jc.pico.utils.customMapper.store.StoreBrandSalesMapper;
import com.jc.pico.utils.customMapper.store.StoreCalendarMapper;
import com.jc.pico.utils.customMapper.store.StoreCommonMapper;
import com.jc.pico.utils.customMapper.store.StoreDetailSalesMapper;
import com.jc.pico.utils.customMapper.store.StoreItemSalesMapper;
import com.jc.pico.utils.customMapper.store.StorePaymentSalesMapper;
import com.jc.pico.utils.customMapper.store.StoreServiceSalesMapper;
import com.jc.pico.utils.customMapper.store.StoreStoreSalesMapper;
import com.jc.pico.utils.customMapper.store.StoreWeekTimeSalesMapper;

@Service
public class StoreSalesServiceImpl implements StoreSalesService {

	@Autowired
	private StoreCommonMapper commonMapper;

	@Autowired
	private StoreBrandSalesMapper brandSalesMapper;

	@Autowired
	private StoreStoreSalesMapper storeSalesMapper;

	@Autowired
	private StoreItemSalesMapper itemSalesMapper;

	@Autowired
	private StoreServiceSalesMapper serviceSalesMapper;

	@Autowired
	private StorePaymentSalesMapper paymentSalesMapper;

	@Autowired
	private StoreWeekTimeSalesMapper weekTimeSalesMapper;

	@Autowired
	private StoreDetailSalesMapper detailSalesMapper;

	@Autowired
	private StoreCalendarMapper calendarMapper;

	@Autowired
	private StoreCommonService commonService;
	
	private ObjectMapper objectMapper;
	
	@PostConstruct
	public void init() {
		objectMapper = JsonConvert.getObjectMapper();
	}

	@Override
	public SingleMap getBrandSalesSummary(SingleMap param) {

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("amount", brandSalesMapper.selectSummaryTotal(param));
		result.put("items", brandSalesMapper.selectSummaryList(param));

		return result;
	}

	@Override
	public SingleMap getBrandSalesDaily(SingleMap param) {

		param.put("pagination", calendarMapper.selectPaginationDaily(param));
		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("items", brandSalesMapper.selectDailyList(param));

		return result;
	}

	@Override
	public SingleMap getBrandSalesWeekly(SingleMap param) {

		param.put("pagination", calendarMapper.selectPaginationWeekly(param));
		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("items", brandSalesMapper.selectWeeklyList(param));

		return result;
	}

	@Override
	public SingleMap getBrandSalesMonthly(SingleMap param) {

		param.put("pagination", calendarMapper.selectPaginationMonthly(param));
		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("items", brandSalesMapper.selectMonthlyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreSalesSummary(SingleMap param) {
		/*
		 * request
		 * storeId, startDate, endDate
		 * 
		 * response
		 * sales : 매출 금액
		 * discount : 할인 금액
		 * realSales : 실매출액
		 * tax : 부가세
		 * netSales: 순매출금액
		 * payMethodSales : 결제 수단별 매출
		 * 		- label, 
		 * 		- cardSalesTotal : 카드 매출 
		 * 		- cashSalesTotal : 현금 매출 
		 * 		- otherSalesTotal : 기타 매출
		 * customerTotal: 고객수
		 * hourlySales : 시간대별 매출 목록
		 *  	- label : 시간대
		 *  	- sales : 금액
		 *
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.putAll(storeSalesMapper.selectSalesSummary(param));
		result.put("payMethodSales", storeSalesMapper.selectPayMethodSales(param));
		result.put("hourlySales", storeSalesMapper.selectHourlySalesList(param));

		return result;
	}

	@Override
	public SingleMap getStoreSalesDaily(SingleMap param) {
		/*
		 * req : storeId, startDate, endDate
		 * response 
		 * items 
		 *  	- label : 일별 날짜
		 *  	- salesTotal : 매출 금액
		 *  	- customerTotal : 고객수 
		 * 
		 * 		
		 */

		param.put("pagination", calendarMapper.selectPaginationDaily(param));
		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("items", storeSalesMapper.selectDailyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreSalesWeekly(SingleMap param) {
		/*
		 * req : storeId, startDate, endDate
		 * response 
		 * items 
		 *  	- label : 주별 날짜
		 *  	- salesTotal : 매출 금액
		 *  	- customerTotal : 고객수 
		 * 
		 * 		
		 */

		param.put("pagination", calendarMapper.selectPaginationWeekly(param));
		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("items", storeSalesMapper.selectWeeklyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreSalesMonthly(SingleMap param) {
		/*
		 * req : storeId, startDate, endDate
		 * response 
		 * items 
		 *  	- label : 월별 날짜
		 *  	- salesTotal : 매출 금액
		 *  	- customerTotal : 고객수 
		 * 
		 * 		
		 */

		param.put("pagination", calendarMapper.selectPaginationMonthly(param));
		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("items", storeSalesMapper.selectMonthlyList(param));

		return result;
	}

	@Override
	public SingleMap getStoreItemSales(SingleMap param) {
		/*
		 * req : storeId, startDate, endDate
		 * response
		 * amount : 함계 		
		 * 		- salesCount : 판매 수량
		 * 		- salesTotal : 매출 금액
		 * items 
		 *  	- label : 상품명 
		 *  	- salesCount : 판매 수량 
		 *  	- salesTotal : 매출 금액 
		 * 
		 * 		
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("amount", itemSalesMapper.selectItemTotal(param));
		result.put("items", itemSalesMapper.selectItemList(param));

		return result;
	}

	@Override
	public SingleMap getStoreItemCateSales(SingleMap param) {
		/*
		 * req : storeId, startDate, endDate
		 * response
		 * amount : 함계 		
		 * 		- salesCount : 판매 수량
		 * 		- salesTotal : 매출 금액
		 * items 
		 *  	- label : 상품명 
		 *  	- salesCount : 판매 수량 
		 *  	- salesTotal : 매출 금액 
		 * 
		 * 		
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();

		result.put("amount", itemSalesMapper.selectItemCateTotal(param));
		result.put("items", itemSalesMapper.selectItemCateList(param));

		return result;
	}

	@Override
	public SingleMap getStoreServiceSales(SingleMap param) {
		/*
		 * req : storeId
		 * response
		 * amount : 함계 		
		 * 		- orderTotal : 주문건수
		 * 		- salesTotal : 매출 금액
		 * offline : 오프라인 목록
		 * 		- amount : 합계
		 * 			- orderTotal : 주문건수
		 * 			- salesTotal : 매출 금액
		 * 		- items : 세부 항목
		 * 			- label : 명칭
		 * 			- orderTotal : 주문 건수
		 * 			- salesTotal : 매출 금액 
		 * online : 온라인 목록
		 * 		- amount : 합계
		 * 			- orderTotal : 주문건수
		 * 			- salesTotal : 매출 금액
		 * 		- items : 세부 항목
		 * 			- label : 명칭
		 * 			- orderTotal : 주문 건수
		 * 			- salesTotal : 매출 금액
		 * 		
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap offline = new SingleMap();
		offline.put("amount", serviceSalesMapper.selectOfflineServiceTotal(param));
		offline.put("items", serviceSalesMapper.selectOfflineServiceList(param));

		SingleMap online = new SingleMap();
		online.put("amount", serviceSalesMapper.selectOnlineServiceTotal(param));
		online.put("items", serviceSalesMapper.selectOnlineServiceList(param));

		SingleMap result = new SingleMap();
		result.put("amount", serviceSalesMapper.selectServiceTotal(param));
		result.put("offline", offline);
		result.put("online", online);

		return result;
	}

	@Override
	public SingleMap getStorePaymentSales(SingleMap param) {
		/*
		 * req : storeId
		 * response
		 * amount : 합계
		 * 		- paymentCount : 결제건수
		 *  	- salesTotal : 매출금액
		 * items : 세부 항목
		 * 		- label : 결제구분
		 * 		- paymentCount : 결제건수
		 * 		- saleTotal : 매출 금액 
		 * 	
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();
		result.put("amount", paymentSalesMapper.selectPaymentTotal(param));
		result.put("items", paymentSalesMapper.selectPaymentList(param));
		return result;
	}

	@Override
	public SingleMap getStoreSalesTime(SingleMap param) {
		/*
		 * req : storeId
		 * response
		 * amount : 합계
		 * 		- paymentCount : 결제건수
		 *  	- salesTotal : 매출금액
		 * items : 세부 항목
		 * 		- label : 결제구분
		 * 		- paymentCount : 결제건수
		 * 		- saleTotal : 매출 금액 
		 * 	
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap result = new SingleMap();
		result.put("amount", weekTimeSalesMapper.selectTimeTotal(param));
		result.put("items", weekTimeSalesMapper.selectTimeList(param));

		return result;
	}

	@Override
	public SingleMap getStoreSalesWeek(SingleMap param) {
		/*
		 * req : storeId
		 * response
		 * amount : 합계
		 * 		- dayCount : 결제건수
		 *  	- salesAvg : 매출 평균 금액
		 * items : 세부 항목
		 * 		- label : 요일
		 * 		- dayCount : 일수
		 * 		- saleAvg : 매출 평균 금액 
		 * 	
		 */

		int currencyFraction = commonService.getBrandFractionDigits(param); 
		param.put("currencyFraction", currencyFraction);

		List<SingleMap> weekSales = weekTimeSalesMapper.selectWeekList(param);

		SingleMap result = new SingleMap();
		result.put("amount", getWeekSalesAmount(weekSales, currencyFraction));
		result.put("items", weekSales);

		return result;
	}

	/**
	 * 일수와 매출 평균을 합산
	 * 
	 * @param weekSales
	 * @return
	 */
	private SingleMap getWeekSalesAmount(List<SingleMap> weekSales, int currencyFraction) {
		long dayCount = 0;
		double salesTotal = 0;
		double salesAvg = 0;
		for (SingleMap week : weekSales) {
			dayCount += week.getLong("dayCount");
			salesTotal += week.getDouble("salesTotal");
			salesAvg += week.getDouble("salesAvg");
		}
		SingleMap weekSalesAmount = new SingleMap();
		weekSalesAmount.put("dayCount", MathUtil.round(dayCount, currencyFraction));
		weekSalesAmount.put("salesTotal",  MathUtil.round(salesTotal, currencyFraction));
		weekSalesAmount.put("salesAvg", MathUtil.round( salesAvg, currencyFraction));
		return weekSalesAmount;
	}

	@Override
	public SingleMap getStoreSalesDetails(SingleMap param) {
		/*
		 * req : storeId
		 * response
		 * amount : 합계 		
		 *  	- salesTotal : 매출금액
		 *  	- discountTotal : 할인금액
		 *  	- realSalesTotal : 실매출액
		 * items : 세부 항목
		 * 		- salesId : 아이디
		 * 		- label : 시간 (11:20:01)
		 * 		- salesTotal : 매출금액
		 *  	- discountTotal : 할인금액
		 *  	- realSalesTotal : 실매출액 
		 * 	
		 */

		param.put("currencyFraction", commonService.getBrandFractionDigits(param));
		resolveSalesDetailServiceTypeParam(param);

		SingleMap result = new SingleMap();
		result.put("amount", detailSalesMapper.selectDetailTotal(param));
		result.put("items", detailSalesMapper.selectDetailList(param));

		return result;
	}

	/**
	 * servityType과 serviceId 를 기반으로 검새 파라미터를 설정 한다.
	 * 
	 * @param param
	 *            serviceType : 서비스 타입 (all, pos, online)
	 *            serviceId : 서비스ID
	 *            타입이 all인 경우 all
	 *            타입이 pos인 경우 POS_NO
	 *            타입이 online인 경우 미리 정해진 상수 (order type의 alias)
	 *            - order-type-order
	 *            - order-type-reservation
	 *            - order-type-contract
	 * 
	 * 
	 */
	private void resolveSalesDetailServiceTypeParam(SingleMap param) {
		String serviceType = param.getString("serviceType", null);
		String serviceId = param.getString("serviceId", null);

		// POS 검색
		if ("pos".equals(serviceType)) {
			param.put("pathType", "606001"); // 606001: pos
			param.put("posNo", serviceId);
			param.remove("orderType");
			return;
		}

		// 온라인 중 주문(self order), 에약(reservation), 계약(party contract) 별 조회
		if ("online".equals(serviceType)) {
			String orderType = commonMapper.selectBaseCodeByAlias("605", serviceId);
			param.put("pathType", "606002"); // 606002: mobile
			param.put("orderType", StringUtils.isEmpty(orderType) ? "-0" : orderType); // orderType이 없는 경우 검색되지 않도록 -0로 설정 
			param.remove("posNo");
			return;
		}

		// 전체 검색
		// service type == all 
		param.remove("pathType");
		param.remove("orderType");
		param.remove("posNo");
	}

	@Override
	public SingleMap getStoreSalesDetail(SingleMap param) {
		/*
		 * req : salesId
		 * response
		 * salesInfo : 세부 항목
		 * 		- salesId : 아이디
		 * 		- payDate : 시간 (11:20:01) 
		 * 		- orderNo : 주문번호 
		 * 		- sales : 매출금액
		 *  	- discount : 할인금액
		 *  	- realSales : 실매출액
		 *  	- tax : 부가세
		 *  	- netSales : 순매출금액
		 *  	- customerCount : 고객수
		 *  	- serviceType : 서비스 형태 (Online / Self order)
		 * payMethodSales : 결제 수단별 매출
		 * 		- cardSales : 카드 매출 
		 * 		- cashSales : 현금 매출 
		 * 		- otherSales : 기타 매출
		 * salesItem : 주문 상품 목록
		 * 		- amount : 합계
		 * 			- itemCount: 2
		 * 			- salesTotal : 10000
		 * 		- items : 주문 상품
		 * 			- label : item명
		 * 			- itemCount : 수량
		 * 			- salesTotal : 매출금액
		 */

		param.put("brandId", storeSalesMapper.selectSalesBrandIdSalesId(param));
		param.put("currencyFraction", commonService.getBrandFractionDigits(param));

		SingleMap salesItem = new SingleMap();
		salesItem.put("amount", detailSalesMapper.selectSalesItemTotal(param));
		salesItem.put("items", detailSalesMapper.selectSalesItemList(param));

		List<SingleMap> payMehtodSales = detailSalesMapper.selectSalesDetailPayMethodList(param);
		SingleMap salesInfo = detailSalesMapper.selectSalesDetail(param);
		if (salesInfo == null) {
			// 앱 요구사항으로 not null 하도록 함.
			salesInfo = new SingleMap();
		}

		SingleMap result = new SingleMap();
		result.put("salesInfo", salesInfo);
		result.put("payMethodSales", payMehtodSales);
		result.put("salesItem", salesItem);

		return result;
	}

	@Override
	public Page<SvcStoreSales> getStoreSalesList(SingleMap param) {
		Page<SvcStoreSales> page = new Page<>();
		final StaffUserDetail staffUserDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		param.put("brandId", staffUserDetail.getBrandId());
		param.put("storeId", staffUserDetail.getStoreId());
		param.put("pageLimit", (param.get("page") != null ? Integer.parseInt(param.getString("page")) : 1) * 10);
		
		SvcStoreSales storeSales = new SvcStoreSales();
		SvcStoreSales storeSalesTotal = storeSalesMapper.getStoreSalesTotal(param);
		if(storeSalesTotal.getTotalItemCount() > 0) {
			storeSales = storeSalesMapper.getStoreSales(param);
			storeSales.setTotalItemCount(storeSalesTotal.getTotalItemCount());
			storeSales.setTotalSales(storeSalesTotal.getTotalSales());
			storeSales.setTotalPageCount(storeSalesTotal.getTotalPageCount());
		}
		
		page.setPaginate(storeSales.getTotalPageCount(), param.getInt("page"),  param.getInt("limit"));
		page.setData(storeSales);
		
		return page;
	}
	

	@Override
	public SvcStoreSalesInfo getStoreSalesDtl(SingleMap param) {
		final StaffUserDetail staffUserDetail = AuthenticationUtils.getDetails(StaffUserDetail.class);
		
		param.put("brandId", staffUserDetail.getBrandId());
		param.put("storeId", staffUserDetail.getStoreId());
		param.put("orderId", param.getLong("orderId"));
		
		return storeSalesMapper.getStoreSalesDtl(param);
	}

}
