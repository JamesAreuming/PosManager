package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcOrderItemHistory {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Long orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Long itemId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Long staffId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Long ordinal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.ORDER_CNT
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Short orderCnt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.ORDER_TM
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Date orderTm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.REASON
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private String reason;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.MEMO
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private String memo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.CREATED
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item_history.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.ID
     *
     * @return the value of tb_svc_order_item_history.ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.ID
     *
     * @param id the value for tb_svc_order_item_history.ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.ORDER_ID
     *
     * @return the value of tb_svc_order_item_history.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.ORDER_ID
     *
     * @param orderId the value for tb_svc_order_item_history.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.ITEM_ID
     *
     * @return the value of tb_svc_order_item_history.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.ITEM_ID
     *
     * @param itemId the value for tb_svc_order_item_history.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.STAFF_ID
     *
     * @return the value of tb_svc_order_item_history.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Long getStaffId() {
        return staffId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.STAFF_ID
     *
     * @param staffId the value for tb_svc_order_item_history.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.ORDINAL
     *
     * @return the value of tb_svc_order_item_history.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Long getOrdinal() {
        return ordinal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.ORDINAL
     *
     * @param ordinal the value for tb_svc_order_item_history.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setOrdinal(Long ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.ORDER_CNT
     *
     * @return the value of tb_svc_order_item_history.ORDER_CNT
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Short getOrderCnt() {
        return orderCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.ORDER_CNT
     *
     * @param orderCnt the value for tb_svc_order_item_history.ORDER_CNT
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setOrderCnt(Short orderCnt) {
        this.orderCnt = orderCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.ORDER_TM
     *
     * @return the value of tb_svc_order_item_history.ORDER_TM
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Date getOrderTm() {
        return orderTm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.ORDER_TM
     *
     * @param orderTm the value for tb_svc_order_item_history.ORDER_TM
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setOrderTm(Date orderTm) {
        this.orderTm = orderTm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.REASON
     *
     * @return the value of tb_svc_order_item_history.REASON
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public String getReason() {
        return reason;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.REASON
     *
     * @param reason the value for tb_svc_order_item_history.REASON
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setReason(String reason) {
        this.reason = reason == null ? null : reason.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.MEMO
     *
     * @return the value of tb_svc_order_item_history.MEMO
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.MEMO
     *
     * @param memo the value for tb_svc_order_item_history.MEMO
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.CREATED
     *
     * @return the value of tb_svc_order_item_history.CREATED
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.CREATED
     *
     * @param created the value for tb_svc_order_item_history.CREATED
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item_history.UPDATED
     *
     * @return the value of tb_svc_order_item_history.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item_history.UPDATED
     *
     * @param updated the value for tb_svc_order_item_history.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:45:47 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}