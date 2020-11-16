package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcStockAdjust {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.BRAND_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Long brandId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.STORE_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Long storeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.ITEM_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Long itemId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.SAFE_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Integer safeCnt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.CURRENT_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Integer currentCnt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.ACTUAL_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Integer actualCnt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.GAP_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Integer gapCnt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.CREATED
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_stock_adjust.UPDATED
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.ID
     *
     * @return the value of tb_svc_stock_adjust.ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.ID
     *
     * @param id the value for tb_svc_stock_adjust.ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.BRAND_ID
     *
     * @return the value of tb_svc_stock_adjust.BRAND_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.BRAND_ID
     *
     * @param brandId the value for tb_svc_stock_adjust.BRAND_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.STORE_ID
     *
     * @return the value of tb_svc_stock_adjust.STORE_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Long getStoreId() {
        return storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.STORE_ID
     *
     * @param storeId the value for tb_svc_stock_adjust.STORE_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.ITEM_ID
     *
     * @return the value of tb_svc_stock_adjust.ITEM_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.ITEM_ID
     *
     * @param itemId the value for tb_svc_stock_adjust.ITEM_ID
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.SAFE_CNT
     *
     * @return the value of tb_svc_stock_adjust.SAFE_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Integer getSafeCnt() {
        return safeCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.SAFE_CNT
     *
     * @param safeCnt the value for tb_svc_stock_adjust.SAFE_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setSafeCnt(Integer safeCnt) {
        this.safeCnt = safeCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.CURRENT_CNT
     *
     * @return the value of tb_svc_stock_adjust.CURRENT_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Integer getCurrentCnt() {
        return currentCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.CURRENT_CNT
     *
     * @param currentCnt the value for tb_svc_stock_adjust.CURRENT_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setCurrentCnt(Integer currentCnt) {
        this.currentCnt = currentCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.ACTUAL_CNT
     *
     * @return the value of tb_svc_stock_adjust.ACTUAL_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Integer getActualCnt() {
        return actualCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.ACTUAL_CNT
     *
     * @param actualCnt the value for tb_svc_stock_adjust.ACTUAL_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setActualCnt(Integer actualCnt) {
        this.actualCnt = actualCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.GAP_CNT
     *
     * @return the value of tb_svc_stock_adjust.GAP_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Integer getGapCnt() {
        return gapCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.GAP_CNT
     *
     * @param gapCnt the value for tb_svc_stock_adjust.GAP_CNT
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setGapCnt(Integer gapCnt) {
        this.gapCnt = gapCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.CREATED
     *
     * @return the value of tb_svc_stock_adjust.CREATED
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.CREATED
     *
     * @param created the value for tb_svc_stock_adjust.CREATED
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_stock_adjust.UPDATED
     *
     * @return the value of tb_svc_stock_adjust.UPDATED
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_stock_adjust.UPDATED
     *
     * @param updated the value for tb_svc_stock_adjust.UPDATED
     *
     * @mbggenerated Thu Dec 01 15:37:25 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}