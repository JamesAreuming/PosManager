package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcItemOpt {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.ITEM_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long itemId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long brandId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long storeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.IS_USED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Boolean isUsed;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String name;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.DESC
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String desc;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.IS_MANDATORY
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Boolean isMandatory;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.OPT_COUNT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Short optCount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.ORDINAL
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Byte ordinal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date updated;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_item_opt.TB_SVC_ITEM_OPTcol
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private String tbSvcItemOptcol;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.ID
     *
     * @return the value of tb_svc_item_opt.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.ID
     *
     * @param id the value for tb_svc_item_opt.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.ITEM_ID
     *
     * @return the value of tb_svc_item_opt.ITEM_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.ITEM_ID
     *
     * @param itemId the value for tb_svc_item_opt.ITEM_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.BRAND_ID
     *
     * @return the value of tb_svc_item_opt.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.BRAND_ID
     *
     * @param brandId the value for tb_svc_item_opt.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.STORE_ID
     *
     * @return the value of tb_svc_item_opt.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getStoreId() {
        return storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.STORE_ID
     *
     * @param storeId the value for tb_svc_item_opt.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.IS_USED
     *
     * @return the value of tb_svc_item_opt.IS_USED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Boolean getIsUsed() {
        return isUsed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.IS_USED
     *
     * @param isUsed the value for tb_svc_item_opt.IS_USED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setIsUsed(Boolean isUsed) {
        this.isUsed = isUsed;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.NAME
     *
     * @return the value of tb_svc_item_opt.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.NAME
     *
     * @param name the value for tb_svc_item_opt.NAME
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.DESC
     *
     * @return the value of tb_svc_item_opt.DESC
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getDesc() {
        return desc;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.DESC
     *
     * @param desc the value for tb_svc_item_opt.DESC
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setDesc(String desc) {
        this.desc = desc == null ? null : desc.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.IS_MANDATORY
     *
     * @return the value of tb_svc_item_opt.IS_MANDATORY
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Boolean getIsMandatory() {
        return isMandatory;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.IS_MANDATORY
     *
     * @param isMandatory the value for tb_svc_item_opt.IS_MANDATORY
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.OPT_COUNT
     *
     * @return the value of tb_svc_item_opt.OPT_COUNT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Short getOptCount() {
        return optCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.OPT_COUNT
     *
     * @param optCount the value for tb_svc_item_opt.OPT_COUNT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setOptCount(Short optCount) {
        this.optCount = optCount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.ORDINAL
     *
     * @return the value of tb_svc_item_opt.ORDINAL
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Byte getOrdinal() {
        return ordinal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.ORDINAL
     *
     * @param ordinal the value for tb_svc_item_opt.ORDINAL
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setOrdinal(Byte ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.CREATED
     *
     * @return the value of tb_svc_item_opt.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.CREATED
     *
     * @param created the value for tb_svc_item_opt.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.UPDATED
     *
     * @return the value of tb_svc_item_opt.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_item_opt.UPDATED
     *
     * @param updated the value for tb_svc_item_opt.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_item_opt.TB_SVC_ITEM_OPTcol
     *
     * @return the value of tb_svc_item_opt.TB_SVC_ITEM_OPTcol
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
}