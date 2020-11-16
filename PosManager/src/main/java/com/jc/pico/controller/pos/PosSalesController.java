package com.jc.pico.controller.pos;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.SvcClosingExample;
import com.jc.pico.bean.SvcClosingExample.Criteria;
import com.jc.pico.bean.SvcOrder;
import com.jc.pico.bean.SvcOrderExample;
import com.jc.pico.mapper.SvcClosingMapper;
import com.jc.pico.mapper.SvcOrderMapper;
import com.jc.pico.service.pos.PosOrderService;
import com.jc.pico.utils.PosUtil;
import com.jc.pico.utils.bean.PosResult;
import com.jc.pico.utils.bean.PosSalesOrderInfo;
import com.jc.pico.utils.bean.PosSalesOrderInfoDetail;
import com.jc.pico.utils.bean.PosSalesOrderInfoDiscount;
import com.jc.pico.utils.bean.PosSalesOrderInfoHistory;
import com.jc.pico.utils.bean.PosSalesOrderInfoOption;
import com.jc.pico.utils.bean.PosSalesOrderMissing;
import com.jc.pico.utils.bean.PosSalesTableInfo;
import com.jc.pico.utils.bean.PosSalesTableOrder;
import com.jc.pico.utils.bean.PosSalesTableOrderDetail;
import com.jc.pico.utils.bean.PosSalesTableStatusSave;
import com.jc.pico.utils.bean.SingleMap;
import com.jc.pico.utils.customMapper.pos.PosOrderPaymentMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesOrderInfoDetailMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesOrderInfoDiscountMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesOrderInfoHistoryMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesOrderInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesOrderInfoOptionMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesOrderMissingMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesTableInfoMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesTableOrderDetailMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesTableOrderMapper;
import com.jc.pico.utils.customMapper.pos.PosSalesTableStatusSaveMapper;

/**
 * POS 연동부 중 주문정보 조회 컨트롤러
 * 
 * @author green
 *
 */
@RestController
@RequestMapping(value = "/api/pos/store")
public class PosSalesController {
	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SvcClosingMapper svcClosingMapper;

	@Autowired
	private PosSalesTableInfoMapper posSalesTableInfoMapper;

	@Autowired
	private PosSalesTableStatusSaveMapper posSalesTableStatusSaveMapper;

	@Autowired
	private PosSalesTableOrderMapper posSalesTableOrderMapper;

	@Autowired
	private PosSalesTableOrderDetailMapper posSalesTableOrderDetailMapper;

	@Autowired
	private SvcOrderMapper svcOrderMapper;

	@Autowired
	private PosSalesOrderInfoMapper posSalesOrderInfoMapper;

	@Autowired
	private PosSalesOrderInfoDetailMapper posSalesOrderInfoDetailMapper;

	@Autowired
	private PosSalesOrderInfoDiscountMapper posSalesOrderInfoDiscountMapper;

	@Autowired
	private PosSalesOrderInfoHistoryMapper posSalesOrderInfoHistoryMapper;

	@Autowired
	private PosSalesOrderMissingMapper posSalesOrderMissingMapper;

	@Autowired
	private PosSalesOrderInfoOptionMapper posSalesOrderInfoOptionMapper;

	@Autowired
	private PosOrderPaymentMapper posOrderPaymentMapper;

	@Autowired
	private PosOrderService posOrderService;

	@Autowired
	private PosUtil posUtil;
	
	private ObjectMapper objectMapper = new ObjectMapper();

	private DateFormat df = new SimpleDateFormat(PosUtil.DAY_FORMAT);

