package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcItemCat {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long brandId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long storeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.PARENT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long parent;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.CAT_CD
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String catCd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.ADMIN_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String adminId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_cat.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.ID
     *
     * @return the value of tb_svc_item_cat.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.ID
     *
     * @param id the value for tb_svc_item_cat.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.BRAND_ID
     *
     * @return the value of tb_svc_item_cat.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.BRAND_ID
     *
     * @param brandId the value for tb_svc_item_cat.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.STORE_ID
     *
     * @return the value of tb_svc_item_cat.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getStoreId() {
        return storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.STORE_ID
     *
     * @param storeId the value for tb_svc_item_cat.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.PARENT
     *
     * @return the value of tb_svc_item_cat.PARENT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getParent() {
        return parent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.PARENT
     *
     * @param parent the value for tb_svc_item_cat.PARENT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setParent(Long parent) {
        this.parent = parent;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.NAME
     *
     * @return the value of tb_svc_item_cat.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.NAME
     *
     * @param name the value for tb_svc_item_cat.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.CAT_CD
     *
     * @return the value of tb_svc_item_cat.CAT_CD
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getCatCd() {
        return catCd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.CAT_CD
     *
     * @param catCd the value for tb_svc_item_cat.CAT_CD
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setCatCd(String catCd) {
        this.catCd = catCd == null ? null : catCd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.ADMIN_ID
     *
     * @return the value of tb_svc_item_cat.ADMIN_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.ADMIN_ID
     *
     * @param adminId the value for tb_svc_item_cat.ADMIN_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setAdminId(String adminId) {
        this.adminId = adminId == null ? null : adminId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.CREATED
     *
     * @return the value of tb_svc_item_cat.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.CREATED
     *
     * @param created the value for tb_svc_item_cat.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_cat.UPDATED
     *
     * @return the value of tb_svc_item_cat.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_cat.UPDATED
     *
     * @param updated the value for tb_svc_item_cat.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}