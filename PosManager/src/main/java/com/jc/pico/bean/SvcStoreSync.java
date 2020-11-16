package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcStoreSync {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long brandId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Long storeId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.STORE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date store;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.ITEM_CAT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date itemCat;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.ITEM
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date item;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.PLU_CAT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date pluCat;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.PLU
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date plu;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.STAFF
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date staff;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_store_sync.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.ID
     *
     * @return the value of tb_svc_store_sync.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.ID
     *
     * @param id the value for tb_svc_store_sync.ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.BRAND_ID
     *
     * @return the value of tb_svc_store_sync.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getBrandId() {
        return brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.BRAND_ID
     *
     * @param brandId the value for tb_svc_store_sync.BRAND_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.STORE_ID
     *
     * @return the value of tb_svc_store_sync.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Long getStoreId() {
        return storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.STORE_ID
     *
     * @param storeId the value for tb_svc_store_sync.STORE_ID
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setStoreId(Long storeId) {
        this.storeId = storeId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.STORE
     *
     * @return the value of tb_svc_store_sync.STORE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getStore() {
        return store;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.STORE
     *
     * @param store the value for tb_svc_store_sync.STORE
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setStore(Date store) {
        this.store = store;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.ITEM_CAT
     *
     * @return the value of tb_svc_store_sync.ITEM_CAT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getItemCat() {
        return itemCat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.ITEM_CAT
     *
     * @param itemCat the value for tb_svc_store_sync.ITEM_CAT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setItemCat(Date itemCat) {
        this.itemCat = itemCat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.ITEM
     *
     * @return the value of tb_svc_store_sync.ITEM
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getItem() {
        return item;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.ITEM
     *
     * @param item the value for tb_svc_store_sync.ITEM
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setItem(Date item) {
        this.item = item;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.PLU_CAT
     *
     * @return the value of tb_svc_store_sync.PLU_CAT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getPluCat() {
        return pluCat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.PLU_CAT
     *
     * @param pluCat the value for tb_svc_store_sync.PLU_CAT
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setPluCat(Date pluCat) {
        this.pluCat = pluCat;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.PLU
     *
     * @return the value of tb_svc_store_sync.PLU
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getPlu() {
        return plu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.PLU
     *
     * @param plu the value for tb_svc_store_sync.PLU
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setPlu(Date plu) {
        this.plu = plu;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.STAFF
     *
     * @return the value of tb_svc_store_sync.STAFF
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getStaff() {
        return staff;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.STAFF
     *
     * @param staff the value for tb_svc_store_sync.STAFF
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setStaff(Date staff) {
        this.staff = staff;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.CREATED
     *
     * @return the value of tb_svc_store_sync.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.CREATED
     *
     * @param created the value for tb_svc_store_sync.CREATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_store_sync.UPDATED
     *
     * @return the value of tb_svc_store_sync.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_store_sync.UPDATED
     *
     * @param updated the value for tb_svc_store_sync.UPDATED
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}