	/**
	 * POS 연동 (0. S_OPEN_INFO : 개점정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드</b></li>
	 *            <li><b style="color:#FF0F65;">OPEN_DATE_APC</b> String <b>개점일자(yyyymmdd)</b></li>
	 *            </ol>
	 * @Example {"CD_COMPANY": "5","CD_STORE": "23"}
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_OPEN_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sOpenInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		SingleMap param = null;

		Map<String, Object> result = new HashMap<String, Object>();

		String openDateApcStr = null;
		Long storeId = null;
		Long companyId = null;
		Long brandId = null;

		try {
			param = mapper.readValue(jsonString, SingleMap.class);
			openDateApcStr = param.getString("OPEN_DATE_APC", null);
//			String cdCompany = param.getString("CD_COMPANY"); // franId 
//			String cdStore = param.getString("CD_STORE"); // storeId
			Date openDateApc = StringUtils.isEmpty(openDateApcStr) ? null : param.getDate("OPEN_DATE_APC", "yyyyMMdd"); // 개점일자(yyyymmdd)			

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			brandId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_BRAND_ID) : 0L);

			// 2016.10.25 OPEN_DATE_APC 가 있으면 해당일 재개점 처리, 없으면 마감 처리 하도록 함.
			// 메인 포스의 메인 화면에 진입시 호출되며 
			// 포스가 오프라인인 상태에서 재개점/마감 했을때 발생함.

			// 해당 매장의 개점/폐점 정보를 읽어오기
			String orderByClause = "OPEN_DT DESC";
			SvcClosingExample svcClosingExample = new SvcClosingExample();
			svcClosingExample.setOrderByClause(orderByClause);
			Criteria svcClosingExampleCriteria = svcClosingExample.createCriteria() // 검색 조건
					.andBrandIdEqualTo(brandId) // 브랜드
					.andStoreIdEqualTo(storeId); // 상점

			if (openDateApc != null) { // 개점 처리

				svcClosingExampleCriteria.andOpenDtBetween(posUtil.getDateTime(openDateApc, 0, 0, 0, 0),
						posUtil.getDateTime(openDateApc, 23, 59, 59, 999));

				List<SvcClosing> svcClosings = svcClosingMapper.selectByExampleWithRowbounds(svcClosingExample, new RowBounds(0, 1));
				if (svcClosings.isEmpty()) { // 신규 개점 처리 					

					SvcClosing record = new SvcClosing();
					record.setBrandId(brandId);
					record.setStoreId(storeId);
					record.setOpenDt(openDateApc);
					record.setOpenTm(new Date());
					record.setIsClosing(false);
					svcClosingMapper.insertSelective(record);

				} else { // 재개점 처리					

					SvcClosing record = new SvcClosing();
					record.setId(svcClosings.get(0).getId());
					record.setIsClosing(false);
					record.setOpenTm(new Date());
					svcClosingMapper.updateByPrimaryKeySelective(record);
				}

			} else { // openDateApc 가 없으면 마감 처리

				// 가장 최근에 오픈된 정보를 마감으로 변경 처리 함
				List<SvcClosing> svcClosings = svcClosingMapper.selectByExampleWithRowbounds(svcClosingExample, new RowBounds(0, 1));
				if (!svcClosings.isEmpty()) { // 신규 개점 처리					
					SvcClosing record = new SvcClosing();
					record.setId(svcClosings.get(0).getId());
					record.setIsClosing(true);
					record.setCloseTm(new Date());
					svcClosingMapper.updateByPrimaryKeySelective(record);
				} else { // 미개점 상태에서 마감 요청이 들어오면 무시 (이미 마감 처리되어 있거나, 새로운 개점이 없음)

					logger.warn("Invalid close request. Cause by not opened. storeId={}", storeId);
				}
			}

			result.put("SUCCESS", true);
			result.put("RESULT_MSG", "Success");
			result.put("CD_COMPANY", companyId);
			result.put("CD_STORE", storeId);
			result.put("OPEN_DATE_POS", openDateApcStr);
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("SUCCESS", false);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
			result.put("CD_COMPANY", companyId);
			result.put("CD_STORE", storeId);
			result.put("OPEN_DATE_POS", openDateApcStr);
		}
		return result;
	}

	/**
	 * POS 연동 (1. S_RCP_PRINT : 주방/고객주문서 출력)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">PRINTER_CODE</b> String <b>프린터번호</b></li>
	 *            <li><b style="color:#FF0F65;">PRINT_DATA</b> String <b>출력내용</b></li>
	 *            </ol>
	 * @Example {"PRINTER_CODE": "prIntcOdE","PRINT_DATA": "prIntdAtA"}
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_RCP_PRINT", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sRcpPrint(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		String printerCode = null; // 프린터번호
		String printData = null; // 출력내용
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			printerCode = (String) jsonObject.get("PRINTER_CODE"); // 프린터번호
			printData = (String) jsonObject.get("PRINT_DATA"); // 출력내용

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

			// TODO Green, 프린트는 Green 작업영역에 포함되지 않으며 기획과 협의 후 진행하기로 함.
			result.put("SUCCESS", true);
			result.put("RESULT_MSG", "Success");
			result.put("PRINTER_CODE", "XYX");
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("SUCCESS", false);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
			result.put("PRINTER_CODE", "YXY");
		}
		return result;
	}

	/**
	 * POS 연동 (2. S_TABLE_MOVE : 테이블 이동)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">ORG_TABLE_NO</b> Integer <b>원 테이블 번호</b></li>
	 *            <li><b style="color:#FF0F65;">NEW_TABLE_NO</b> Integer <b>변경 테이블 번호</b></li>
	 *            <li><b style="color:#FF0F65;">NO_POS</b> String <b>주문기 포스 번호</b></li>
	 *            </ol>
	 * @Example {"ORG_TABLE_NO": 233,"NEW_TABLE_NO": 21,"NO_POS": "POS-001"}
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_TABLE_MOVE", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sTableMove(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Integer orgTableNo = null; // 원 테이블 번호
		Integer newTableNo = null; // 변경 테이블 번호
		String noPos = null; // 주문기 포스 번호
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			orgTableNo = (Integer) jsonObject.get("ORG_TABLE_NO"); // 원 테이블 번호
			newTableNo = (Integer) jsonObject.get("NEW_TABLE_NO"); // 변경 테이블 번호
			noPos = (String) jsonObject.get("NO_POS"); // 주문기 포스 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);

			// TODO Green, 테이블 이동 구현해야 함
			// 1. 새로운 테이블에 이미 주문 등이 있는 경우와 주문이 없는 경우에 생각해보아야 할 것이 많이 다르다.
			// 2. 새로운 테이블에 주문이 없는 경우에는 이전 테이블에 있는 ORDER_ID 를 새로운 테이블에 넣어주고 이전 테이블에서 삭제하면 끝
			// 3. 새로운 테이블에 주문이 있는 경우에는 이동이 아니라 합석으로 보아야 한다. (단말단에서도 처리하겠지만 서버단에서 한 번 더 검증)
			String errorCode = null;
			String errorMsg = null;
			Boolean isUsed = null;
			String posNo = null;
			BigInteger orderId = null;
			String adminId = null;

			// 옮겨 갈 새 테이블
			params.put("tableId", newTableNo);
			List<Map<String, Object>> newTables = posSalesTableStatusSaveMapper.selectCurrentTables(params);
			Map<String, Object> newTable = newTables != null ? newTables.get(0) : null;

			// org 테이블
			params.put("tableId", orgTableNo);
			List<Map<String, Object>> orgTables = posSalesTableStatusSaveMapper.selectCurrentTables(params);
			Map<String, Object> orgTable = orgTables != null ? orgTables.get(0) : null;

			if (newTable != null && newTable.size() > 0 && orgTable != null && orgTable.size() > 0) {
				isUsed = (Boolean) newTable.get("IS_USED");
				posNo = (String) newTable.get("POS_NO");
				orderId = (BigInteger) newTable.get("ORDER_ID");
				adminId = (String) newTable.get("ADMIN_ID");

				if (isUsed) {
					// 옮겨갈 테이블에 락이 걸려있으므로, 오류
					result.put("RESULT_MSG", "New table was used (orgTable=" + orgTableNo + ", newTable=" + newTableNo + ", noPos=" + noPos + ")");
					return result;
				}
				if (orderId != null && orderId.longValue() > 0) {
					// 옮겨갈 테이블에 주문이 있으므로 이동이 아닌 합석으로 처리해야 한다.
					result.put("RESULT_MSG", "New table had order(orderNo=" + orderId + ")");
					return result;
				}
				// org 테이블에 주문이 없으면 이동은 의미없음.
				orderId = (BigInteger) orgTable.get("ORDER_ID");
				if (orderId != null && orderId.longValue() > 0) {
					result.put("RESULT_MSG", "Org table had no order");
					return result;
				}

				params.put("updated", new Date()); // TODO Green, UTC로 변환하여 저장해야 하나?

				// 옮겨갈 테이블로 주문을 옮기고 org 테이블에서는 주문을 삭제한다.
				params.put("orderId", orderId);
				params.put("tableId", newTableNo);
				int updatedCount = posSalesTableStatusSaveMapper.updateCurrentTable(params);

				params.put("orderId", null);
				params.put("tableId", orgTableNo);
				updatedCount += posSalesTableStatusSaveMapper.updateCurrentTable(params);

				logger.debug("Success...[{}]: {}", updatedCount, params);

				result.put("SUCCESS", true);
				result.put("RESULT_MSG", "Success");
			} else {
				// 옮겨갈 테이블이나 org 테이블 정보가 없다면 이동요청이 잘못된 것.
				result.put("RESULT_MSG", "You've got the wrong table number (orgTable=" + orgTableNo + ", newTable=" + newTableNo + ")");
			}

		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}
		return result;
	}

	/**
	 * POS 연동 (3. S_TABLE_MERGE : 테이블 합석)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">ORG_TABLE_NO</b> Integer <b>원 테이블 번호</b></li>
	 *            <li><b style="color:#FF0F65;">NEW_TABLE_NO</b> Integer <b>변경 테이블 번호</b></li>
	 *            <li><b style="color:#FF0F65;">NO_POS</b> String <b>주문기 포스 번호</b></li>
	 *            </ol>
	 * @Example {"ORG_TABLE_NO": 233,"NEW_TABLE_NO": 21,"NO_POS": "POS-001"}
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_TABLE_MERGE", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sTableMerge(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Integer orgTableNo = null; // 원 테이블 번호
		Integer newTableNo = null; // 변경 테이블 번호
		String noPos = null; // 주문기 포스 번호
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			orgTableNo = (Integer) jsonObject.get("ORG_TABLE_NO"); // 원 테이블 번호
			newTableNo = (Integer) jsonObject.get("NEW_TABLE_NO"); // 변경 테이블 번호
			noPos = (String) jsonObject.get("NO_POS"); // 주문기 포스 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);

			// TODO Green, 테이블 이동 구현해야 함
			// 1. 새로운 테이블에 이미 주문 등이 있는 경우와 주문이 없는 경우에 생각해보아야 할 것이 많이 다르다.
			// 2. 새로운 테이블에 주문이 없는 경우에는 이전 테이블에 있는 ORDER_ID 를 새로운 테이블에 넣어주고 이전 테이블에서 삭제하면 끝
			// 3. 새로운 테이블에 주문이 있는 경우에는 이동이 아니라 합석으로 보아야 한다. (단말단에서도 처리하겠지만 서버단에서 한 번 더 검증)
			String errorCode = null;
			String errorMsg = null;
			Boolean isUsed = null;
			String posNo = null;
			BigInteger orderId = null;
			String adminId = null;

			// 옮겨 갈 새 테이블
			params.put("tableId", newTableNo);
			List<Map<String, Object>> newTables = posSalesTableStatusSaveMapper.selectCurrentTables(params);
			Map<String, Object> newTable = newTables != null ? newTables.get(0) : null;

			// org 테이블
			params.put("tableId", orgTableNo);
			List<Map<String, Object>> orgTables = posSalesTableStatusSaveMapper.selectCurrentTables(params);
			Map<String, Object> orgTable = orgTables != null ? orgTables.get(0) : null;

			if (newTable != null && newTable.size() > 0 && orgTable != null && orgTable.size() > 0) {
				isUsed = (Boolean) newTable.get("IS_USED");
				posNo = (String) newTable.get("POS_NO");
				orderId = (BigInteger) newTable.get("ORDER_ID");
				adminId = (String) newTable.get("ADMIN_ID");

				if (isUsed) {
					// 옮겨갈 테이블에 락이 걸려있으므로, 오류
					result.put("RESULT_MSG", "New table was used (orgTable=" + orgTableNo + ", newTable=" + newTableNo + ", noPos=" + noPos + ")");
					return result;
				}
				if (orderId != null && orderId.longValue() > 0) {
					// 옮겨갈 테이블에 주문이 있으므로 이동이 아닌 합석으로 처리해야 한다.
					result.put("RESULT_MSG", "New table had order(orderNo=" + orderId + ")");
					return result;
				}
				// org 테이블에 주문이 없으면 이동은 의미없음.
				orderId = (BigInteger) orgTable.get("ORDER_ID");
				if (orderId != null && orderId.longValue() > 0) {
					result.put("RESULT_MSG", "Org table had no order");
					return result;
				}

				params.put("updated", new Date()); // TODO Green, UTC로 변환하여 저장해야 하나?
				params.put("posNo", noPos);

				// 옮겨갈 테이블로 주문을 옮기고 org 테이블에서는 주문을 삭제한다.
				params.put("orderId", orderId);
				params.put("tableId", newTableNo);
				int updatedCount = posSalesTableStatusSaveMapper.updateCurrentTable(params);

				params.put("orderId", null);
				params.put("tableId", orgTableNo);
				updatedCount += posSalesTableStatusSaveMapper.updateCurrentTable(params);

				logger.debug("Success...[{}]: {}", updatedCount, params);

				result.put("SUCCESS", true);
				result.put("RESULT_MSG", "Success");
			} else {
				// 옮겨갈 테이블이나 org 테이블 정보가 없다면 이동요청이 잘못된 것.
				result.put("RESULT_MSG", "You've got the wrong table number (orgTable=" + orgTableNo + ", newTable=" + newTableNo + ")");
			}

		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}
		return result;
	}

	/**
	 * POS 연동 (4. S_TABLE_PARTY : 단체 지정)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">MASTER_TABLE_NO</b> Long <b>마스타 테이블 번호</b></li>
	 *            <li><b style="color:#FF0F65;">TABLES</b> List&lt;Map&lt;String, Long&gt;&gt; <b>대상
	 *            테이블 번호</b></li>
	 *            </ol>
	 * @Example
	 * 
	 *          <pre>
	 * {
	 *   "MASTER_TABLE_NO": 10,
	 *   "TABLES": [
	 *     {"TABLE_NO": 0},
	 *     {"TABLE_NO": 1}
	 *     ]
	 * }
	 *          </pre>
	 * 
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_TABLE_PARTY", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sTableParty(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long masterTableNo = null; // 마스타 테이블 번호
		List<Map<String, Long>> tables = null; // 대상 테이블 번호
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			masterTableNo = jsonObject.get("MASTER_TABLE_NO").getClass().equals(Long.class) ? (Long) jsonObject.get("MASTER_TABLE_NO")
					: (Integer) jsonObject.get("MASTER_TABLE_NO"); // 마스타 테이블 번호
			tables = (List<Map<String, Long>>) jsonObject.get("TABLES"); // 대상 테이블 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			params.put("masterTableNo", masterTableNo); // 단체로 묶일 마스터 테이블번호

			if (tables != null && tables.size() > 0) {
				params.put("tableIds", tables); // 단체로 묶일 대상 테이블번호 묶음

				int updatedCount = posSalesTableStatusSaveMapper.updateCurrentTable(params);
				logger.debug("Success...[{}]: {}", updatedCount, params);

				result.put("SUCCESS", true);
				result.put("RESULT_MSG", "Success");
			} else {
				result.put("RESULT_MSG", "There is No target tables");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}
		return result;
	}

	/**
	 * POS 연동 (5. S_TABLE_UNPARTY : 단체 해제)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">TABLE_NO</b> Long <b>테이블 번호</b></li>
	 *            </ol>
	 * @Example {"TABLE_NO": 5}
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_TABLE_UNPARTY", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sTableUnparty(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long tableNo = null; // 테이블 번호
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			tableNo = jsonObject.get("TABLE_NO").getClass().equals(Long.class) ? (Long) jsonObject.get("TABLE_NO")
					: (Integer) jsonObject.get("TABLE_NO"); // 테이블 번호

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			params.put("masterTableNo", null);
			params.put("tableId", tableNo);

			int updatedCount = posSalesTableStatusSaveMapper.updateCurrentTable(params);
			logger.debug("Success...[{}]: {}", updatedCount, params);

			result.put("SUCCESS", true);
			result.put("RESULT_MSG", "Success");
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}
		return result;
	}

	/**
	 * POS 연동 (6. S_TABLE_CUSTOMER : 고객수 변경)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">TABLE_NO</b> Long <b>테이블 번호</b></li>
	 *            <li><b style="color:#FF0F65;">CUSTOMER_COUNT</b> Integer <b>고객수</b></li>
	 *            </ol>
	 * @Example {"TABLE_NO": 233, "CUSTOMER_COUNT": 5}
	 * @return {@link Map} 성공/실패 여부
	 */
	@RequestMapping(value = "/S_TABLE_CUSTOMER", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> sTableCustomer(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		Long tableNo = null; // 테이블 번호
		Integer customerCount = null; // 고객수
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("SUCCESS", false); // 기본 값은 실패로...
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			tableNo = jsonObject.get("TABLE_NO").getClass().equals(Long.class) ? (Long) jsonObject.get("TABLE_NO")
					: (Integer) jsonObject.get("TABLE_NO"); // 테이블 번호
			customerCount = (Integer) jsonObject.get("CUSTOMER_COUNT"); // 고객수

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);

			// 해당 테이블에 주문이 있는 경우에만 고객수 변경 가능
			List<Map<String, Object>> currentTables = posSalesTableStatusSaveMapper.selectCurrentTables(params);
			if (currentTables != null && currentTables.size() > 0) {
				Map<String, Object> currentTable = currentTables != null ? currentTables.get(0) : null;

				Long orderId = currentTable.get("ORDER_ID") != null ? ((BigInteger) currentTable.get("ORDER_ID")).longValue() : null;
				if (orderId != null) {

					SvcOrderExample svcOrderExample = new SvcOrderExample();
					SvcOrderExample.Criteria svcOrderExampleCriteria = svcOrderExample.createCriteria();
					svcOrderExampleCriteria.andStoreIdEqualTo(storeId);
					svcOrderExampleCriteria.andIdEqualTo(orderId);

					SvcOrder svcOrder = svcOrderMapper.selectByPrimaryKey(orderId);
					svcOrder.setCustomerCnt(customerCount.shortValue());
					svcOrder.setUpdated(new Date());
					svcOrderMapper.updateByExampleSelective(svcOrder, svcOrderExample);

					result.put("SUCCESS", true);
					result.put("RESULT_MSG", "Success");
				} else {
					result.put("RESULT_MSG", "There is No order on table. tableId: " + tableNo);
				}
			} else {
				result.put("RESULT_MSG", "There is No table");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			result.put("RESULT_MSG", "Exception: " + e.getMessage());
		}
		return result;
	}

