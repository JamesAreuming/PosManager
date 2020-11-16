package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcCouponWithBLOBs extends SvcCoupon {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_coupon.STORE_IDS
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String storeIds;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_coupon.NOTICE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String notice;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_coupon.STORE_IDS
     *
     * @return the value of tb_svc_coupon.STORE_IDS
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getStoreIds() {
        return storeIds;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_coupon.STORE_IDS
     *
     * @param storeIds the value for tb_svc_coupon.STORE_IDS
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setStoreIds(String storeIds) {
        this.storeIds = storeIds == null ? null : storeIds.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_coupon.NOTICE
     *
     * @return the value of tb_svc_coupon.NOTICE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getNotice() {
        return notice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_coupon.NOTICE
     *
     * @param notice the value for tb_svc_coupon.NOTICE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setNotice(String notice) {
        this.notice = notice == null ? null : notice.trim();
    }
}