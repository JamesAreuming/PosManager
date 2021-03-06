package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcStampType {

	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.ID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private Integer id;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.TYPE_NM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private String typeNm;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.START_TM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private String startTm;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.END_TM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private String endTm;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.WEEK
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private String week;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.ITEM_CD
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private String itemCd;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.ITME_GROUP
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private String itmeGroup;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.ITEM_QTY
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private Integer itemQty;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.ORDER_PAY
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private Integer orderPay;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.CID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private Integer cid;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.CDT
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private Date cdt;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.MID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private Integer mid;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database column TB_SVC_STAMP_TYPE.MDT
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	private Date mdt;

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.ID
	 * @return  the value of TB_SVC_STAMP_TYPE.ID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.ID
	 * @param id  the value for TB_SVC_STAMP_TYPE.ID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.TYPE_NM
	 * @return  the value of TB_SVC_STAMP_TYPE.TYPE_NM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public String getTypeNm() {
		return typeNm;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.TYPE_NM
	 * @param typeNm  the value for TB_SVC_STAMP_TYPE.TYPE_NM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setTypeNm(String typeNm) {
		this.typeNm = typeNm == null ? null : typeNm.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.START_TM
	 * @return  the value of TB_SVC_STAMP_TYPE.START_TM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public String getStartTm() {
		return startTm;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.START_TM
	 * @param startTm  the value for TB_SVC_STAMP_TYPE.START_TM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setStartTm(String startTm) {
		this.startTm = startTm == null ? null : startTm.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.END_TM
	 * @return  the value of TB_SVC_STAMP_TYPE.END_TM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public String getEndTm() {
		return endTm;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.END_TM
	 * @param endTm  the value for TB_SVC_STAMP_TYPE.END_TM
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setEndTm(String endTm) {
		this.endTm = endTm == null ? null : endTm.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.WEEK
	 * @return  the value of TB_SVC_STAMP_TYPE.WEEK
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public String getWeek() {
		return week;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.WEEK
	 * @param week  the value for TB_SVC_STAMP_TYPE.WEEK
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setWeek(String week) {
		this.week = week == null ? null : week.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.ITEM_CD
	 * @return  the value of TB_SVC_STAMP_TYPE.ITEM_CD
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public String getItemCd() {
		return itemCd;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.ITEM_CD
	 * @param itemCd  the value for TB_SVC_STAMP_TYPE.ITEM_CD
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setItemCd(String itemCd) {
		this.itemCd = itemCd == null ? null : itemCd.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.ITME_GROUP
	 * @return  the value of TB_SVC_STAMP_TYPE.ITME_GROUP
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public String getItmeGroup() {
		return itmeGroup;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.ITME_GROUP
	 * @param itmeGroup  the value for TB_SVC_STAMP_TYPE.ITME_GROUP
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setItmeGroup(String itmeGroup) {
		this.itmeGroup = itmeGroup == null ? null : itmeGroup.trim();
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.ITEM_QTY
	 * @return  the value of TB_SVC_STAMP_TYPE.ITEM_QTY
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Integer getItemQty() {
		return itemQty;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.ITEM_QTY
	 * @param itemQty  the value for TB_SVC_STAMP_TYPE.ITEM_QTY
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.ORDER_PAY
	 * @return  the value of TB_SVC_STAMP_TYPE.ORDER_PAY
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Integer getOrderPay() {
		return orderPay;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.ORDER_PAY
	 * @param orderPay  the value for TB_SVC_STAMP_TYPE.ORDER_PAY
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setOrderPay(Integer orderPay) {
		this.orderPay = orderPay;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.CID
	 * @return  the value of TB_SVC_STAMP_TYPE.CID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Integer getCid() {
		return cid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.CID
	 * @param cid  the value for TB_SVC_STAMP_TYPE.CID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setCid(Integer cid) {
		this.cid = cid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.CDT
	 * @return  the value of TB_SVC_STAMP_TYPE.CDT
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Date getCdt() {
		return cdt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.CDT
	 * @param cdt  the value for TB_SVC_STAMP_TYPE.CDT
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setCdt(Date cdt) {
		this.cdt = cdt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.MID
	 * @return  the value of TB_SVC_STAMP_TYPE.MID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Integer getMid() {
		return mid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.MID
	 * @param mid  the value for TB_SVC_STAMP_TYPE.MID
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setMid(Integer mid) {
		this.mid = mid;
	}

	/**
	 * This method was generated by MyBatis Generator. This method returns the value of the database column TB_SVC_STAMP_TYPE.MDT
	 * @return  the value of TB_SVC_STAMP_TYPE.MDT
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Date getMdt() {
		return mdt;
	}

	/**
	 * This method was generated by MyBatis Generator. This method sets the value of the database column TB_SVC_STAMP_TYPE.MDT
	 * @param mdt  the value for TB_SVC_STAMP_TYPE.MDT
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setMdt(Date mdt) {
		this.mdt = mdt;
	}
}