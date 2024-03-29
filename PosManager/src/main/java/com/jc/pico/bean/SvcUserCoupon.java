package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcUserCoupon {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.USER_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.PROMOTION_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Long promotionId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.COUPON_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Long couponId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.BRAND_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Long brandId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.STORE_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Long storeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.COUPON_CD
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private String couponCd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.COUPON_TP
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private String couponTp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.COUPON_ST
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private String couponSt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.USED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Boolean used;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.USE_STORE_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Long useStoreId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.USE_TM
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Date useTm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.USE_TM_LOCAL
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Date useTmLocal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.EXPIRE
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Date expire;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.CREATED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.UPDATED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Date updated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.STAMP_CNT
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Integer stampCnt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.DISCOUNT_TP
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private String discountTp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_user_coupon.DISCOUNT
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    private Double discount;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.ID
     *
     * @return the value of tb_svc_user_coupon.ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.ID
     *
     * @param id the value for tb_svc_user_coupon.ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.USER_ID
     *
     * @return the value of tb_svc_user_coupon.USER_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.USER_ID
     *
     * @param userId the value for tb_svc_user_coupon.USER_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.PROMOTION_ID
     *
     * @return the value of tb_svc_user_coupon.PROMOTION_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Long getPromotionId() {
        return promotionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.PROMOTION_ID
     *
     * @param promotionId the value for tb_svc_user_coupon.PROMOTION_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setPromotionId(Long promotionId) {
        this.promotionId = promotionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.COUPON_ID
     *
     * @return the value of tb_svc_user_coupon.COUPON_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Long getCouponId() {
        return couponId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.COUPON_ID
     *
     * @param couponId the value for tb_svc_user_coupon.COUPON_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.BRAND_ID
     *
     * @return the value of tb_svc_user_coupon.BRAND_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.BRAND_ID
     *
     * @param brandId the value for tb_svc_user_coupon.BRAND_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.STORE_ID
     *
     * @return the value of tb_svc_user_coupon.STORE_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Long getStoreId() {
        return storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.STORE_ID
     *
     * @param storeId the value for tb_svc_user_coupon.STORE_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.COUPON_CD
     *
     * @return the value of tb_svc_user_coupon.COUPON_CD
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public String getCouponCd() {
        return couponCd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.COUPON_CD
     *
     * @param couponCd the value for tb_svc_user_coupon.COUPON_CD
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setCouponCd(String couponCd) {
        this.couponCd = couponCd == null ? null : couponCd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.COUPON_TP
     *
     * @return the value of tb_svc_user_coupon.COUPON_TP
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public String getCouponTp() {
        return couponTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.COUPON_TP
     *
     * @param couponTp the value for tb_svc_user_coupon.COUPON_TP
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setCouponTp(String couponTp) {
        this.couponTp = couponTp == null ? null : couponTp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.COUPON_ST
     *
     * @return the value of tb_svc_user_coupon.COUPON_ST
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public String getCouponSt() {
        return couponSt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.COUPON_ST
     *
     * @param couponSt the value for tb_svc_user_coupon.COUPON_ST
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setCouponSt(String couponSt) {
        this.couponSt = couponSt == null ? null : couponSt.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.USED
     *
     * @return the value of tb_svc_user_coupon.USED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Boolean getUsed() {
        return used;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.USED
     *
     * @param used the value for tb_svc_user_coupon.USED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setUsed(Boolean used) {
        this.used = used;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.USE_STORE_ID
     *
     * @return the value of tb_svc_user_coupon.USE_STORE_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Long getUseStoreId() {
        return useStoreId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.USE_STORE_ID
     *
     * @param useStoreId the value for tb_svc_user_coupon.USE_STORE_ID
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setUseStoreId(Long useStoreId) {
        this.useStoreId = useStoreId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.USE_TM
     *
     * @return the value of tb_svc_user_coupon.USE_TM
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Date getUseTm() {
        return useTm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.USE_TM
     *
     * @param useTm the value for tb_svc_user_coupon.USE_TM
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setUseTm(Date useTm) {
        this.useTm = useTm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.USE_TM_LOCAL
     *
     * @return the value of tb_svc_user_coupon.USE_TM_LOCAL
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Date getUseTmLocal() {
        return useTmLocal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.USE_TM_LOCAL
     *
     * @param useTmLocal the value for tb_svc_user_coupon.USE_TM_LOCAL
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setUseTmLocal(Date useTmLocal) {
        this.useTmLocal = useTmLocal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.EXPIRE
     *
     * @return the value of tb_svc_user_coupon.EXPIRE
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Date getExpire() {
        return expire;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.EXPIRE
     *
     * @param expire the value for tb_svc_user_coupon.EXPIRE
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setExpire(Date expire) {
        this.expire = expire;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.CREATED
     *
     * @return the value of tb_svc_user_coupon.CREATED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.CREATED
     *
     * @param created the value for tb_svc_user_coupon.CREATED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.UPDATED
     *
     * @return the value of tb_svc_user_coupon.UPDATED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.UPDATED
     *
     * @param updated the value for tb_svc_user_coupon.UPDATED
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.STAMP_CNT
     *
     * @return the value of tb_svc_user_coupon.STAMP_CNT
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Integer getStampCnt() {
        return stampCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.STAMP_CNT
     *
     * @param stampCnt the value for tb_svc_user_coupon.STAMP_CNT
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setStampCnt(Integer stampCnt) {
        this.stampCnt = stampCnt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.DISCOUNT_TP
     *
     * @return the value of tb_svc_user_coupon.DISCOUNT_TP
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public String getDiscountTp() {
        return discountTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.DISCOUNT_TP
     *
     * @param discountTp the value for tb_svc_user_coupon.DISCOUNT_TP
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setDiscountTp(String discountTp) {
        this.discountTp = discountTp == null ? null : discountTp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_user_coupon.DISCOUNT
     *
     * @return the value of tb_svc_user_coupon.DISCOUNT
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_user_coupon.DISCOUNT
     *
     * @param discount the value for tb_svc_user_coupon.DISCOUNT
     *
     * @mbggenerated Wed Dec 07 15:11:20 KST 2016
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }
}