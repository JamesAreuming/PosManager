package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 10. M_GOODS_INFO : 상품 정보
 * 2016. 7. 21, green, create
 */
public class PosMasterGoodsInfo implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 회사코드
	 */
	@JsonProperty(value = "CD_COMPANY")
	private String cdCompany;
	/**
	 * 매장코드
	 */
	@JsonProperty(value = "CD_STORE")
	private String cdStore;
	/**
	 * 품번
	 */
	@JsonProperty(value = "CD_GOODS")
	private String cdGoods;
	/**
	 * 매장코드
	 */
	@JsonProperty(value = "CD_STORE_MNG")
	private String cdStoreMng;
	/**
	 * 후방분류코드
	 */
	@JsonProperty(value = "CD_CLASS")
	private String cdClass;
	/**
	 * 세트코스구분코드
	 */
	@JsonProperty(value = "CD_SALE_TYPE")
	private Integer cdSaleType;

	/**
	 * 상품 옵션 사용 여부 (0: 미사용, 1: 사용)
	 */
	@JsonProperty(value = "YN_GOODS_OPTION")
	private Integer ynGoodsOption;

	/**
	 * 밴사코드
	 */
	@JsonProperty(value = "CD_VAN")
	private Long cdVan;
	/**
	 * 주방프린터
	 */
	@JsonProperty(value = "CD_PRINTER")
	private String cdPrinter;
	/**
	 * 품목유형코드
	 */
	@JsonProperty(value = "CD_GOODS_TYPE")
	private Integer cdGoodsType;
	/**
	 * 매출과세구분
	 */
	@JsonProperty(value = "DS_TAX")
	private Integer dsTax;
	/**
	 * 상품명
	 */
	@JsonProperty(value = "NM_FULL")
	private String nmFull;
	/**
	 * 상품약칭명
	 */
	@JsonProperty(value = "NM_SHORT")
	private String nmShort;
	/**
	 * 상품영문명
	 */
	@JsonProperty(value = "NM_ENG")
	private String nmEng;
	/**
	 * 규격
	 */
	@JsonProperty(value = "NM_SIZE")
	private String nmSize;
	/**
	 * 제조사
	 */
	@JsonProperty(value = "NM_MAKER")
	private String nmMaker;
	/**
	 * 바코드
	 */
	@JsonProperty(value = "BARCODE")
	private String barcode;
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
	 * 할인단가
	 */
	@JsonProperty(value = "PR_DISCOUNT")
	private Double prDiscount;
	/**
	 * 포인트적립유무
	 */
	@JsonProperty(value = "YN_POINT_SAVE")
	private Integer ynPointSave;
	/**
	 * 포인트사용유무
	 */
	@JsonProperty(value = "YN_POINT_USE")
	private Integer ynPointUse;
	/**
	 * 할인가능유무
	 */
	@JsonProperty(value = "YN_DISCOUNT")
	private Integer ynDiscount;
	/**
	 * 사용유무
	 */
	@JsonProperty(value = "YN_USE")
	private Integer ynUse;
	/**
	 * 오픈세트갯수
	 */
	@JsonProperty(value = "QTY_SELECT")
	private Integer qtySelect;
	/**
	 * 무시주방프린터
	 */
	@JsonProperty(value = "CD_PRINTER_IG")
	private String cdPrinterIg;
	/**
	 * 기준중량
	 */
	@JsonProperty(value = "WT_STANDARD")
	private Double wtStandard;
	/**
	 * 주요매출거래처코드
	 */
	@JsonProperty(value = "CD_CORNER")
	private String cdCorner;
	/**
	 * 카드결제불가유무
	 */
	@JsonProperty(value = "YN_NO_CARD_PAY")
	private Integer ynNoCardPay;
	/**
	 * 공급가
	 */
	@JsonProperty(value = "PR_SUPPLY")
	private Double prSupply;
	/**
	 * 부가세
	 */
	@JsonProperty(value = "PR_TAX")
	private Double prTax;
	/**
	 * 코스 자동 팝업 유무
	 */
	@JsonProperty(value = "YN_COURSE_AUTO_POPUP")
	private Integer ynCourseAutoPopup;
	/**
	 * 발주단가
	 */
	@JsonProperty(value = "PR_BALJU")
	private Double prBalju;
	/**
	 * 이미지 파일명
	 */
	@JsonProperty(value = "NM_IMAGE")
	private String nmImage;
	/**
	 * 메모
	 */
	@JsonProperty(value = "MEMO")
	private String memo;
	/**
	 * 검색약칭명
	 */
	@JsonProperty(value = "NM_SEARCH")
	private String nmSearch;
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
	 * 상품 옵션
	 */
	@JsonProperty(value = "OPTIONS")
	private List<PosMasterGoodsOpt> options;

	/**
	 * Getter 회사코드
	 */
	public String getCdCompany() {
		return cdCompany;
	}

	/**
	 * Setter 회사코드
	 * 
	 * @param cdCompany
	 *            회사코드
	 */
	public void setCdCompany(String cdCompany) {
		this.cdCompany = cdCompany;
	}

	/**
	 * Getter 매장코드
	 */
	public String getCdStore() {
		return cdStore;
	}

	/**
	 * Setter 매장코드
	 * 
	 * @param cdStore
	 *            매장코드
	 */
	public void setCdStore(String cdStore) {
		this.cdStore = cdStore;
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
	 * Getter 매장코드
	 */
	public String getCdStoreMng() {
		return cdStoreMng;
	}

	/**
	 * Setter 매장코드
	 * 
	 * @param cdStoreMng
	 *            매장코드
	 */
	public void setCdStoreMng(String cdStoreMng) {
		this.cdStoreMng = cdStoreMng;
	}

	/**
	 * Getter 후방분류코드
	 */
	public String getCdClass() {
		return cdClass;
	}

	/**
	 * Setter 후방분류코드
	 * 
	 * @param cdClass
	 *            후방분류코드
	 */
	public void setCdClass(String cdClass) {
		this.cdClass = cdClass;
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
	 * Getter 밴사코드
	 */
	public Long getCdVan() {
		return cdVan;
	}

	/**
	 * Setter 밴사코드
	 * 
	 * @param cdVan
	 *            밴사코드
	 */
	public void setCdVan(Long cdVan) {
		this.cdVan = cdVan;
	}

	/**
	 * Getter 주방프린터
	 */
	public String getCdPrinter() {
		return cdPrinter;
	}

	/**
	 * Setter 주방프린터
	 * 
	 * @param cdPrinter
	 *            주방프린터
	 */
	public void setCdPrinter(String cdPrinter) {
		this.cdPrinter = cdPrinter;
	}

	/**
	 * Getter 품목유형코드
	 */
	public Integer getCdGoodsType() {
		return cdGoodsType;
	}

	/**
	 * Setter 품목유형코드
	 * 
	 * @param cdGoodsType
	 *            품목유형코드
	 */
	public void setCdGoodsType(Integer cdGoodsType) {
		this.cdGoodsType = cdGoodsType;
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
	 * Getter 상품명
	 */
	public String getNmFull() {
		return nmFull;
	}

	/**
	 * Setter 상품명
	 * 
	 * @param nmFull
	 *            상품명
	 */
	public void setNmFull(String nmFull) {
		this.nmFull = nmFull;
	}

	/**
	 * Getter 상품약칭명
	 */
	public String getNmShort() {
		return nmShort;
	}

	/**
	 * Setter 상품약칭명
	 * 
	 * @param nmShort
	 *            상품약칭명
	 */
	public void setNmShort(String nmShort) {
		this.nmShort = nmShort;
	}

	/**
	 * Getter 상품영문명
	 */
	public String getNmEng() {
		return nmEng;
	}

	/**
	 * Setter 상품영문명
	 * 
	 * @param nmEng
	 *            상품영문명
	 */
	public void setNmEng(String nmEng) {
		this.nmEng = nmEng;
	}

	/**
	 * Getter 규격
	 */
	public String getNmSize() {
		return nmSize;
	}

	/**
	 * Setter 규격
	 * 
	 * @param nmSize
	 *            규격
	 */
	public void setNmSize(String nmSize) {
		this.nmSize = nmSize;
	}

	/**
	 * Getter 제조사
	 */
	public String getNmMaker() {
		return nmMaker;
	}

	/**
	 * Setter 제조사
	 * 
	 * @param nmMaker
	 *            제조사
	 */
	public void setNmMaker(String nmMaker) {
		this.nmMaker = nmMaker;
	}

	/**
	 * Getter 바코드
	 */
	public String getBarcode() {
		return barcode;
	}

	/**
	 * Setter 바코드
	 * 
	 * @param barcode
	 *            바코드
	 */
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	 * Getter 할인단가
	 */
	public Double getPrDiscount() {
		return prDiscount;
	}

	/**
	 * Setter 할인단가
	 * 
	 * @param prDiscount
	 *            할인단가
	 */
	public void setPrDiscount(Double prDiscount) {
		this.prDiscount = prDiscount;
	}

	/**
	 * Getter 포인트적립유무
	 */
	public Integer getYnPointSave() {
		return ynPointSave;
	}

	/**
	 * Setter 포인트적립유무
	 * 
	 * @param ynPointSave
	 *            포인트적립유무
	 */
	public void setYnPointSave(Integer ynPointSave) {
		this.ynPointSave = ynPointSave;
	}

	/**
	 * Getter 포인트사용유무
	 */
	public Integer getYnPointUse() {
		return ynPointUse;
	}

	/**
	 * Setter 포인트사용유무
	 * 
	 * @param ynPointUse
	 *            포인트사용유무
	 */
	public void setYnPointUse(Integer ynPointUse) {
		this.ynPointUse = ynPointUse;
	}

	/**
	 * Getter 할인가능유무
	 */
	public Integer getYnDiscount() {
		return ynDiscount;
	}

	/**
	 * Setter 할인가능유무
	 * 
	 * @param ynDiscount
	 *            할인가능유무
	 */
	public void setYnDiscount(Integer ynDiscount) {
		this.ynDiscount = ynDiscount;
	}

	/**
	 * Getter 사용유무
	 */
	public Integer getYnUse() {
		return ynUse;
	}

	/**
	 * Setter 사용유무
	 * 
	 * @param ynUse
	 *            사용유무
	 */
	public void setYnUse(Integer ynUse) {
		this.ynUse = ynUse;
	}

	/**
	 * Getter 오픈세트갯수
	 */
	public Integer getQtySelect() {
		return qtySelect;
	}

	/**
	 * Setter 오픈세트갯수
	 * 
	 * @param qtySelect
	 *            오픈세트갯수
	 */
	public void setQtySelect(Integer qtySelect) {
		this.qtySelect = qtySelect;
	}

	/**
	 * Getter 무시주방프린터
	 */
	public String getCdPrinterIg() {
		return cdPrinterIg;
	}

	/**
	 * Setter 무시주방프린터
	 * 
	 * @param cdPrinterIg
	 *            무시주방프린터
	 */
	public void setCdPrinterIg(String cdPrinterIg) {
		this.cdPrinterIg = cdPrinterIg;
	}

	/**
	 * Getter 기준중량
	 */
	public Double getWtStandard() {
		return wtStandard;
	}

	/**
	 * Setter 기준중량
	 * 
	 * @param wtStandard
	 *            기준중량
	 */
	public void setWtStandard(Double wtStandard) {
		this.wtStandard = wtStandard;
	}

	/**
	 * Getter 주요매출거래처코드
	 */
	public String getCdCorner() {
		return cdCorner;
	}

	/**
	 * Setter 주요매출거래처코드
	 * 
	 * @param cdCorner
	 *            주요매출거래처코드
	 */
	public void setCdCorner(String cdCorner) {
		this.cdCorner = cdCorner;
	}

	/**
	 * Getter 카드결제불가유무
	 */
	public Integer getYnNoCardPay() {
		return ynNoCardPay;
	}

	/**
	 * Setter 카드결제불가유무
	 * 
	 * @param ynNoCardPay
	 *            카드결제불가유무
	 */
	public void setYnNoCardPay(Integer ynNoCardPay) {
		this.ynNoCardPay = ynNoCardPay;
	}

	/**
	 * Getter 공급가
	 */
	public Double getPrSupply() {
		return prSupply;
	}

	/**
	 * Setter 공급가
	 * 
	 * @param prSupply
	 *            공급가
	 */
	public void setPrSupply(Double prSupply) {
		this.prSupply = prSupply;
	}

	/**
	 * Getter 부가세
	 */
	public Double getPrTax() {
		return prTax;
	}

	/**
	 * Setter 부가세
	 * 
	 * @param prTax
	 *            부가세
	 */
	public void setPrTax(Double prTax) {
		this.prTax = prTax;
	}

	/**
	 * Getter 코스 자동 팝업 유무
	 */
	public Integer getYnCourseAutoPopup() {
		return ynCourseAutoPopup;
	}

	/**
	 * Setter 코스 자동 팝업 유무
	 * 
	 * @param ynCourseAutoPopup
	 *            코스 자동 팝업 유무
	 */
	public void setYnCourseAutoPopup(Integer ynCourseAutoPopup) {
		this.ynCourseAutoPopup = ynCourseAutoPopup;
	}

	/**
	 * Getter 발주단가
	 */
	public Double getPrBalju() {
		return prBalju;
	}

	/**
	 * Setter 발주단가
	 * 
	 * @param prBalju
	 *            발주단가
	 */
	public void setPrBalju(Double prBalju) {
		this.prBalju = prBalju;
	}

	/**
	 * Getter 이미지 파일명
	 */
	public String getNmImage() {
		return nmImage;
	}

	/**
	 * Setter 이미지 파일명
	 * 
	 * @param nmImage
	 *            이미지 파일명
	 */
	public void setNmImage(String nmImage) {
		this.nmImage = nmImage;
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
	 * Getter 검색약칭명
	 */
	public String getNmSearch() {
		return nmSearch;
	}

	/**
	 * Setter 검색약칭명
	 * 
	 * @param nmSearch
	 *            검색약칭명
	 */
	public void setNmSearch(String nmSearch) {
		this.nmSearch = nmSearch;
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

	public List<PosMasterGoodsOpt> getOptions() {
		return options;
	}

	public void setOptions(List<PosMasterGoodsOpt> options) {
		this.options = options;
	}

	public Integer getYnGoodsOption() {
		return ynGoodsOption;
	}

	public void setYnGoodsOption(Integer ynGoodsOption) {
		this.ynGoodsOption = ynGoodsOption;
	}

}
