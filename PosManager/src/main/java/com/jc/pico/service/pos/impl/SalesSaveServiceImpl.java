package com.jc.pico.service.pos.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jc.pico.bean.SvcSales;
import com.jc.pico.bean.SvcSalesDiscount;
import com.jc.pico.bean.SvcSalesDiscountExample;
import com.jc.pico.bean.SvcSalesItem;
import com.jc.pico.bean.SvcSalesItemExample;
import com.jc.pico.bean.SvcSalesItemOpt;
import com.jc.pico.bean.SvcSalesItemOptExample;
import com.jc.pico.bean.SvcSalesPay;
import com.jc.pico.bean.SvcSalesPayExample;
import com.jc.pico.mapper.SvcSalesDiscountMapper;
import com.jc.pico.mapper.SvcSalesItemMapper;
import com.jc.pico.mapper.SvcSalesItemOptMapper;
import com.jc.pico.mapper.SvcSalesMapper;
import com.jc.pico.mapper.SvcSalesPayMapper;
import com.jc.pico.service.pos.SalesSaveService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.bean.SvcSalesExtended;
import com.jc.pico.utils.bean.SvcSalesItemExtended;
import com.jc.pico.utils.customMapper.pos.PosSalesSaleSaveMapper;

@Service
public class SalesSaveServiceImpl implements SalesSaveService {

	private static final Logger logger = LoggerFactory.getLogger(SalesSaveServiceImpl.class);

	@Autowired
	private SvcSalesMapper svcSalesMapper;

	@Autowired
	private SvcSalesItemMapper svcSalesItemMapper;

	@Autowired
	private SvcSalesItemOptMapper svcSalesItemOptMapper;

	@Autowired
	private SvcSalesDiscountMapper svcSalesDiscountMapper;

	@Autowired
	private SvcSalesPayMapper svcSalesPayMapper;

	@Autowired
	private PosSalesSaleSaveMapper posSalesSaleSaveMapper;