	/**
	 * POS 연동 (7. S_TABLE_INFO : 테이블 정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">NO_TABLE</b> Long <b>테이블번호 (0이면 전체 테이블 정보를 리턴하고 0
	 *            이상이면 해당 테이블정보를 리턴한다)</b></li>
	 *            <li><b style="color:#FF0F65;">NO_PARTITION</b> Long <b>파티션번호(고정값 0)</b></li>
	 *            </ol>
	 * @Example {"CD_COMPANY": 5,"CD_STORE": 23,"NO_TABLE": 0,"NO_PARTITION": 0}
	 * @return {@link PosResult} DATAS: {@link PosSalesTableInfo}
	 */
	@RequestMapping(value = "/S_TABLE_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult sTableInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		String cdCompany = null; // 회사코드(주문기일때 미사용)
		String cdStore = null; // 매장코드(주문기일때 미사용)
		Long noTable = null; // 테이블번호 (0이면 전체 테이블 정보를 리턴하고 0 이상이면 해당 테이블정보를 리턴한다)
		Long noPartition = null; // 파티션번호(고정값 0)
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			cdCompany = (String) jsonObject.get("CD_COMPANY"); // 회사코드(주문기일때 미사용)
			cdStore = (String) jsonObject.get("CD_STORE"); // 매장코드(주문기일때 미사용)
			noTable = jsonObject.get("NO_TABLE").getClass().equals(Long.class) ? (Long) jsonObject.get("NO_TABLE")
					: (Integer) jsonObject.get("NO_TABLE"); // 테이블번호 (0이면 전체 테이블 정보를 리턴하고 0 이상이면 해당 테이블정보를 리턴한다)
			noPartition = jsonObject.get("NO_TABLE").getClass().equals(Long.class) ? (Long) jsonObject.get("NO_PARTITION")
					: (Integer) jsonObject.get("NO_TABLE"); // 파티션번호(고정값 0)

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			if (noTable != null && noTable > 0) {
				params.put("tableId", noTable);
			}
			List<Object> posSalesTableInfos = posSalesTableInfoMapper.selectDefault(params);
			if (posSalesTableInfos != null && posSalesTableInfos.size() > 0) {
				int beansSize = posSalesTableInfos.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosSalesTableInfo posSalesTableInfo = (PosSalesTableInfo) posSalesTableInfos.get(inx);
					posSalesTableInfo.setDtInsert(posUtil.getStoreTime(localTimeZone, posSalesTableInfo.getDtInsert()));
					posSalesTableInfo.setDtUpdate(posUtil.getStoreTime(localTimeZone, posSalesTableInfo.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posSalesTableInfos);
			} else {
				logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posSalesTableInfos is Null or Size=0");
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(0);
				posResult.setDatas(posSalesTableInfos);
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosSalesTableInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (8. S_TABLE_STATUS_SAVE : 테이블 상태 변경)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">NO_TABLE</b> Long <b>테이블번호 (0이면 전체 테이블 정보를 리턴하고 0
	 *            이상이면 해당 테이블정보를 리턴한다)</b></li>
	 *            <li><b style="color:#FF0F65;">NO_PARTITION</b> Long <b>파티션번호(고정값 0)</b></li>
	 *            <li><b style="color:#FF0F65;">DS_STATUS</b> Integer <b>상태(0:미사용 1:사용중)</b></li>
	 *            <li><b style="color:#FF0F65;">NO_POS</b> String <b>주문기 포스 번호</b></li>
	 *            </ol>
	 * @Example {"CD_COMPANY": 5, "CD_STORE": 23, "NO_TABLE": 0, "NO_PARTITION": 0, "DS_STATUS": 1,
	 *          "NO_POS": "POS-001"}
	 * @return {@link PosResult} DATAS: {@link PosSalesTableStatusSave}
	 */
	@RequestMapping(value = "/S_TABLE_STATUS_SAVE", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult sTableStatusSave(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		SingleMap jsonObject = null;
		String cdCompany = null; // 회사코드(주문기일때 미사용)
		String cdStore = null; // 매장코드(주문기일때 미사용)
		Long noTable = null; // 테이블번호 (0이면 전체 테이블 정보를 리턴하고 0 이상이면 해당 테이블정보를 리턴한다)
		Long noPartition = null; // 파티션번호(고정값 0)
		Integer dsStatus = null; // 상태(0:미사용 1:사용중)
		String noPos = null; // 주문기 포스 번호
		PosResult posResult = new PosResult();
		try {
			
			jsonObject = objectMapper.readValue(jsonString, SingleMap.class);
			cdCompany = jsonObject.getString("CD_COMPANY"); // 회사코드(주문기일때 미사용)
			cdStore = jsonObject.getString("CD_STORE"); // 매장코드(주문기일때 미사용)
			noTable = jsonObject.getLong("NO_TABLE"); // 테이블번호 (0이면 전체 테이블 정보를 리턴하고 0 이상이면 해당 테이블정보를 리턴한다)
			noPartition = jsonObject.getLong("NO_PARTITION"); // 파티션번호(고정값 0)
			dsStatus = jsonObject.getInt("DS_STATUS"); // 상태(0:미사용 1:사용중)
			noPos = jsonObject.getString("NO_POS"); // 주문기 포스 번호
			final int dsForce = jsonObject.getInt("DS_FORCE", 0); // 0: 일반, 1:강제

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

			Map<String, Object> params = new HashMap<String, Object>();

			// S: 테이블정보에 다른 프로세스에서 알아볼 수 있도록 Lock 걸기
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put("tableId", noTable); // 특정 테이블의 정보 읽어오기
			List<Map<String, Object>> newTables = posSalesTableStatusSaveMapper.selectCurrentTables(params);
			Map<String, Object> result = newTables != null ? newTables.get(0) : null;

			boolean hasError = false;
			String errorCode = null;
			String errorMsg = null;
			Boolean isUsed = null;
			String posNo = null;
			BigInteger orderId = null;
			String adminId = null;
			if (result == null || result.size() == 0) {
				hasError = true;
			} else {
				isUsed = (Boolean) result.get("IS_USED");
				posNo = (String) result.get("POS_NO");
				orderId = (BigInteger) result.get("ORDER_ID");
				adminId = (String) result.get("ADMIN_ID");

				if (dsForce == 0 && isUsed && !StringUtils.equals(noPos, posNo)) {
					// 테이블 락이 걸려있으면서 내 POS_NO 가 아닌 경우... 오류!
					hasError = true;
					errorCode = PosUtil.EPO_0003_CODE;
					errorMsg = PosUtil.EPO_0003_MSG;
				} else if (noPos.equals(posNo)) {
					// 테이블 락이 걸려있는 것과 상관없이 마지막 POS_NO 가 나인 경우는 Warning.
					// 별로 할 건 없어 보임.
					logger.warn("Same noPos accessed Current SvcTable(cdStore=" + cdStore + ", noPos=" + noPos + ", noTable=" + noTable + ")");
				}
			}
			if (hasError) {
				// 오류로 Return
				logger.error("[{}][{}] {}", errorCode, errorMsg, "Current SvcTable(noTable=" + noTable + ")");
				posResult.setSuccess(false);
				posResult.setResultMsg("Current SvcTable(noTable=" + noTable + ") is NULL");
				return posResult;
			}
			params.put("isUsed", dsStatus);
			params.put("posNo", noPos);
			int updatedCount = posSalesTableStatusSaveMapper.updateCurrentTable(params);
			logger.debug("updatedCount: {}, params: {}, selected table: {}", updatedCount, params, result);
			// E: 테이블정보에 다른 프로세스에서 알아볼 수 있도록 Lock 걸기

			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<Object> posSalesTableStatusSaves = posSalesTableStatusSaveMapper.selectDefault(params);
			if (posSalesTableStatusSaves != null && posSalesTableStatusSaves.size() > 0) {
				int beansSize = posSalesTableStatusSaves.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosSalesTableStatusSave posSalesTableStatusSave = (PosSalesTableStatusSave) posSalesTableStatusSaves.get(inx);
					posSalesTableStatusSave.setDtUpdate(posUtil.getStoreTime(localTimeZone, posSalesTableStatusSave.getDtUpdate()));
				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success: dsStatus was updated to [" + dsStatus + "] rows=" + updatedCount);
				posResult.setRows(beansSize);
				posResult.setDatas(posSalesTableStatusSaves);
			} else {
				logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posSalesTableStatusSaves is Null or Size=0");
				posResult.setSuccess(false);
				posResult.setResultMsg(PosSalesTableStatusSave.class.getSimpleName() + " is NULL");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosSalesTableStatusSave.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (9. S_TABLE_ORDER : 테이블 주문정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드(주문기일때 미사용)</b></li>
	 *            </ol>
	 * @Example {"CD_COMPANY": 5, "CD_STORE": 23}
	 * @return {@link PosResult} DATAS: {@link PosSalesTableOrder}
	 */
	@RequestMapping(value = "/S_TABLE_ORDER", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult sTableOrder(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		String cdCompany = null; // 회사코드(주문기일때 미사용)
		String cdStore = null; // 매장코드(주문기일때 미사용)
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			cdCompany = (String) jsonObject.get("CD_COMPANY"); // 회사코드(주문기일때 미사용)
			cdStore = (String) jsonObject.get("CD_STORE"); // 매장코드(주문기일때 미사용)

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<PosSalesTableOrder> posSalesTableOrders = posSalesTableOrderMapper.selectDefault(params);
			if (posSalesTableOrders != null && posSalesTableOrders.size() > 0) {
				int beansSize = posSalesTableOrders.size();

				TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

				for (int inx = 0; inx < beansSize; inx++) {
					PosSalesTableOrder posSalesTableOrder = (PosSalesTableOrder) posSalesTableOrders.get(inx);
					posSalesTableOrder.setDtAdmission(posUtil.getStoreTime(localTimeZone, posSalesTableOrder.getDtAdmission()));
					posSalesTableOrder.setDtInsert(posUtil.getStoreTime(localTimeZone, posSalesTableOrder.getDtInsert()));
					posSalesTableOrder.setDtUpdate(posUtil.getStoreTime(localTimeZone, posSalesTableOrder.getDtUpdate()));

					// S: 상세 정보 추가하기 (주문당 주문 아이템 리스트 가져오기)
					params.put("orderId", posSalesTableOrder.getNoOrder());

					List<PosSalesTableOrderDetail> posSalesTableOrderDetails = posSalesTableOrderDetailMapper.selectDefault(params);
					if (posSalesTableOrderDetails != null && posSalesTableOrderDetails.size() > 0) {
						int detailBeansSize = posSalesTableOrderDetails.size();

						for (int jnx = 0; jnx < detailBeansSize; jnx++) {
							PosSalesTableOrderDetail posSalesTableOrderDetail = (PosSalesTableOrderDetail) posSalesTableOrderDetails.get(jnx);
							posSalesTableOrderDetail.setDtInsert(posUtil.getStoreTime(localTimeZone, posSalesTableOrderDetail.getDtInsert()));
							posSalesTableOrderDetail.setDtUpdate(posUtil.getStoreTime(localTimeZone, posSalesTableOrderDetail.getDtUpdate()));
						}

						posSalesTableOrder.setDetail(posSalesTableOrderDetails);
					} else {
						logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posSalesTableOrderDetails is Null or Size=0");
					}
					// E: 상세 정보 추가하기 (주문당 주문 아이템 리스트 가져오기)

				}
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posSalesTableOrders);
			} else {
				logger.warn("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posSalesTableOrders is Null or Size=0");
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(0);
				posResult.setDatas(posSalesTableOrders);
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosSalesTableOrder.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (10. S_ORDER_INFO : 주문정보)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">YMD_ORDER</b> String <b>주문일자(yyyymmdd)
	 *            (예약시작일자)</b></li>
	 *            <li><b style="color:#FF0F65;">YMD_ORDER_END</b> String <b>주문일자(yyyymmdd) (예약종료일자)
	 *            예약에서만 사용</b></li>
	 *            <li><b style="color:#FF0F65;">NO_ORDER</b> Long <b>주문번호</b></li>
	 *            <li><b style="color:#FF0F65;">CD_ORDER</b> String
	 *            <b>주문유형(605001:주문,605002:예약,605001:계약)</b></li>
	 *            </ol>
	 * @Example {"CD_COMPANY": "5", "CD_STORE": "23", "YMD_ORDER": "", "YMD_ORDER_END": "",
	 *          "NO_ORDER": 0, "CD_ORDER": ""}
	 * @return {@link PosResult} DATAS: {@link PosSalesOrderInfo}
	 */
	@RequestMapping(value = "/S_ORDER_INFO", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult sOrderInfo(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> jsonObject = null;
		String cdCompany = null; // 회사코드(주문기일때 미사용)
		String cdStore = null; // 매장코드(주문기일때 미사용)
		String ymdOrder = null; // 주문일자(yyyymmdd) (예약시작일자)
		String ymdOrderEnd = null; // 주문일자(yyyymmdd) (예약종료일자) 예약에서만 사용
		Long noOrder = null; // 주문번호
		String cdOrderType = null; // 주문유형(605001:주문,605002:예약,605001:계약)
		PosResult posResult = new PosResult();
		try {
			jsonObject = mapper.readValue(jsonString, Map.class);
			cdCompany = (String) jsonObject.get("CD_COMPANY"); // 회사코드(주문기일때 미사용)
			cdStore = (String) jsonObject.get("CD_STORE"); // 매장코드(주문기일때 미사용)
			ymdOrder = (String) jsonObject.get("YMD_ORDER"); // 주문일자(yyyymmdd)
			ymdOrderEnd = (String) jsonObject.get("YMD_ORDER_END"); // 주문일자(yyyymmdd)
			noOrder = jsonObject.get("NO_ORDER").getClass().equals(Long.class) ? (Long) jsonObject.get("NO_ORDER")
					: (Integer) jsonObject.get("NO_ORDER"); // 주문번호
			cdOrderType = (String) jsonObject.get("CD_ORDER_TYPE"); // 주문유형(605001:주문,605002:예약,605001:계약)

			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);

			// ymdOrder 에 대해 해당 일자의 모든 데이터를 읽어가기 위해 시작/종료 일시 지정 (ymdOrder 의 포맷이 잘못된 경우 오류)
			if (ymdOrder == null || !ymdOrder.matches("[0-9]{8}")) {
				posResult.setSuccess(false);
				posResult.setResultMsg(PosSalesOrderInfo.class.getSimpleName() + " .YMD_ORDER was not formatted 'yyyyMMdd'='" + ymdOrder + "'");
				return posResult;
			}
			TimeZone localTimeZone = posUtil.getStoreTimeZone(storeId); // 로컬 매장의 TimeZone

			// FIXME Green, 받은 시간을 UTC 로 치환 (실제 DB 시간기록 부분과 비교해보아야 함, 굳이 할 필요가 없을지도...)
			String startDtOpen = ymdOrder.substring(0, 4) + "-" + ymdOrder.substring(4, 6) + "-" + ymdOrder.substring(6, 8) + " 00:00:00";
			// 종료일 값이 있으면 해당 값 사용, 없으면 시작일을 종료일로 하여 당일 검색되도록 함
			String endDtOpen;
			if (ymdOrderEnd != null && !ymdOrderEnd.isEmpty()) {
				endDtOpen = ymdOrderEnd.substring(0, 4) + "-" + ymdOrderEnd.substring(4, 6) + "-" + ymdOrderEnd.substring(6, 8) + " 23:59:59";
			} else {
				endDtOpen = ymdOrder.substring(0, 4) + "-" + ymdOrder.substring(4, 6) + "-" + ymdOrder.substring(6, 8) + " 23:59:59";
			}

			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			params.put("startDtOpen", startDtOpen); // 주문일자 시작시간 (해당 일자의 0시 0분 0초)
			params.put("endDtOpen", endDtOpen); // 주문일자 종료시간 (해당 일자의 23시 59분 59초)
			if (noOrder != 0) { // 0 일 경우 전체 검색.
				params.put("noOrder", noOrder);
			}

			List<PosSalesOrderInfo> posSalesOrderInfos = posSalesOrderInfoMapper.selectDefault(params);
			if (posSalesOrderInfos.size() > 0) {
				int beansSize = posSalesOrderInfos.size();

				params.remove("startDtOpen");
				params.remove("endDtOpen");
				for (int inx = 0; inx < beansSize; inx++) {
					PosSalesOrderInfo posSalesOrderInfo = posSalesOrderInfos.get(inx);

					posSalesOrderInfo.setCdOrderStatus(posUtil.getPosCode(posSalesOrderInfo.getCdOrderStatus(), 0));
					posSalesOrderInfo.setCdMemberType(posUtil.getPosCode(posSalesOrderInfo.getCdMemberType(), 0));
					posSalesOrderInfo.setCdMemberAge(posUtil.getPosCode(posSalesOrderInfo.getCdMemberAge(), 0));
					posSalesOrderInfo.setCdMemberSex(posUtil.getPosCode(posSalesOrderInfo.getCdMemberSex(), 0));

					// Order 마다 아이템 정보를 DETAIL 로 읽어가기
					params.put("orderId", posSalesOrderInfo.getOrderId());
					List<PosSalesOrderInfoDetail> posSalesOrderInfoDetails = posSalesOrderInfoDetailMapper.selectDefault(params);
					if (posSalesOrderInfoDetails != null && posSalesOrderInfoDetails.size() > 0) {

						for (PosSalesOrderInfoDetail posSalesOrderInfoDetail : posSalesOrderInfoDetails) {

							posSalesOrderInfoDetail.setCdOrderStatus(posUtil.getPosCode(posSalesOrderInfoDetail.getCdOrderStatus(), 0));

							// 주문 품목별로 할인 리스트 세팅
							Long itemId = posSalesOrderInfoDetail.getOrderItemId();
							params.put("itemId", itemId);
							List<PosSalesOrderInfoDiscount> posSalesOrderInfoDiscounts = posSalesOrderInfoDiscountMapper.selectDefault(params);
							posSalesOrderInfoDetail.setDiscount(posSalesOrderInfoDiscounts);

							// 주문 품목별로 히스토리 리스트 세팅
							params.put("itemId", itemId);
							List<PosSalesOrderInfoHistory> posSalesOrderInfoHistories = posSalesOrderInfoHistoryMapper.selectDefault(params);
							posSalesOrderInfoDetail.setHistory(posSalesOrderInfoHistories);

							// 주문 옵션 정보 추가
							params.put("itemId", itemId);
							List<PosSalesOrderInfoOption> posSalesOrderInfoOptions = posSalesOrderInfoOptionMapper.selectDefault(params);
							posSalesOrderInfoDetail.setOptions(posSalesOrderInfoOptions);
						}

						// Order 마다 Detail 이 있기 때문에 Loop를 돌며 Detail을 Setting 한다.(Order Items)
						posSalesOrderInfo.setDetail(posSalesOrderInfoDetails);
					}

					// 결제 정보 추가					
					posSalesOrderInfo.setPayments(posOrderPaymentMapper.selectListByOrderId(posSalesOrderInfo.getOrderId()));
				}

			}

			/**
			 * 포스에서 해당 정보를 조회해 갔음을 표시한다.
			 * clerk/tab의 경우는 포스에서 mqtt 수신후 주문 정보를 조회해 갈때까지 대기하므로
			 * 여기서 상태를 승인으로 변경하여 주문 완료 처리 할 수 있도록 한다
			 */
			for (PosSalesOrderInfo orderInfo : posSalesOrderInfos) {
				posOrderService.confirmOrder(orderInfo);
			}

			posResult.setSuccess(true);
			posResult.setResultMsg("Success");
			posResult.setRows(posSalesOrderInfos.size());
			posResult.setDatas(posSalesOrderInfos);
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosSalesOrderInfo.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}

	/**
	 * POS 연동 (12. S_ORDER_MISSING : 미 수신 주문 정보 조회)
	 * 
	 * @param jsonString
	 *            "PARAM": 통합 파라미터
	 *            <ol style="line-height: 1.5em;">
	 *            <li><b style="color:#FF0F65;">CD_COMPANY</b> String <b>회사코드(주문기일때 미사용)</b></li>
	 *            <li><b style="color:#FF0F65;">CD_STORE</b> String <b>매장코드(주문기일때 미사용)</b></li>
	 *            </ol>
	 * @Example {"CD_COMPANY": "", "CD_STORE": ""}
	 * @return {@link PosResult} DATAS: {@link PosSalesOrderMissing}
	 */
	@RequestMapping(value = "/S_ORDER_MISSING", method = { RequestMethod.GET, RequestMethod.POST })
	public PosResult sOrderMissing(@RequestParam(value = "PARAM") String jsonString // 통합 파라미터
	) {
		ObjectMapper mapper = new ObjectMapper();
		String cdCompany = null; // 회사코드(주문기일때 미사용)
		String cdStore = null; // 매장코드(주문기일때 미사용)
		PosResult posResult = new PosResult();
		try {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			String username = (authentication != null ? authentication.getName() : null);
			Map<String, Object> posBaseKeyMap = posUtil.getPosBasicInfo(username); // 매장/POS 정보키 맵

			Long storeId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_STORE_ID) : 0L);
			Long companyId = (Long) (posBaseKeyMap != null ? posBaseKeyMap.get(PosUtil.BASE_INFO_COMPANY_ID) : 0L);
			Map<String, Object> params = new HashMap<String, Object>();
			params.put(PosUtil.BASE_INFO_STORE_ID, storeId);
			params.put(PosUtil.BASE_INFO_COMPANY_ID, companyId);
			List<PosSalesOrderMissing> posSalesOrderMissings = posSalesOrderMissingMapper.selectDefault(params);
			if (posSalesOrderMissings != null && posSalesOrderMissings.size() > 0) {
				int beansSize = posSalesOrderMissings.size();
				posResult.setSuccess(true);
				posResult.setResultMsg("Success");
				posResult.setRows(beansSize);
				posResult.setDatas(posSalesOrderMissings);
			} else {
				logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, "posSalesOrderMissings is Null or Size=0");
				posResult.setSuccess(false);
				posResult.setResultMsg(PosSalesOrderMissing.class.getSimpleName() + " is NULL");
			}
		} catch (Exception e) {
			logger.error("[{}][{}] {}", PosUtil.EPO_0000_CODE, PosUtil.EPO_0000_MSG, e.getMessage(), e);
			posResult.setSuccess(false);
			posResult.setResultMsg(PosSalesOrderMissing.class.getSimpleName() + " has Exceptions... " + e.getMessage());
		}
		return posResult;
	}
}
