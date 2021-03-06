package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcOrderItem {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long id;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long orderId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long ordinal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.LAST_ST
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String lastSt;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.PATH_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String pathTp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.SALES_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String salesTp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long staffId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.IS_STAMP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Boolean isStamp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.CAT_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String catNm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.CAT_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String catCd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Long itemId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ITEM_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String itemNm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ITEM_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String itemCd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ITEM_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String itemTp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double price;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.PURCHASE_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double purchasePrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.COUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Short count;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.SALES
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double sales;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.DISCOUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double discount;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.NET_SALES
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double netSales;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.SUPPLY_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double supplyPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.TAX
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double tax;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.SERVICE_CHARGE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double serviceCharge;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.TAX_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String taxTp;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ORDER_TM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Date orderTm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.ORDER_TM_LOCAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Date orderTmLocal;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.OPT_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String optId;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.OPT_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String optNm;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.OPT_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String optCd;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.OPT_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Double optPrice;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.SALES_DIV
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Byte salesDiv;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.SALES_TYPE_DIV
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Byte salesTypeDiv;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.IS_PACKING
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Boolean isPacking;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.MEMO
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private String memo;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.CREATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Date created;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column tb_svc_order_item.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    private Date updated;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ID
     *
     * @return the value of tb_svc_order_item.ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ID
     *
     * @param id the value for tb_svc_order_item.ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ORDER_ID
     *
     * @return the value of tb_svc_order_item.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getOrderId() {
        return orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ORDER_ID
     *
     * @param orderId the value for tb_svc_order_item.ORDER_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ORDINAL
     *
     * @return the value of tb_svc_order_item.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getOrdinal() {
        return ordinal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ORDINAL
     *
     * @param ordinal the value for tb_svc_order_item.ORDINAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOrdinal(Long ordinal) {
        this.ordinal = ordinal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.LAST_ST
     *
     * @return the value of tb_svc_order_item.LAST_ST
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getLastSt() {
        return lastSt;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.LAST_ST
     *
     * @param lastSt the value for tb_svc_order_item.LAST_ST
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setLastSt(String lastSt) {
        this.lastSt = lastSt == null ? null : lastSt.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.PATH_TP
     *
     * @return the value of tb_svc_order_item.PATH_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getPathTp() {
        return pathTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.PATH_TP
     *
     * @param pathTp the value for tb_svc_order_item.PATH_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setPathTp(String pathTp) {
        this.pathTp = pathTp == null ? null : pathTp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.SALES_TP
     *
     * @return the value of tb_svc_order_item.SALES_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getSalesTp() {
        return salesTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.SALES_TP
     *
     * @param salesTp the value for tb_svc_order_item.SALES_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setSalesTp(String salesTp) {
        this.salesTp = salesTp == null ? null : salesTp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.STAFF_ID
     *
     * @return the value of tb_svc_order_item.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getStaffId() {
        return staffId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.STAFF_ID
     *
     * @param staffId the value for tb_svc_order_item.STAFF_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.IS_STAMP
     *
     * @return the value of tb_svc_order_item.IS_STAMP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Boolean getIsStamp() {
        return isStamp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.IS_STAMP
     *
     * @param isStamp the value for tb_svc_order_item.IS_STAMP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setIsStamp(Boolean isStamp) {
        this.isStamp = isStamp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.CAT_NM
     *
     * @return the value of tb_svc_order_item.CAT_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getCatNm() {
        return catNm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.CAT_NM
     *
     * @param catNm the value for tb_svc_order_item.CAT_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setCatNm(String catNm) {
        this.catNm = catNm == null ? null : catNm.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.CAT_CD
     *
     * @return the value of tb_svc_order_item.CAT_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getCatCd() {
        return catCd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.CAT_CD
     *
     * @param catCd the value for tb_svc_order_item.CAT_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setCatCd(String catCd) {
        this.catCd = catCd == null ? null : catCd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ITEM_ID
     *
     * @return the value of tb_svc_order_item.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Long getItemId() {
        return itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ITEM_ID
     *
     * @param itemId the value for tb_svc_order_item.ITEM_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ITEM_NM
     *
     * @return the value of tb_svc_order_item.ITEM_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getItemNm() {
        return itemNm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ITEM_NM
     *
     * @param itemNm the value for tb_svc_order_item.ITEM_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setItemNm(String itemNm) {
        this.itemNm = itemNm == null ? null : itemNm.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ITEM_CD
     *
     * @return the value of tb_svc_order_item.ITEM_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getItemCd() {
        return itemCd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ITEM_CD
     *
     * @param itemCd the value for tb_svc_order_item.ITEM_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setItemCd(String itemCd) {
        this.itemCd = itemCd == null ? null : itemCd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ITEM_TP
     *
     * @return the value of tb_svc_order_item.ITEM_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getItemTp() {
        return itemTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ITEM_TP
     *
     * @param itemTp the value for tb_svc_order_item.ITEM_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setItemTp(String itemTp) {
        this.itemTp = itemTp == null ? null : itemTp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.PRICE
     *
     * @return the value of tb_svc_order_item.PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getPrice() {
        return price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.PRICE
     *
     * @param price the value for tb_svc_order_item.PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.PURCHASE_PRICE
     *
     * @return the value of tb_svc_order_item.PURCHASE_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getPurchasePrice() {
        return purchasePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.PURCHASE_PRICE
     *
     * @param purchasePrice the value for tb_svc_order_item.PURCHASE_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setPurchasePrice(Double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.COUNT
     *
     * @return the value of tb_svc_order_item.COUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Short getCount() {
        return count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.COUNT
     *
     * @param count the value for tb_svc_order_item.COUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setCount(Short count) {
        this.count = count;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.SALES
     *
     * @return the value of tb_svc_order_item.SALES
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getSales() {
        return sales;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.SALES
     *
     * @param sales the value for tb_svc_order_item.SALES
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setSales(Double sales) {
        this.sales = sales;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.DISCOUNT
     *
     * @return the value of tb_svc_order_item.DISCOUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.DISCOUNT
     *
     * @param discount the value for tb_svc_order_item.DISCOUNT
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.NET_SALES
     *
     * @return the value of tb_svc_order_item.NET_SALES
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getNetSales() {
        return netSales;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.NET_SALES
     *
     * @param netSales the value for tb_svc_order_item.NET_SALES
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setNetSales(Double netSales) {
        this.netSales = netSales;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.SUPPLY_PRICE
     *
     * @return the value of tb_svc_order_item.SUPPLY_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getSupplyPrice() {
        return supplyPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.SUPPLY_PRICE
     *
     * @param supplyPrice the value for tb_svc_order_item.SUPPLY_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setSupplyPrice(Double supplyPrice) {
        this.supplyPrice = supplyPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.TAX
     *
     * @return the value of tb_svc_order_item.TAX
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getTax() {
        return tax;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.TAX
     *
     * @param tax the value for tb_svc_order_item.TAX
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setTax(Double tax) {
        this.tax = tax;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.SERVICE_CHARGE
     *
     * @return the value of tb_svc_order_item.SERVICE_CHARGE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getServiceCharge() {
        return serviceCharge;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.SERVICE_CHARGE
     *
     * @param serviceCharge the value for tb_svc_order_item.SERVICE_CHARGE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setServiceCharge(Double serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.TAX_TP
     *
     * @return the value of tb_svc_order_item.TAX_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getTaxTp() {
        return taxTp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.TAX_TP
     *
     * @param taxTp the value for tb_svc_order_item.TAX_TP
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setTaxTp(String taxTp) {
        this.taxTp = taxTp == null ? null : taxTp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ORDER_TM
     *
     * @return the value of tb_svc_order_item.ORDER_TM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Date getOrderTm() {
        return orderTm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ORDER_TM
     *
     * @param orderTm the value for tb_svc_order_item.ORDER_TM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOrderTm(Date orderTm) {
        this.orderTm = orderTm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.ORDER_TM_LOCAL
     *
     * @return the value of tb_svc_order_item.ORDER_TM_LOCAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Date getOrderTmLocal() {
        return orderTmLocal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.ORDER_TM_LOCAL
     *
     * @param orderTmLocal the value for tb_svc_order_item.ORDER_TM_LOCAL
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOrderTmLocal(Date orderTmLocal) {
        this.orderTmLocal = orderTmLocal;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.OPT_ID
     *
     * @return the value of tb_svc_order_item.OPT_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getOptId() {
        return optId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.OPT_ID
     *
     * @param optId the value for tb_svc_order_item.OPT_ID
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOptId(String optId) {
        this.optId = optId == null ? null : optId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.OPT_NM
     *
     * @return the value of tb_svc_order_item.OPT_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getOptNm() {
        return optNm;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.OPT_NM
     *
     * @param optNm the value for tb_svc_order_item.OPT_NM
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOptNm(String optNm) {
        this.optNm = optNm == null ? null : optNm.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.OPT_CD
     *
     * @return the value of tb_svc_order_item.OPT_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getOptCd() {
        return optCd;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.OPT_CD
     *
     * @param optCd the value for tb_svc_order_item.OPT_CD
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOptCd(String optCd) {
        this.optCd = optCd == null ? null : optCd.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.OPT_PRICE
     *
     * @return the value of tb_svc_order_item.OPT_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Double getOptPrice() {
        return optPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.OPT_PRICE
     *
     * @param optPrice the value for tb_svc_order_item.OPT_PRICE
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setOptPrice(Double optPrice) {
        this.optPrice = optPrice;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.SALES_DIV
     *
     * @return the value of tb_svc_order_item.SALES_DIV
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Byte getSalesDiv() {
        return salesDiv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.SALES_DIV
     *
     * @param salesDiv the value for tb_svc_order_item.SALES_DIV
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setSalesDiv(Byte salesDiv) {
        this.salesDiv = salesDiv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.SALES_TYPE_DIV
     *
     * @return the value of tb_svc_order_item.SALES_TYPE_DIV
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Byte getSalesTypeDiv() {
        return salesTypeDiv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.SALES_TYPE_DIV
     *
     * @param salesTypeDiv the value for tb_svc_order_item.SALES_TYPE_DIV
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setSalesTypeDiv(Byte salesTypeDiv) {
        this.salesTypeDiv = salesTypeDiv;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.IS_PACKING
     *
     * @return the value of tb_svc_order_item.IS_PACKING
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Boolean getIsPacking() {
        return isPacking;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.IS_PACKING
     *
     * @param isPacking the value for tb_svc_order_item.IS_PACKING
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setIsPacking(Boolean isPacking) {
        this.isPacking = isPacking;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.MEMO
     *
     * @return the value of tb_svc_order_item.MEMO
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public String getMemo() {
        return memo;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.MEMO
     *
     * @param memo the value for tb_svc_order_item.MEMO
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setMemo(String memo) {
        this.memo = memo == null ? null : memo.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.CREATED
     *
     * @return the value of tb_svc_order_item.CREATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Date getCreated() {
        return created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.CREATED
     *
     * @param created the value for tb_svc_order_item.CREATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column tb_svc_order_item.UPDATED
     *
     * @return the value of tb_svc_order_item.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public Date getUpdated() {
        return updated;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column tb_svc_order_item.UPDATED
     *
     * @param updated the value for tb_svc_order_item.UPDATED
     *
     * @mbggenerated Thu Oct 27 10:43:21 KST 2016
     */
    public void setUpdated(Date updated) {
        this.updated = updated;
    }
}