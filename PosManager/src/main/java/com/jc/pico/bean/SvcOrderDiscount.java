package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcOrderDiscount {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long ordinal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.IS_CANCELED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Boolean isCanceled;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long itemId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long staffId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.DISCOUNT_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String discountTp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.AMOUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double amount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.MEMO
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String memo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.COUPON_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String couponCd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.CREATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_discount.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.ID
     *
     * @return the value of tb_svc_order_discount.ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.ID
     *
     * @param id the value for tb_svc_order_discount.ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.ORDER_ID
     *
     * @return the value of tb_svc_order_discount.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.ORDER_ID
     *
     * @param orderId the value for tb_svc_order_discount.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.ORDINAL
     *
     * @return the value of tb_svc_order_discount.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getOrdinal() {
        return ordinal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.ORDINAL
     *
     * @param ordinal the value for tb_svc_order_discount.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOrdinal(Long ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.IS_CANCELED
     *
     * @return the value of tb_svc_order_discount.IS_CANCELED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Boolean getIsCanceled() {
        return isCanceled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.IS_CANCELED
     *
     * @param isCanceled the value for tb_svc_order_discount.IS_CANCELED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setIsCanceled(Boolean isCanceled) {
        this.isCanceled = isCanceled;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.ITEM_ID
     *
     * @return the value of tb_svc_order_discount.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.ITEM_ID
     *
     * @param itemId the value for tb_svc_order_discount.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.STAFF_ID
     *
     * @return the value of tb_svc_order_discount.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getStaffId() {
        return staffId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.STAFF_ID
     *
     * @param staffId the value for tb_svc_order_discount.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.DISCOUNT_TP
     *
     * @return the value of tb_svc_order_discount.DISCOUNT_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getDiscountTp() {
        return discountTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.DISCOUNT_TP
     *
     * @param discountTp the value for tb_svc_order_discount.DISCOUNT_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setDiscountTp(String discountTp) {
        this.discountTp = discountTp == null ? null : discountTp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.AMOUNT
     *
     * @return the value of tb_svc_order_discount.AMOUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.AMOUNT
     *
     * @param amount the value for tb_svc_order_discount.AMOUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.MEMO
     *
     * @return the value of tb_svc_order_discount.MEMO
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.MEMO
     *
     * @param memo the value for tb_svc_order_discount.MEMO
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.COUPON_CD
     *
     * @return the value of tb_svc_order_discount.COUPON_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getCouponCd() {
        return couponCd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.COUPON_CD
     *
     * @param couponCd the value for tb_svc_order_discount.COUPON_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setCouponCd(String couponCd) {
        this.couponCd = couponCd == null ? null : couponCd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.CREATED
     *
     * @return the value of tb_svc_order_discount.CREATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.CREATED
     *
     * @param created the value for tb_svc_order_discount.CREATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_discount.UPDATED
     *
     * @return the value of tb_svc_order_discount.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_discount.UPDATED
     *
     * @param updated the value for tb_svc_order_discount.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}