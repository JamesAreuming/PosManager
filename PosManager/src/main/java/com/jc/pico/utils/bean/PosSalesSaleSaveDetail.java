package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 1. S_SALE_SAVE : 매출정보
 * 2016. 8. 13, green, create
 */
public class PosSalesSaleSaveDetail implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 일련번호
	 */
	@JsonProperty(value = "SEQ")
	private Long seq;
	
	/**
	 * 분할 결제 일련번호
	 */
	@JsonProperty(value = "SEQ_PARTIAL")
	private Long seq_partial;
	
	/**
	 * 판매형태코드(일반, 폐기, 서비스, 자가소비, 증정, 접대, 손실, 스탬프, 상품교환(단품반품))
	 */
	@JsonProperty(value = "DS_SALE")
	private Integer dsSale;

	/**
	 * 주문 경로 타입(pos: 606001, app: 606002, cleck: 606003, tab: 606004)
	 */
	@JsonProperty(value = "CD_ORDER_PATH")
	private Integer cdOrderPath;

	/**
	 * 세트코스구분코드
	 */
	@JsonProperty(value = "CD_SALE_TYPE")
	private Integer cdSaleType;
	/**
	 * 세트코스그룹번호
	 */
	@JsonProperty(value = "NO_GROUP")
	private Long noGroup;
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
	 * 과세구분코드
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
	 * 수량
	 */
	@JsonProperty(value = "QTY_SALE")
	private Integer qtySale;
	/**
	 * 금액
	 */
	@JsonProperty(value = "AMT_SALE")
	private Double amtSale;
	/**
	 * 공급가액
	 */
	@JsonProperty(value = "AMT_SUPPLY")
	private Double amtSupply;
	/**
	 * 부가세
	 */
	@JsonProperty(value = "AMT_VAT")
	private Double amtVat;
	/**
	 * 할인금액
	 */
	@JsonProperty(value = "AMT_DC")
	private Double amtDc;
	/**
	 * 수수료율
	 */
	@JsonProperty(value = "RT_COMMISSION")
	private Double rtCommission;
	/**
	 * 인센티브(금액)
	 */
	@JsonProperty(value = "AMT_COMMISSION")
	private Double amtCommission;
	/**
	 * 메모
	 */
	@JsonProperty(value = "MEMO")
	private String memo;
	/**
	 * 구성품유무
	 */
	@JsonProperty(value = "YN_SET_SUB")
	private Integer ynSetSub;
	/**
	 * 비매출사유
	 */
	@JsonProperty(value = "CD_NO_SALE_REASON")
	private Integer cdNoSaleReason;
	/**
	 * 포장여부
	 */
	@JsonProperty(value = "YN_PACKING")
	private Integer ynPacking;
	
	/**
	 * 주문일시
	 */
	@JsonProperty(value = "DT_ORDER")
	private String dtOrder;
	
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
	

	public Long getSeq_partial() {
		return seq_partial;
	}

	public void setSeq_partial(Long seq_partial) {
		this.seq_partial = seq_partial;
	}

	/**
	 * Getter 판매형태코드(일반, 폐기, 서비스, 자가소비, 증정, 접대, 손실, 스탬프, 상품교환(단품반품))
	 */
	public Integer getDsSale() {
		return dsSale;
	}

	/**
	 * Setter 판매형태코드(일반, 폐기, 서비스, 자가소비, 증정, 접대, 손실, 스탬프, 상품교환(단품반품))
	 * 
	 * @param dsSale
	 *            판매형태코드(일반, 폐기, 서비스, 자가소비, 증정, 접대, 손실, 스탬프, 상품교환(단품반품))
	 */
	public void setDsSale(Integer dsSale) {
		this.dsSale = dsSale;
	}

	/**
	 * Getter 세트코스구분코드
	 */
	public Integer getCdSaleType() {
		return cdSaleType;
	}

	/**
	 * Setter 세트코스구분코드
	 * 
	 * @param cdSaleType
	 *            세트코스구분코드
	 */
	public void setCdSaleType(Integer cdSaleType) {
		this.cdSaleType = cdSaleType;
	}

	/**
	 * Getter 세트코스그룹번호
	 */
	public Long getNoGroup() {
		return noGroup;
	}

	/**
	 * Setter 세트코스그룹번호
	 * 
	 * @param noGroup
	 *            세트코스그룹번호
	 */
	public void setNoGroup(Long noGroup) {
		this.noGroup = noGroup;
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
	 * Getter 과세구분코드
	 */
	public Integer getDsTax() {
		return dsTax;
	}

	/**
	 * Setter 과세구분코드
	 * 
	 * @param dsTax
	 *            과세구분코드
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
	 * Getter 수량
	 */
	public Integer getQtySale() {
		return qtySale;
	}

	/**
	 * Setter 수량
	 * 
	 * @param qtySale
	 *            수량
	 */
	public void setQtySale(Integer qtySale) {
		this.qtySale = qtySale;
	}

	/**
	 * Getter 금액
	 */
	public Double getAmtSale() {
		return amtSale;
	}

	/**
	 * Setter 금액
	 * 
	 * @param amtSale
	 *            금액
	 */
	public void setAmtSale(Double amtSale) {
		this.amtSale = amtSale;
	}

	/**
	 * Getter 공급가액
	 */
	public Double getAmtSupply() {
		return amtSupply;
	}

	/**
	 * Setter 공급가액
	 * 
	 * @param amtSupply
	 *            공급가액
	 */
	public void setAmtSupply(Double amtSupply) {
		this.amtSupply = amtSupply;
	}

	/**
	 * Getter 부가세
	 */
	public Double getAmtVat() {
		return amtVat;
	}

	/**
	 * Setter 부가세
	 * 
	 * @param amtTax
	 *            부가세
	 */
	public void setAmtVat(Double amtVat) {
		this.amtVat = amtVat;
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
	 * Getter 수수료율
	 */
	public Double getRtCommission() {
		return rtCommission;
	}

	/**
	 * Setter 수수료율
	 * 
	 * @param rtCommission
	 *            수수료율
	 */
	public void setRtCommission(Double rtCommission) {
		this.rtCommission = rtCommission;
	}

	/**
	 * Getter 인센티브(금액)
	 */
	public Double getAmtCommission() {
		return amtCommission;
	}

	/**
	 * Setter 인센티브(금액)
	 * 
	 * @param amtCommission
	 *            인센티브(금액)
	 */
	public void setAmtCommission(Double amtCommission) {
		this.amtCommission = amtCommission;
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
	 * Getter 구성품유무
	 */
	public Integer getYnSetSub() {
		return ynSetSub;
	}

	/**
	 * Setter 구성품유무
	 * 
	 * @param ynSetSub
	 *            구성품유무
	 */
	public void setYnSetSub(Integer ynSetSub) {
		this.ynSetSub = ynSetSub;
	}

	/**
	 * Getter 비매출사유
	 */
	public Integer getCdNoSaleReason() {
		return cdNoSaleReason;
	}

	/**
	 * Setter 비매출사유
	 * 
	 * @param cdNoSaleReason
	 *            비매출사유
	 */
	public void setCdNoSaleReason(Integer cdNoSaleReason) {
		this.cdNoSaleReason = cdNoSaleReason;
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

	public Integer getCdOrderPath() {
		return cdOrderPath;
	}

	public void setCdOrderPath(Integer cdOrderPath) {
		this.cdOrderPath = cdOrderPath;
	}	
	
	public String getDtOrder() {
		return dtOrder;
	}

	public void setDtOrder(String dtOrder) {
		this.dtOrder = dtOrder;
	}

	public List<PosSalesOrderInfoOption> getOptions() {
		return options;
	}

	public void setOptions(List<PosSalesOrderInfoOption> options) {
		this.options = options;
	}	

}
