package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserBookmark {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_bookmark.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_bookmark.USER_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long userId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_bookmark.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long brandId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_bookmark.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long storeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_bookmark.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_user_bookmark.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_bookmark.ID
     *
     * @return the value of tb_user_bookmark.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_bookmark.ID
     *
     * @param id the value for tb_user_bookmark.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_bookmark.USER_ID
     *
     * @return the value of tb_user_bookmark.USER_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_bookmark.USER_ID
     *
     * @param userId the value for tb_user_bookmark.USER_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_bookmark.BRAND_ID
     *
     * @return the value of tb_user_bookmark.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_bookmark.BRAND_ID
     *
     * @param brandId the value for tb_user_bookmark.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_bookmark.STORE_ID
     *
     * @return the value of tb_user_bookmark.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getStoreId() {
        return storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_bookmark.STORE_ID
     *
     * @param storeId the value for tb_user_bookmark.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_bookmark.CREATED
     *
     * @return the value of tb_user_bookmark.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_bookmark.CREATED
     *
     * @param created the value for tb_user_bookmark.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_user_bookmark.UPDATED
     *
     * @return the value of tb_user_bookmark.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_user_bookmark.UPDATED
     *
     * @param updated the value for tb_user_bookmark.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}