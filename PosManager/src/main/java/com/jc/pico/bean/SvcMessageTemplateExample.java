package com.jc.pico.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SvcMessageTemplateExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public SvcMessageTemplateExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
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
     * This method corresponds to the database table tb_svc_message_template
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
     * This method corresponds to the database table tb_svc_message_template
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_svc_message_template
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
     * This class corresponds to the database table tb_svc_message_template
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

        public Criteria andServiceIdIsNull() {
            addCriterion("SERVICE_ID is null");
            return (Criteria) this;
        }

        public Criteria andServiceIdIsNotNull() {
            addCriterion("SERVICE_ID is not null");
            return (Criteria) this;
        }

        public Criteria andServiceIdEqualTo(Long value) {
            addCriterion("SERVICE_ID =", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotEqualTo(Long value) {
            addCriterion("SERVICE_ID <>", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThan(Long value) {
            addCriterion("SERVICE_ID >", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdGreaterThanOrEqualTo(Long value) {
            addCriterion("SERVICE_ID >=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThan(Long value) {
            addCriterion("SERVICE_ID <", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdLessThanOrEqualTo(Long value) {
            addCriterion("SERVICE_ID <=", value, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdIn(List<Long> values) {
            addCriterion("SERVICE_ID in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotIn(List<Long> values) {
            addCriterion("SERVICE_ID not in", values, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdBetween(Long value1, Long value2) {
            addCriterion("SERVICE_ID between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andServiceIdNotBetween(Long value1, Long value2) {
            addCriterion("SERVICE_ID not between", value1, value2, "serviceId");
            return (Criteria) this;
        }

        public Criteria andMessageTpIsNull() {
            addCriterion("MESSAGE_TP is null");
            return (Criteria) this;
        }

        public Criteria andMessageTpIsNotNull() {
            addCriterion("MESSAGE_TP is not null");
            return (Criteria) this;
        }

        public Criteria andMessageTpEqualTo(String value) {
            addCriterion("MESSAGE_TP =", value, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpNotEqualTo(String value) {
            addCriterion("MESSAGE_TP <>", value, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpGreaterThan(String value) {
            addCriterion("MESSAGE_TP >", value, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpGreaterThanOrEqualTo(String value) {
            addCriterion("MESSAGE_TP >=", value, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpLessThan(String value) {
            addCriterion("MESSAGE_TP <", value, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpLessThanOrEqualTo(String value) {
            addCriterion("MESSAGE_TP <=", value, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpLike(String value) {
            addCriterion("MESSAGE_TP like", value, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpNotLike(String value) {
            addCriterion("MESSAGE_TP not like", value, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpIn(List<String> values) {
            addCriterion("MESSAGE_TP in", values, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpNotIn(List<String> values) {
            addCriterion("MESSAGE_TP not in", values, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpBetween(String value1, String value2) {
            addCriterion("MESSAGE_TP between", value1, value2, "messageTp");
            return (Criteria) this;
        }

        public Criteria andMessageTpNotBetween(String value1, String value2) {
            addCriterion("MESSAGE_TP not between", value1, value2, "messageTp");
            return (Criteria) this;
        }

        public Criteria andUsedIsNull() {
            addCriterion("USED is null");
            return (Criteria) this;
        }

        public Criteria andUsedIsNotNull() {
            addCriterion("USED is not null");
            return (Criteria) this;
        }

        public Criteria andUsedEqualTo(Boolean value) {
            addCriterion("USED =", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedNotEqualTo(Boolean value) {
            addCriterion("USED <>", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedGreaterThan(Boolean value) {
            addCriterion("USED >", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedGreaterThanOrEqualTo(Boolean value) {
            addCriterion("USED >=", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedLessThan(Boolean value) {
            addCriterion("USED <", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedLessThanOrEqualTo(Boolean value) {
            addCriterion("USED <=", value, "used");
            return (Criteria) this;
        }

        public Criteria andUsedIn(List<Boolean> values) {
            addCriterion("USED in", values, "used");
            return (Criteria) this;
        }

        public Criteria andUsedNotIn(List<Boolean> values) {
            addCriterion("USED not in", values, "used");
            return (Criteria) this;
        }

        public Criteria andUsedBetween(Boolean value1, Boolean value2) {
            addCriterion("USED between", value1, value2, "used");
            return (Criteria) this;
        }

        public Criteria andUsedNotBetween(Boolean value1, Boolean value2) {
            addCriterion("USED not between", value1, value2, "used");
            return (Criteria) this;
        }

        public Criteria andTitleIsNull() {
            addCriterion("TITLE is null");
            return (Criteria) this;
        }

        public Criteria andTitleIsNotNull() {
            addCriterion("TITLE is not null");
            return (Criteria) this;
        }

        public Criteria andTitleEqualTo(String value) {
            addCriterion("TITLE =", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotEqualTo(String value) {
            addCriterion("TITLE <>", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThan(String value) {
            addCriterion("TITLE >", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleGreaterThanOrEqualTo(String value) {
            addCriterion("TITLE >=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThan(String value) {
            addCriterion("TITLE <", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLessThanOrEqualTo(String value) {
            addCriterion("TITLE <=", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleLike(String value) {
            addCriterion("TITLE like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotLike(String value) {
            addCriterion("TITLE not like", value, "title");
            return (Criteria) this;
        }

        public Criteria andTitleIn(List<String> values) {
            addCriterion("TITLE in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotIn(List<String> values) {
            addCriterion("TITLE not in", values, "title");
            return (Criteria) this;
        }

        public Criteria andTitleBetween(String value1, String value2) {
            addCriterion("TITLE between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andTitleNotBetween(String value1, String value2) {
            addCriterion("TITLE not between", value1, value2, "title");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNull() {
            addCriterion("CREATED is null");
            return (Criteria) this;
        }

        public Criteria andCreatedIsNotNull() {
            addCriterion("CREATED is not null");
            return (Criteria) this;
        }

        public Criteria andCreatedEqualTo(Date value) {
            addCriterion("CREATED =", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotEqualTo(Date value) {
            addCriterion("CREATED <>", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThan(Date value) {
            addCriterion("CREATED >", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedGreaterThanOrEqualTo(Date value) {
            addCriterion("CREATED >=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThan(Date value) {
            addCriterion("CREATED <", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedLessThanOrEqualTo(Date value) {
            addCriterion("CREATED <=", value, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedIn(List<Date> values) {
            addCriterion("CREATED in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotIn(List<Date> values) {
            addCriterion("CREATED not in", values, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedBetween(Date value1, Date value2) {
            addCriterion("CREATED between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andCreatedNotBetween(Date value1, Date value2) {
            addCriterion("CREATED not between", value1, value2, "created");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNull() {
            addCriterion("UPDATED is null");
            return (Criteria) this;
        }

        public Criteria andUpdatedIsNotNull() {
            addCriterion("UPDATED is not null");
            return (Criteria) this;
        }

        public Criteria andUpdatedEqualTo(Date value) {
            addCriterion("UPDATED =", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotEqualTo(Date value) {
            addCriterion("UPDATED <>", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThan(Date value) {
            addCriterion("UPDATED >", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedGreaterThanOrEqualTo(Date value) {
            addCriterion("UPDATED >=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThan(Date value) {
            addCriterion("UPDATED <", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedLessThanOrEqualTo(Date value) {
            addCriterion("UPDATED <=", value, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedIn(List<Date> values) {
            addCriterion("UPDATED in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotIn(List<Date> values) {
            addCriterion("UPDATED not in", values, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedBetween(Date value1, Date value2) {
            addCriterion("UPDATED between", value1, value2, "updated");
            return (Criteria) this;
        }

        public Criteria andUpdatedNotBetween(Date value1, Date value2) {
            addCriterion("UPDATED not between", value1, value2, "updated");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table tb_svc_message_template
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
     * This class corresponds to the database table tb_svc_message_template
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