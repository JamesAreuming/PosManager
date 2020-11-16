package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 10. S_ORDER_INFO : 주문정보
 * 11. S_ORDER_SAVE : 주문정보 저장
 * 2016. 8. 13, green, create
 * 2016. 9. 7, hyo, 주문 조회/저장 bean 통합
 */
public class PosSalesOrderInfoDetail implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "SEQ")
	private Long seq;

	/**
	 * 주문상품 ID (내부 사용, 포스와 연동하지 않음)
	 */
	private Long orderItemId;

	/**
	 * 주문상태코드(1.변동없음,2.신규,3.수정(수량변경),4.삭제)
	 */
	@JsonProperty(value = "CD_ORDER_STATUS")
	private Integer cdOrderStatus;

	/**
	 * 주문유형(605001:주문,605002:예약,605003:계약)
	 */
	@JsonProperty(value = "CD_ORDER_PATH")
	private Integer cdOrderPath;

	/**
	 * 판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
	 */
	@JsonProperty(value = "DS_SALE")
	private Integer dsSale;
	/**
	 * 판매유형 (0: 일반 1: 고정셋트 3: 코스 5: 0원단가변동(금액형) 6: 중량형)
	 */
	@JsonProperty(value = "CD_SALE_TYPE")
	private Integer cdSaleType;
	/**
	 * 셋트구성품여부
	 */
	@JsonProperty(value = "YN_SET_SUB")
	private Integer ynSetSub;
	/**
	 * 셋트코스그룹번호
	 */
	@JsonProperty(value = "NO_GROUP")
	private Long noGroup;
	/**
	 * 셋트단위수량
	 */
	@JsonProperty(value = "QTY_ITEM")
	private Integer qtyItem;
	/**
	 * 통계사업단위
	 */
	@JsonProperty(value = "CD_STORE_MNG")
	private String cdStoreMng;
	/**
	 * 품번
	 */
	@JsonProperty(value = "CD_GOODS")
	private String cdGoods;
	/**
	 * 품명
	 */
	@JsonProperty(value = "NM_GOODS")
	private String nmGoods;
	/**
	 * 매출과세구분
	 */
	@JsonProperty(value = "DS_TAX")
	private Integer dsTax;
	/**
	 * 매입단가
	 */
	@JsonProperty(value = "PR_BUY")
	private Double prBuy;
	/**
	 * 판매단가
	 */
	@JsonProperty(value = "PR_SALE")
	private Double prSale;
	/**
	 * 주문수량
	 */
	@JsonProperty(value = "QTY_ORDER")
	private Integer qtyOrder;
	/**
	 * 주문금액
	 */
	@JsonProperty(value = "AMT_ORDER")
	private Double amtOrder;

	/**
	 * 옵션 단가 합계
	 */
	@JsonProperty(value = "PR_OPTIONS")
	private Double prOptions;

	/**
	 * 공급가
	 */
	@JsonProperty(value = "AMT_SUPPLY")
	private Double amtSupply;
	/**
	 * 부가세
	 */
	@JsonProperty(value = "AMT_TAX")
	private Double amtTax;
	/**
	 * 할인금액
	 */
	@JsonProperty(value = "AMT_DC")
	private Double amtDc;
	/**
	 * 메모
	 */
	@JsonProperty(value = "MEMO")
	private String memo;
	/**
	 * 비매출 사유
	 */
	@JsonProperty(value = "CD_NO_SALE_REASON")
	private Integer cdNoSaleReason;
	/**
	 * 주문일시
	 */
	@JsonProperty(value = "DT_ORDER")
	private String dtOrder;
	/**
	 * 포장여부
	 */
	@JsonProperty(value = "YN_PACKING")
	private Integer ynPacking;
	/**
	 * 기프트카드번호
	 */
	@JsonProperty(value = "NO_GIFT_CARD")
	private String noGiftCard;
	/**
	 * 등록일시
	 */
	@JsonProperty(value = "DT_INSERT")
	private String dtInsert;
	/**
	 * 등록자
	 */
	@JsonProperty(value = "CD_EMPLOYEE_INSERT")
	private String cdEmployeeInsert;
	/**
	 * 수정일시
	 */
	@JsonProperty(value = "DT_UPDATE")
	private String dtUpdate;
	/**
	 * 수정자
	 */
	@JsonProperty(value = "CD_EMPLOYEE_UPDATE")
	private String cdEmployeeUpdate;
	/**
	 * 할인 내역
	 */
	@JsonProperty(value = "DISCOUNT")
	private List<PosSalesOrderInfoDiscount> discount;
	/**
	 * 주문 변동 이력
	 */
	@JsonProperty(value = "HISTORY")
	private List<PosSalesOrderInfoHistory> history;

	/**
	 * 주문 옵션
	 */
	@JsonProperty(value = "OPTIONS")
	private List<PosSalesOrderInfoOption> options;

	/**
	 * Getter 일련번호
	 */
	public Long getSeq() {
		return seq;
	}

	/**
	 * Setter 일련번호
	 * 
	 * @param seq
	 *            일련번호
	 */
	public void setSeq(Long seq) {
		this.seq = seq;
	}

	public Long getOrderItemId() {
		return orderItemId;
	}

	public void setOrderItemId(Long orderItemId) {
		this.orderItemId = orderItemId;
	}

