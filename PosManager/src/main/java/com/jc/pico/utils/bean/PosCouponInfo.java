package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 포스 연동 쿠폰 정보
 * S_COUPON_INFO
 * 
 * @author hyo 2016.09.02
 *
 */
public class PosCouponInfo implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 1L;

	/**
	 * 사용자 쿠폰 ID
	 */
	@JsonProperty(value = "CD_COUPON")
	private Long cdCoupon;

	/**
	 * 사용자 쿠폰 Code
	 */
	@JsonProperty(value = "NO_COUPON")
	private String noCoupon;

	/**
	 * 쿠폰 유효성
	 */
	@JsonProperty(value = "CD_VALID")
	private Integer cdValid;

	/**
	 * 발행일 (yyyyMMddHHmmss)
	 */
	@JsonProperty(value = "DT_ISSUE")
	private String dtIssue;

	/**
	 * 사용자 쿠폰 상태
	 * 402001:발행, 402002:재발행(사용취소), 402003:사용, 402004:취소(발행취소))
	 */
	@JsonProperty(value = "CD_COUPON_STATUS")
	private Integer cdCouponStatus;

	/**
	 * 쿠폰 소유자 ID
	 */
	@JsonProperty(value = "CD_MEMBER")
	private Long cdMember;

	/**
	 * 발급 브랜드
	 */
	@JsonProperty(value = "CD_COMPANY")
	private Long cdCompany;

	/**
	 * 발급 상점
	 */
	@JsonProperty(value = "CD_STORE")
	private Long cdStore;

	/**
	 * 쿠폰 이름
	 */
	@JsonProperty(value = "NM_COUPON")
	private String nmCoupon;

	/**
	 * 할인 금액 혹은 할인 비율
	 */
	@JsonProperty(value = "PR_DISCOUNT")
	private Double prDiscount;

	/**
	 * 할인 종류
	 * 403001:비율 할인, 403002: 금액할인, 403003:1+1, 403004:공짜메뉴
	 */
	@JsonProperty(value = "CD_DISCOUNT_TYPE")
	private Integer cdDiscountType;

	/**
	 * 최대 할인 금액
	 */
	@JsonProperty(value = "PR_DISCOUNT_LIMIT")
	private Double prDiscountLimit;

	/**
	 * 만료 타입
	 * 404001:발행일로부터, 404002:기간지정
	 */
	@JsonProperty(value = "CD_EXPIRE_TYPE")
	private Integer cdExpireType;

	/**
	 * 쿠폰 유효기간 시작일
	 */
	@JsonProperty(value = "YMD_BEGIN")
	private String ymdBegin;

	/**
	 * 쿠폰 유효기간 만료일
	 */
	@JsonProperty(value = "YMD_EXPIRE")
	private String ymdExpire;

	/**
	 * 사용 가능한 메뉴 ID
	 */
	@JsonProperty(value = "CD_TARGET_MENU")
	private Long cdTargetMenuId;

	/**
	 * 사용 가능한 공급??
	 */
	@JsonProperty(value = "CD_SUPPLY_MENU")
	private Long cdSupplyMenuId;

	/**
	 * 사용 제한 사용 여부
	 */
	@JsonProperty(value = "YN_USE_LIMIT")
	private Integer ynUseLimit;

	/**
	 * 사용 가능한 요일
	 * 0000000
	 * 일월화수목금토일 ?
	 */
	@JsonProperty(value = "CD_ENABLE_DAYS")
	private String cdEnableDays;

	/**
	 * 사용 가능 시간대 개시 (hhmm)
	 */
	@JsonProperty(value = "TIME_OPEN")
	private String timeOpen;

	/**
	 * 사용 가능 시간대 종료시 (hhmm)
	 */
	@JsonProperty(value = "TIME_CLOSE")
	private String timeClose;

	/**
	 * 쿠폰 이미지
	 */
	@JsonProperty(value = "NM_IMG")
	private String nmImg;

	/**
	 * 작은 쿠폰 이미지
	 */
	@JsonProperty(value = "NM_SMALL_IMG")
	private String nmSmallImg;

	/**
	 * 전체 매장에서 사용
	 */
	@JsonProperty(value = "YN_ALL")
	private Integer ynAll;

	/**
	 * 사용 가능한 매장 목록 (json array string)
	 */
	@JsonProperty(value = "STORES")
	private List<PosResultStoreInfo> stores;

	/**
	 * 유의사항
	 */
	@JsonProperty(value = "NM_NOTICE")
	private String nmNotice;

	public Long getCdCoupon() {
		return cdCoupon;
	}

	public void setCdCoupon(Long cdCoupon) {
		this.cdCoupon = cdCoupon;
	}

	public String getNoCoupon() {
		return noCoupon;
	}

	public void setNoCoupon(String noCoupon) {
		this.noCoupon = noCoupon;
	}

	public Integer getCdValid() {
		return cdValid;
	}

	public void setCdValid(Integer cdValid) {
		this.cdValid = cdValid;
	}

	public String getDtIssue() {
		return dtIssue;
	}

	public void setDtIssue(String dtIssue) {
		this.dtIssue = dtIssue;
	}

	public Integer getCdCouponStatus() {
		return cdCouponStatus;
	}

	public void setCdCouponStatus(Integer cdCouponStatus) {
		this.cdCouponStatus = cdCouponStatus;
	}

	public Long getCdMember() {
		return cdMember;
	}

	public void setCdMember(Long cdMember) {
		this.cdMember = cdMember;
	}

	public Long getCdCompany() {
		return cdCompany;
	}

	public void setCdCompany(Long cdCompany) {
		this.cdCompany = cdCompany;
	}

	public Long getCdStore() {
		return cdStore;
	}

	public void setCdStore(Long cdStore) {
		this.cdStore = cdStore;
	}

	public String getNmCoupon() {
		return nmCoupon;
	}

	public void setNmCoupon(String nmCoupon) {
		this.nmCoupon = nmCoupon;
	}

	public Double getPrDiscount() {
		return prDiscount;
	}

	public void setPrDiscount(Double prDiscount) {
		this.prDiscount = prDiscount;
	}

	public Integer getCdDiscountType() {
		return cdDiscountType;
	}

	public void setCdDiscountType(Integer cdDiscountType) {
		this.cdDiscountType = cdDiscountType;
	}

	public Double getPrDiscountLimit() {
		return prDiscountLimit;
	}

	public void setPrDiscountLimit(Double prDiscountLimit) {
		this.prDiscountLimit = prDiscountLimit;
	}

	public Integer getCdExpireType() {
		return cdExpireType;
	}

	public void setCdExpireType(Integer cdExpireType) {
		this.cdExpireType = cdExpireType;
	}

	public String getYmdBegin() {
		return ymdBegin;
	}

	public void setYmdBegin(String ymdBegin) {
		this.ymdBegin = ymdBegin;
	}

	public String getYmdExpire() {
		return ymdExpire;
	}

	public void setYmdExpire(String ymdExpire) {
		this.ymdExpire = ymdExpire;
	}

	public Long getCdTargetMenuId() {
		return cdTargetMenuId;
	}

	public void setCdTargetMenuId(Long cdTargetMenuId) {
		this.cdTargetMenuId = cdTargetMenuId;
	}

	public Long getCdSupplyMenuId() {
		return cdSupplyMenuId;
	}

	public void setCdSupplyMenuId(Long cdSupplyMenuId) {
		this.cdSupplyMenuId = cdSupplyMenuId;
	}

	public Integer getYnUseLimit() {
		return ynUseLimit;
	}

	public void setYnUseLimit(Integer ynUseLimit) {
		this.ynUseLimit = ynUseLimit;
	}

	public String getCdEnableDays() {
		return cdEnableDays;
	}

	public void setCdEnableDays(String cdEnableDays) {
		this.cdEnableDays = cdEnableDays;
	}

	public String getTimeOpen() {
		return timeOpen;
	}

	public void setTimeOpen(String timeOpen) {
		this.timeOpen = timeOpen;
	}

	public String getTimeClose() {
		return timeClose;
	}

	public void setTimeClose(String timeClose) {
		this.timeClose = timeClose;
	}

	public String getNmImg() {
		return nmImg;
	}

	public void setNmImg(String nmImg) {
		this.nmImg = nmImg;
	}

	public String getNmSmallImg() {
		return nmSmallImg;
	}

	public void setNmSmallImg(String nmSmallImg) {
		this.nmSmallImg = nmSmallImg;
	}

	public Integer getYnAll() {
		return ynAll;
	}

	public void setYnAll(Integer ynAll) {
		this.ynAll = ynAll;
	}

	public List<PosResultStoreInfo> getStores() {
		return stores;
	}

	public void setStores(List<PosResultStoreInfo> stores) {
		this.stores = stores;
	}

	public String getNmNotice() {
		return nmNotice;
	}

	public void setNmNotice(String nmNotice) {
		this.nmNotice = nmNotice;
	}

	@Override
	public String toString() {
		return "PosCouponInfo [cdCoupon=" + cdCoupon + ", noCoupon=" + noCoupon + ", cdValid=" + cdValid + ", dtIssue=" + dtIssue
				+ ", cdCouponStatus=" + cdCouponStatus + ", cdMember=" + cdMember + ", cdCompany=" + cdCompany + ", cdStore=" + cdStore
				+ ", nmCoupon=" + nmCoupon + ", prDiscount=" + prDiscount + ", cdDiscountType=" + cdDiscountType + ", prDiscountLimit="
				+ prDiscountLimit + ", cdExpireType=" + cdExpireType + ", ymdBegin=" + ymdBegin + ", ymdExpire=" + ymdExpire + ", cdTargetMenuId="
				+ cdTargetMenuId + ", cdSupplyMenuId=" + cdSupplyMenuId + ", ynUseLimit=" + ynUseLimit + ", cdEnableDays=" + cdEnableDays
				+ ", timeOpen=" + timeOpen + ", timeClose=" + timeClose + ", nmImg=" + nmImg + ", nmSmallImg=" + nmSmallImg + ", ynAll=" + ynAll
				+ ", stores=" + stores + ", nmNotice=" + nmNotice + "]";
	}

}