	@Override
	@Transactional
	public String saveSales(SvcSalesExtended sales, SvcSales oldSales) throws Throwable {

		// 1. Validation
		if (sales == null || sales.getStoreId() == null) {
			logger.error("[{}][{}] StoreID empty. storeId: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, sales.getStoreId());
			throw new Exception("StoreId empty. storeId: " + sales.getStoreId());
		}
		if (sales == null || sales.getSvcSalesItems() == null) {
			logger.error("[{}][{}] Sales or Items are empty. sales: {}, items: {}", PosUtil.EPO_0005_CODE, PosUtil.EPO_0005_MSG, sales,
					sales.getSvcSalesItems());
			throw new Exception("Sales or Items are empty. sales: " + sales.getOrderNo() + ", items: " + sales.getSvcSalesItems());
		}

		// 입력 값 전처리
		sales.setCreated(null); // db에서 자동 부여
		sales.setUpdated(null); // db에서 자동 부여

		// 3. TB_SVC_SALES 생성 (이미 있는 경우 수정)
		if (oldSales == null) { // 신규
			svcSalesMapper.insertSelective(sales);
		} else { // 수정
			sales.setId(oldSales.getId());
			svcSalesMapper.updateByPrimaryKeySelective(sales);
		}

		// 4. 기존에 존재하는 모든 매출 상품과 할인 정보, 결제수단 정보들을 삭제
		// 매출에 딸린 할인들 삭제
		SvcSalesDiscountExample svcSalesDiscountExample = new SvcSalesDiscountExample();
		svcSalesDiscountExample.createCriteria().andSalesIdEqualTo(sales.getId());
		svcSalesDiscountMapper.deleteByExample(svcSalesDiscountExample);

		// 매출에 딸린 결제들 삭제
		SvcSalesPayExample svcSalesPayExample = new SvcSalesPayExample();
		svcSalesPayExample.createCriteria().andSalesIdEqualTo(sales.getId());
		svcSalesPayMapper.deleteByExample(svcSalesPayExample);

		// 매출에 딸린 메뉴 옵션들 삭제
		SvcSalesItemOptExample svcSalesItemOptExample = new SvcSalesItemOptExample();
		svcSalesItemOptExample.createCriteria().andSalesIdEqualTo(sales.getId());
		svcSalesItemOptMapper.deleteByExample(svcSalesItemOptExample);

		// 5. 매출 상품 리스트 저장 (존재하면 갱신, 없으면 등록)		
		fillItemAndCategoryInfo(sales.getSvcSalesItems()); // 매출 상품의 상품 및 카테고리 관련 코드 정보 채운다 (통계에 사용)
		SvcSalesItemExample salesItemExample = new SvcSalesItemExample();
		for (SvcSalesItemExtended item : sales.getSvcSalesItems()) {

			item.setSalesId(sales.getId());

			salesItemExample.clear(); // 재사용을 위해 조건 초기화
			salesItemExample.createCriteria() // 갱신 조건은 POS PK
					.andSalesIdEqualTo(item.getSalesId()) // sales id 별로 
					.andOrdinalEqualTo(item.getOrdinal()); // pos seq

			List<SvcSalesItem> oldSalesItems = svcSalesItemMapper.selectByExample(salesItemExample);
			if (oldSalesItems.isEmpty()) {
				// tb_svc_sales_item
				svcSalesItemMapper.insertSelective(item);
			} else {
				item.setId(oldSalesItems.get(0).getId());
				// tb_svc_sales_item
				svcSalesItemMapper.updateByPrimaryKeySelective(item);
			}

			// 6. 매출 상품 옵션 저장
			// tb_svc_sales_item_opt
			saveSalesItemOpts(item);
		}

		// 7. 매출 할인 리스트 저장
		for (SvcSalesDiscount svcSalesDiscount : sales.getSvcSalesDiscounts()) {
			svcSalesDiscount.setSalesId(sales.getId());
			/// tb_svc_sales_discount
			svcSalesDiscountMapper.insertSelective(svcSalesDiscount);
		}

		// 8. 매출 결제 리스트 저장
		for (SvcSalesPay svcSalesPay : sales.getSvcSalesPays()) {
			svcSalesPay.setSalesId(sales.getId());
			// tb_svc_sales_pay
			svcSalesPayMapper.insertSelective(svcSalesPay);
		}

		return String.valueOf(sales.getId());
	}

	/**
	 * 매출이 발생항 항목에서 상품과 카테고리 정보가 누락되어 있으면 채운다.
	 * 상품 이름, 타입, 코드
	 * 카테고리 이름, 코드
	 * 
	 * @param items
	 */
	private void fillItemAndCategoryInfo(List<SvcSalesItemExtended> items) {
		List<Long> itemIds = new ArrayList<>(items.size());
		for (SvcSalesItem item : items) {
			itemIds.add(item.getItemId());
		}

		if (itemIds.isEmpty()) {
			return;
		}

		List<SingleMap> itemCatInfos = posSalesSaleSaveMapper.selectItemAndCatInfos(itemIds);

		for (SvcSalesItem item : items) {
			SingleMap info = SingleMap.search(itemCatInfos, "itemId", item.getItemId());

			if (info == null) {
				continue;
			}
			if (StringUtils.isEmpty(item.getItemNm())) {
				item.setItemNm(info.getString("itemNm", ""));
			}
			if (StringUtils.isEmpty(item.getItemCd())) {
				item.setItemCd(info.getString("itemCd", ""));
			}
			if (StringUtils.isEmpty(item.getItemTp())) {
				item.setItemTp(info.getString("itemTp", ""));
			}
			if (StringUtils.isEmpty(item.getCatCd())) {
				item.setCatCd(info.getString("catCd", ""));
			}
			if (StringUtils.isEmpty(item.getCatNm())) {
				item.setCatNm(info.getString("catNm", ""));
			}
		}
	}

	/**
	 * 매출 상품 옵션 저장
	 * 
	 * @param item
	 *            매출 상품
	 */
	private void saveSalesItemOpts(SvcSalesItemExtended item) {

		logger.debug("saveSalesItemOpts size=" + item.getOptions().size());

		for (SvcSalesItemOpt itemOpt : item.getOptions()) {
			itemOpt.setSalesId(item.getSalesId());
			itemOpt.setItemId(item.getId());
			svcSalesItemOptMapper.insertSelective(itemOpt);
		}
	}

}