//	/**
//	 * Getter 주문취소여부
//	 */
//	public Integer getDsOrder() {
//		return dsOrder;
//	}
//	/**
//	 * Setter 주문취소여부
//	 * @param dsOrder 주문취소여부
//	 */
//	public void setDsOrder(Integer dsOrder) {
//		this.dsOrder = dsOrder;
//	}
	/**
	 * Getter 판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
	 */
	public Integer getDsSale() {
		return dsSale;
	}

	/**
	 * Setter 판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
	 * 
	 * @param dsSale
	 *            판매형태 (0:일반, 1:폐기, 2:서비스, 3:자가소비)
	 */
	public void setDsSale(Integer dsSale) {
		this.dsSale = dsSale;
	}

	/**
	 * Getter 판매유형 (0: 일반 1: 고정셋트 3: 코스 5: 0원단가변동(금액형) 6: 중량형)
	 */
	public Integer getCdSaleType() {
		return cdSaleType;
	}

	/**
	 * Setter 판매유형 (0: 일반 1: 고정셋트 3: 코스 5: 0원단가변동(금액형) 6: 중량형)
	 * 
	 * @param cdSaleType
	 *            판매유형 (0: 일반 1: 고정셋트 3: 코스 5: 0원단가변동(금액형) 6: 중량형)
	 */
	public void setCdSaleType(Integer cdSaleType) {
		this.cdSaleType = cdSaleType;
	}

	/**
	 * Getter 셋트구성품여부
	 */
	public Integer getYnSetSub() {
		return ynSetSub;
	}

	/**
	 * Setter 셋트구성품여부
	 * 
	 * @param ynSetSub
	 *            셋트구성품여부
	 */
	public void setYnSetSub(Integer ynSetSub) {
		this.ynSetSub = ynSetSub;
	}

	/**
	 * Getter 셋트코스그룹번호
	 */
	public Long getNoGroup() {
		return noGroup;
	}

	/**
	 * Setter 셋트코스그룹번호
	 * 
	 * @param noGroup
	 *            셋트코스그룹번호
	 */
	public void setNoGroup(Long noGroup) {
		this.noGroup = noGroup;
	}

	/**
	 * Getter 셋트단위수량
	 */
	public Integer getQtyItem() {
		return qtyItem;
	}

	/**
	 * Setter 셋트단위수량
	 * 
	 * @param qtyItem
	 *            셋트단위수량
	 */
	public void setQtyItem(Integer qtyItem) {
		this.qtyItem = qtyItem;
	}

	/**
	 * Getter 통계사업단위
	 */
	public String getCdStoreMng() {
		return cdStoreMng;
	}

	/**
	 * Setter 통계사업단위
	 * 
	 * @param cdStoreMng
	 *            통계사업단위
	 */
	public void setCdStoreMng(String cdStoreMng) {
		this.cdStoreMng = cdStoreMng;
	}

	/**
	 * Getter 품번
	 */
	public String getCdGoods() {
		return cdGoods;
	}

	/**
	 * Setter 품번
	 * 
	 * @param cdGoods
	 *            품번
	 */
	public void setCdGoods(String cdGoods) {
		this.cdGoods = cdGoods;
	}

	/**
	 * Getter 품명
	 */
	public String getNmGoods() {
		return nmGoods;
	}

	/**
	 * Setter 품명
	 * 
	 * @param nmGoods
	 *            품명
	 */
	public void setNmGoods(String nmGoods) {
		this.nmGoods = nmGoods;
	}

	/**
	 * Getter 매출과세구분
	 */
	public Integer getDsTax() {
		return dsTax;
	}

	/**
	 * Setter 매출과세구분
	 * 
	 * @param dsTax
	 *            매출과세구분
	 */
	public void setDsTax(Integer dsTax) {
		this.dsTax = dsTax;
	}

	/**
	 * Getter 매입단가
	 */
	public Double getPrBuy() {
		return prBuy;
	}

	/**
	 * Setter 매입단가
	 * 
	 * @param prBuy
	 *            매입단가
	 */
	public void setPrBuy(Double prBuy) {
		this.prBuy = prBuy;
	}

	/**
	 * Getter 판매단가
	 */
	public Double getPrSale() {
		return prSale;
	}

	/**
	 * Setter 판매단가
	 * 
	 * @param prSale
	 *            판매단가
	 */
	public void setPrSale(Double prSale) {
		this.prSale = prSale;
	}

	/**
	 * Getter 주문수량
	 */
	public Integer getQtyOrder() {
		return qtyOrder;
	}

	/**
	 * Setter 주문수량
	 * 
	 * @param qtyOrder
	 *            주문수량
	 */
	public void setQtyOrder(Integer qtyOrder) {
		this.qtyOrder = qtyOrder;
	}

	/**
	 * Getter 주문금액
	 */
	public Double getAmtOrder() {
		return amtOrder;
	}

	/**
	 * Setter 주문금액
	 * 
	 * @param amtOrder
	 *            주문금액
	 */
	public void setAmtOrder(Double amtOrder) {
		this.amtOrder = amtOrder;
	}

	/**
	 * Getter 공급가
	 */
	public Double getAmtSupply() {
		return amtSupply;
	}

	/**
	 * Setter 공급가
	 * 
	 * @param amtSupply
	 *            공급가
	 */
	public void setAmtSupply(Double amtSupply) {
		this.amtSupply = amtSupply;
	}

	/**
	 * Getter 부가세
	 */
	public Double getAmtTax() {
		return amtTax;
	}

	/**
	 * Setter 부가세
	 * 
	 * @param amtTax
	 *            부가세
	 */
	public void setAmtTax(Double amtTax) {
		this.amtTax = amtTax;
	}

	/**
	 * Getter 할인금액
	 */
	public Double getAmtDc() {
		return amtDc;
	}

	/**
	 * Setter 할인금액
	 * 
	 * @param amtDc
	 *            할인금액
	 */
	public void setAmtDc(Double amtDc) {
		this.amtDc = amtDc;
	}

	/**
	 * Getter 메모
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * Setter 메모
	 * 
	 * @param memo
	 *            메모
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * Getter 비매출 사유
	 */
	public Integer getCdNoSaleReason() {
		return cdNoSaleReason;
	}

	/**
	 * Setter 비매출 사유
	 * 
	 * @param cdNoSaleReason
	 *            비매출 사유
	 */
	public void setCdNoSaleReason(Integer cdNoSaleReason) {
		this.cdNoSaleReason = cdNoSaleReason;
	}

	/**
	 * Getter 주문일시
	 */
	public String getDtOrder() {
		return dtOrder;
	}

	/**
	 * Setter 주문일시
	 * 
	 * @param dtOrder
	 *            주문일시
	 */
	public void setDtOrder(String dtOrder) {
		this.dtOrder = dtOrder;
	}

	/**
	 * Getter 포장여부
	 */
	public Integer getYnPacking() {
		return ynPacking;
	}

	/**
	 * Setter 포장여부
	 * 
	 * @param ynPacking
	 *            포장여부
	 */
	public void setYnPacking(Integer ynPacking) {
		this.ynPacking = ynPacking;
	}

	/**
	 * Getter 기프트카드번호
	 */
	public String getNoGiftCard() {
		return noGiftCard;
	}

	/**
	 * Setter 기프트카드번호
	 * 
	 * @param noGiftCard
	 *            기프트카드번호
	 */
	public void setNoGiftCard(String noGiftCard) {
		this.noGiftCard = noGiftCard;
	}

	/**
	 * Getter 등록일시
	 */
	public String getDtInsert() {
		return dtInsert;
	}

	/**
	 * Setter 등록일시
	 * 
	 * @param dtInsert
	 *            등록일시
	 */
	public void setDtInsert(String dtInsert) {
		this.dtInsert = dtInsert;
	}

	/**
	 * Getter 등록자
	 */
	public String getCdEmployeeInsert() {
		return cdEmployeeInsert;
	}

	/**
	 * Setter 등록자
	 * 
	 * @param cdEmployeeInsert
	 *            등록자
	 */
	public void setCdEmployeeInsert(String cdEmployeeInsert) {
		this.cdEmployeeInsert = cdEmployeeInsert;
	}

	/**
	 * Getter 수정일시
	 */
	public String getDtUpdate() {
		return dtUpdate;
	}

	/**
	 * Setter 수정일시
	 * 
	 * @param dtUpdate
	 *            수정일시
	 */
	public void setDtUpdate(String dtUpdate) {
		this.dtUpdate = dtUpdate;
	}

	/**
	 * Getter 수정자
	 */
	public String getCdEmployeeUpdate() {
		return cdEmployeeUpdate;
	}

	/**
	 * Setter 수정자
	 * 
	 * @param cdEmployeeUpdate
	 *            수정자
	 */
	public void setCdEmployeeUpdate(String cdEmployeeUpdate) {
		this.cdEmployeeUpdate = cdEmployeeUpdate;
	}

	/**
	 * Getter 할인 내역
	 */
	public List<PosSalesOrderInfoDiscount> getDiscount() {
		return discount;
	}

	/**
	 * Setter 할인 내역
	 * 
	 * @param discount
	 *            할인 내역
	 */
	public void setDiscount(List<PosSalesOrderInfoDiscount> discount) {
		this.discount = discount;
	}

	/**
	 * Getter 주문 변동 이력
	 */
	public List<PosSalesOrderInfoHistory> getHistory() {
		return history;
	}

	/**
	 * Setter 주문 변동 이력
	 * 
	 * @param history
	 *            주문 변동 이력
	 */
	public void setHistory(List<PosSalesOrderInfoHistory> history) {
		this.history = history;
	}

	public Integer getCdOrderStatus() {
		return cdOrderStatus;
	}

	public void setCdOrderStatus(Integer cdOrderStatus) {
		this.cdOrderStatus = cdOrderStatus;
	}

	public Integer getCdOrderPath() {
		return cdOrderPath;
	}

	public void setCdOrderPath(Integer cdOrderPath) {
		this.cdOrderPath = cdOrderPath;
	}

	public List<PosSalesOrderInfoOption> getOptions() {
		return options;
	}

	public void setOptions(List<PosSalesOrderInfoOption> options) {
		this.options = options;
	}

	public Double getPrOptions() {
		return prOptions;
	}

	public void setPrOptions(Double prOptions) {
		this.prOptions = prOptions;
	}

}
