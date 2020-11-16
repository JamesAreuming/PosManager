package com.jc.pico.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SvcStampLogExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public SvcStampLogExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("ID is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("ID is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Long value) {
            addCriterion("ID =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Long value) {
            addCriterion("ID <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Long value) {
            addCriterion("ID >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ID >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Long value) {
            addCriterion("ID <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Long value) {
            addCriterion("ID <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Long> values) {
            addCriterion("ID in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Long> values) {
            addCriterion("ID not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Long value1, Long value2) {
            addCriterion("ID between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Long value1, Long value2) {
            addCriterion("ID not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andStampIdIsNull() {
            addCriterion("STAMP_ID is null");
            return (Criteria) this;
        }

        public Criteria andStampIdIsNotNull() {
            addCriterion("STAMP_ID is not null");
            return (Criteria) this;
        }

        public Criteria andStampIdEqualTo(Long value) {
            addCriterion("STAMP_ID =", value, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdNotEqualTo(Long value) {
            addCriterion("STAMP_ID <>", value, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdGreaterThan(Long value) {
            addCriterion("STAMP_ID >", value, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdGreaterThanOrEqualTo(Long value) {
            addCriterion("STAMP_ID >=", value, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdLessThan(Long value) {
            addCriterion("STAMP_ID <", value, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdLessThanOrEqualTo(Long value) {
            addCriterion("STAMP_ID <=", value, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdIn(List<Long> values) {
            addCriterion("STAMP_ID in", values, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdNotIn(List<Long> values) {
            addCriterion("STAMP_ID not in", values, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdBetween(Long value1, Long value2) {
            addCriterion("STAMP_ID between", value1, value2, "stampId");
            return (Criteria) this;
        }

        public Criteria andStampIdNotBetween(Long value1, Long value2) {
            addCriterion("STAMP_ID not between", value1, value2, "stampId");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNull() {
            addCriterion("USER_ID is null");
            return (Criteria) this;
        }

        public Criteria andUserIdIsNotNull() {
            addCriterion("USER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andUserIdEqualTo(Long value) {
            addCriterion("USER_ID =", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotEqualTo(Long value) {
            addCriterion("USER_ID <>", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThan(Long value) {
            addCriterion("USER_ID >", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdGreaterThanOrEqualTo(Long value) {
            addCriterion("USER_ID >=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThan(Long value) {
            addCriterion("USER_ID <", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdLessThanOrEqualTo(Long value) {
            addCriterion("USER_ID <=", value, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdIn(List<Long> values) {
            addCriterion("USER_ID in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotIn(List<Long> values) {
            addCriterion("USER_ID not in", values, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdBetween(Long value1, Long value2) {
            addCriterion("USER_ID between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andUserIdNotBetween(Long value1, Long value2) {
            addCriterion("USER_ID not between", value1, value2, "userId");
            return (Criteria) this;
        }

        public Criteria andStoreIdIsNull() {
            addCriterion("STORE_ID is null");
            return (Criteria) this;
        }

        public Criteria andStoreIdIsNotNull() {
            addCriterion("STORE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andStoreIdEqualTo(Long value) {
            addCriterion("STORE_ID =", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdNotEqualTo(Long value) {
            addCriterion("STORE_ID <>", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdGreaterThan(Long value) {
            addCriterion("STORE_ID >", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdGreaterThanOrEqualTo(Long value) {
            addCriterion("STORE_ID >=", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdLessThan(Long value) {
            addCriterion("STORE_ID <", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdLessThanOrEqualTo(Long value) {
            addCriterion("STORE_ID <=", value, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdIn(List<Long> values) {
            addCriterion("STORE_ID in", values, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdNotIn(List<Long> values) {
            addCriterion("STORE_ID not in", values, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdBetween(Long value1, Long value2) {
            addCriterion("STORE_ID between", value1, value2, "storeId");
            return (Criteria) this;
        }

        public Criteria andStoreIdNotBetween(Long value1, Long value2) {
            addCriterion("STORE_ID not between", value1, value2, "storeId");
            return (Criteria) this;
        }

        public Criteria andBrandIdIsNull() {
            addCriterion("BRAND_ID is null");
            return (Criteria) this;
        }

        public Criteria andBrandIdIsNotNull() {
            addCriterion("BRAND_ID is not null");
            return (Criteria) this;
        }

        public Criteria andBrandIdEqualTo(Long value) {
            addCriterion("BRAND_ID =", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdNotEqualTo(Long value) {
            addCriterion("BRAND_ID <>", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdGreaterThan(Long value) {
            addCriterion("BRAND_ID >", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdGreaterThanOrEqualTo(Long value) {
            addCriterion("BRAND_ID >=", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdLessThan(Long value) {
            addCriterion("BRAND_ID <", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdLessThanOrEqualTo(Long value) {
            addCriterion("BRAND_ID <=", value, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdIn(List<Long> values) {
            addCriterion("BRAND_ID in", values, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdNotIn(List<Long> values) {
            addCriterion("BRAND_ID not in", values, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdBetween(Long value1, Long value2) {
            addCriterion("BRAND_ID between", value1, value2, "brandId");
            return (Criteria) this;
        }

        public Criteria andBrandIdNotBetween(Long value1, Long value2) {
            addCriterion("BRAND_ID not between", value1, value2, "brandId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNull() {
            addCriterion("ORDER_ID is null");
            return (Criteria) this;
        }

        public Criteria andOrderIdIsNotNull() {
            addCriterion("ORDER_ID is not null");
            return (Criteria) this;
        }

        public Criteria andOrderIdEqualTo(Long value) {
            addCriterion("ORDER_ID =", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotEqualTo(Long value) {
            addCriterion("ORDER_ID <>", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThan(Long value) {
            addCriterion("ORDER_ID >", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdGreaterThanOrEqualTo(Long value) {
            addCriterion("ORDER_ID >=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThan(Long value) {
            addCriterion("ORDER_ID <", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdLessThanOrEqualTo(Long value) {
            addCriterion("ORDER_ID <=", value, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdIn(List<Long> values) {
            addCriterion("ORDER_ID in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotIn(List<Long> values) {
            addCriterion("ORDER_ID not in", values, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdBetween(Long value1, Long value2) {
            addCriterion("ORDER_ID between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andOrderIdNotBetween(Long value1, Long value2) {
            addCriterion("ORDER_ID not between", value1, value2, "orderId");
            return (Criteria) this;
        }

        public Criteria andExpireIsNull() {
            addCriterion("EXPIRE is null");
            return (Criteria) this;
        }

        public Criteria andExpireIsNotNull() {
            addCriterion("EXPIRE is not null");
            return (Criteria) this;
        }

        public Criteria andExpireEqualTo(Date value) {
            addCriterion("EXPIRE =", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireNotEqualTo(Date value) {
            addCriterion("EXPIRE <>", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireGreaterThan(Date value) {
            addCriterion("EXPIRE >", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireGreaterThanOrEqualTo(Date value) {
            addCriterion("EXPIRE >=", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireLessThan(Date value) {
            addCriterion("EXPIRE <", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireLessThanOrEqualTo(Date value) {
            addCriterion("EXPIRE <=", value, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireIn(List<Date> values) {
            addCriterion("EXPIRE in", values, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireNotIn(List<Date> values) {
            addCriterion("EXPIRE not in", values, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireBetween(Date value1, Date value2) {
            addCriterion("EXPIRE between", value1, value2, "expire");
            return (Criteria) this;
        }

        public Criteria andExpireNotBetween(Date value1, Date value2) {
            addCriterion("EXPIRE not between", value1, value2, "expire");
            return (Criteria) this;
        }

        public Criteria andWhenIsNull() {
            addCriterion("`WHEN` is null");
            return (Criteria) this;
        }

        public Criteria andWhenIsNotNull() {
            addCriterion("`WHEN` is not null");
            return (Criteria) this;
        }

        public Criteria andWhenEqualTo(Date value) {
            addCriterion("`WHEN` =", value, "when");
            return (Criteria) this;
        }

        public Criteria andWhenNotEqualTo(Date value) {
            addCriterion("`WHEN` <>", value, "when");
            return (Criteria) this;
        }

        public Criteria andWhenGreaterThan(Date value) {
            addCriterion("`WHEN` >", value, "when");
            return (Criteria) this;
        }

        public Criteria andWhenGreaterThanOrEqualTo(Date value) {
            addCriterion("`WHEN` >=", value, "when");
            return (Criteria) this;
        }

        public Criteria andWhenLessThan(Date value) {
            addCriterion("`WHEN` <", value, "when");
            return (Criteria) this;
        }

        public Criteria andWhenLessThanOrEqualTo(Date value) {
            addCriterion("`WHEN` <=", value, "when");
            return (Criteria) this;
        }

        public Criteria andWhenIn(List<Date> values) {
            addCriterion("`WHEN` in", values, "when");
            return (Criteria) this;
        }

        public Criteria andWhenNotIn(List<Date> values) {
            addCriterion("`WHEN` not in", values, "when");
            return (Criteria) this;
        }

        public Criteria andWhenBetween(Date value1, Date value2) {
            addCriterion("`WHEN` between", value1, value2, "when");
            return (Criteria) this;
        }

        public Criteria andWhenNotBetween(Date value1, Date value2) {
            addCriterion("`WHEN` not between", value1, value2, "when");
            return (Criteria) this;
        }

        public Criteria andLogTpIsNull() {
            addCriterion("LOG_TP is null");
            return (Criteria) this;
        }

        public Criteria andLogTpIsNotNull() {
            addCriterion("LOG_TP is not null");
            return (Criteria) this;
        }

        public Criteria andLogTpEqualTo(String value) {
            addCriterion("LOG_TP =", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpNotEqualTo(String value) {
            addCriterion("LOG_TP <>", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpGreaterThan(String value) {
            addCriterion("LOG_TP >", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpGreaterThanOrEqualTo(String value) {
            addCriterion("LOG_TP >=", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpLessThan(String value) {
            addCriterion("LOG_TP <", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpLessThanOrEqualTo(String value) {
            addCriterion("LOG_TP <=", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpLike(String value) {
            addCriterion("LOG_TP like", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpNotLike(String value) {
            addCriterion("LOG_TP not like", value, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpIn(List<String> values) {
            addCriterion("LOG_TP in", values, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpNotIn(List<String> values) {
            addCriterion("LOG_TP not in", values, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpBetween(String value1, String value2) {
            addCriterion("LOG_TP between", value1, value2, "logTp");
            return (Criteria) this;
        }

        public Criteria andLogTpNotBetween(String value1, String value2) {
            addCriterion("LOG_TP not between", value1, value2, "logTp");
            return (Criteria) this;
        }

        public Criteria andSrcIpIsNull() {
            addCriterion("SRC_IP is null");
            return (Criteria) this;
        }

        public Criteria andSrcIpIsNotNull() {
            addCriterion("SRC_IP is not null");
            return (Criteria) this;
        }

        public Criteria andSrcIpEqualTo(String value) {
            addCriterion("SRC_IP =", value, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpNotEqualTo(String value) {
            addCriterion("SRC_IP <>", value, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpGreaterThan(String value) {
            addCriterion("SRC_IP >", value, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpGreaterThanOrEqualTo(String value) {
            addCriterion("SRC_IP >=", value, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpLessThan(String value) {
            addCriterion("SRC_IP <", value, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpLessThanOrEqualTo(String value) {
            addCriterion("SRC_IP <=", value, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpLike(String value) {
            addCriterion("SRC_IP like", value, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpNotLike(String value) {
            addCriterion("SRC_IP not like", value, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpIn(List<String> values) {
            addCriterion("SRC_IP in", values, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpNotIn(List<String> values) {
            addCriterion("SRC_IP not in", values, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpBetween(String value1, String value2) {
            addCriterion("SRC_IP between", value1, value2, "srcIp");
            return (Criteria) this;
        }

        public Criteria andSrcIpNotBetween(String value1, String value2) {
            addCriterion("SRC_IP not between", value1, value2, "srcIp");
            return (Criteria) this;
        }

        public Criteria andUserAgentIsNull() {
            addCriterion("USER_AGENT is null");
            return (Criteria) this;
        }

        public Criteria andUserAgentIsNotNull() {
            addCriterion("USER_AGENT is not null");
            return (Criteria) this;
        }

        public Criteria andUserAgentEqualTo(String value) {
            addCriterion("USER_AGENT =", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentNotEqualTo(String value) {
            addCriterion("USER_AGENT <>", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentGreaterThan(String value) {
            addCriterion("USER_AGENT >", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentGreaterThanOrEqualTo(String value) {
            addCriterion("USER_AGENT >=", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentLessThan(String value) {
            addCriterion("USER_AGENT <", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentLessThanOrEqualTo(String value) {
            addCriterion("USER_AGENT <=", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentLike(String value) {
            addCriterion("USER_AGENT like", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentNotLike(String value) {
            addCriterion("USER_AGENT not like", value, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentIn(List<String> values) {
            addCriterion("USER_AGENT in", values, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentNotIn(List<String> values) {
            addCriterion("USER_AGENT not in", values, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentBetween(String value1, String value2) {
            addCriterion("USER_AGENT between", value1, value2, "userAgent");
            return (Criteria) this;
        }

        public Criteria andUserAgentNotBetween(String value1, String value2) {
            addCriterion("USER_AGENT not between", value1, value2, "userAgent");
            return (Criteria) this;
        }

        public Criteria andClientIdIsNull() {
            addCriterion("CLIENT_ID is null");
            return (Criteria) this;
        }

        public Criteria andClientIdIsNotNull() {
            addCriterion("CLIENT_ID is not null");
            return (Criteria) this;
        }

        public Criteria andClientIdEqualTo(String value) {
            addCriterion("CLIENT_ID =", value, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdNotEqualTo(String value) {
            addCriterion("CLIENT_ID <>", value, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdGreaterThan(String value) {
            addCriterion("CLIENT_ID >", value, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdGreaterThanOrEqualTo(String value) {
            addCriterion("CLIENT_ID >=", value, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdLessThan(String value) {
            addCriterion("CLIENT_ID <", value, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdLessThanOrEqualTo(String value) {
            addCriterion("CLIENT_ID <=", value, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdLike(String value) {
            addCriterion("CLIENT_ID like", value, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdNotLike(String value) {
            addCriterion("CLIENT_ID not like", value, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdIn(List<String> values) {
            addCriterion("CLIENT_ID in", values, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdNotIn(List<String> values) {
            addCriterion("CLIENT_ID not in", values, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdBetween(String value1, String value2) {
            addCriterion("CLIENT_ID between", value1, value2, "clientId");
            return (Criteria) this;
        }

        public Criteria andClientIdNotBetween(String value1, String value2) {
            addCriterion("CLIENT_ID not between", value1, value2, "clientId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNull() {
            addCriterion("DEVICE_ID is null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIsNotNull() {
            addCriterion("DEVICE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andDeviceIdEqualTo(String value) {
            addCriterion("DEVICE_ID =", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotEqualTo(String value) {
            addCriterion("DEVICE_ID <>", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThan(String value) {
            addCriterion("DEVICE_ID >", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdGreaterThanOrEqualTo(String value) {
            addCriterion("DEVICE_ID >=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThan(String value) {
            addCriterion("DEVICE_ID <", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLessThanOrEqualTo(String value) {
            addCriterion("DEVICE_ID <=", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdLike(String value) {
            addCriterion("DEVICE_ID like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotLike(String value) {
            addCriterion("DEVICE_ID not like", value, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdIn(List<String> values) {
            addCriterion("DEVICE_ID in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotIn(List<String> values) {
            addCriterion("DEVICE_ID not in", values, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdBetween(String value1, String value2) {
            addCriterion("DEVICE_ID between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andDeviceIdNotBetween(String value1, String value2) {
            addCriterion("DEVICE_ID not between", value1, value2, "deviceId");
            return (Criteria) this;
        }

        public Criteria andGrantTypeIsNull() {
            addCriterion("GRANT_TYPE is null");
            return (Criteria) this;
        }

        public Criteria andGrantTypeIsNotNull() {
            addCriterion("GRANT_TYPE is not null");
            return (Criteria) this;
        }

        public Criteria andGrantTypeEqualTo(String value) {
            addCriterion("GRANT_TYPE =", value, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeNotEqualTo(String value) {
            addCriterion("GRANT_TYPE <>", value, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeGreaterThan(String value) {
            addCriterion("GRANT_TYPE >", value, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeGreaterThanOrEqualTo(String value) {
            addCriterion("GRANT_TYPE >=", value, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeLessThan(String value) {
            addCriterion("GRANT_TYPE <", value, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeLessThanOrEqualTo(String value) {
            addCriterion("GRANT_TYPE <=", value, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeLike(String value) {
            addCriterion("GRANT_TYPE like", value, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeNotLike(String value) {
            addCriterion("GRANT_TYPE not like", value, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeIn(List<String> values) {
            addCriterion("GRANT_TYPE in", values, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeNotIn(List<String> values) {
            addCriterion("GRANT_TYPE not in", values, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeBetween(String value1, String value2) {
            addCriterion("GRANT_TYPE between", value1, value2, "grantType");
            return (Criteria) this;
        }

        public Criteria andGrantTypeNotBetween(String value1, String value2) {
            addCriterion("GRANT_TYPE not between", value1, value2, "grantType");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated do_not_delete_during_merge Thu Aug 04 11:39:46 KST 2016
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table tb_svc_stamp_log
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}