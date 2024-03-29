package com.jc.pico.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class BaseCalendarExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public BaseCalendarExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
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
     * This method corresponds to the database table tb_base_calendar
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
     * This method corresponds to the database table tb_base_calendar
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_base_calendar
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
     * This class corresponds to the database table tb_base_calendar
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

        protected void addCriterionForJDBCDate(String condition, Date value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value.getTime()), property);
        }

        protected void addCriterionForJDBCDate(String condition, List<Date> values, String property) {
            if (values == null || values.size() == 0) {
                throw new RuntimeException("Value list for " + property + " cannot be null or empty");
            }
            List<java.sql.Date> dateList = new ArrayList<java.sql.Date>();
            Iterator<Date> iter = values.iterator();
            while (iter.hasNext()) {
                dateList.add(new java.sql.Date(iter.next().getTime()));
            }
            addCriterion(condition, dateList, property);
        }

        protected void addCriterionForJDBCDate(String condition, Date value1, Date value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            addCriterion(condition, new java.sql.Date(value1.getTime()), new java.sql.Date(value2.getTime()), property);
        }

        public Criteria andYmdIsNull() {
            addCriterion("YMD is null");
            return (Criteria) this;
        }

        public Criteria andYmdIsNotNull() {
            addCriterion("YMD is not null");
            return (Criteria) this;
        }

        public Criteria andYmdEqualTo(Date value) {
            addCriterionForJDBCDate("YMD =", value, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdNotEqualTo(Date value) {
            addCriterionForJDBCDate("YMD <>", value, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdGreaterThan(Date value) {
            addCriterionForJDBCDate("YMD >", value, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdGreaterThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("YMD >=", value, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdLessThan(Date value) {
            addCriterionForJDBCDate("YMD <", value, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdLessThanOrEqualTo(Date value) {
            addCriterionForJDBCDate("YMD <=", value, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdIn(List<Date> values) {
            addCriterionForJDBCDate("YMD in", values, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdNotIn(List<Date> values) {
            addCriterionForJDBCDate("YMD not in", values, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("YMD between", value1, value2, "ymd");
            return (Criteria) this;
        }

        public Criteria andYmdNotBetween(Date value1, Date value2) {
            addCriterionForJDBCDate("YMD not between", value1, value2, "ymd");
            return (Criteria) this;
        }

        public Criteria andYearIsNull() {
            addCriterion("`YEAR` is null");
            return (Criteria) this;
        }

        public Criteria andYearIsNotNull() {
            addCriterion("`YEAR` is not null");
            return (Criteria) this;
        }

        public Criteria andYearEqualTo(Integer value) {
            addCriterion("`YEAR` =", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotEqualTo(Integer value) {
            addCriterion("`YEAR` <>", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearGreaterThan(Integer value) {
            addCriterion("`YEAR` >", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearGreaterThanOrEqualTo(Integer value) {
            addCriterion("`YEAR` >=", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLessThan(Integer value) {
            addCriterion("`YEAR` <", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearLessThanOrEqualTo(Integer value) {
            addCriterion("`YEAR` <=", value, "year");
            return (Criteria) this;
        }

        public Criteria andYearIn(List<Integer> values) {
            addCriterion("`YEAR` in", values, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotIn(List<Integer> values) {
            addCriterion("`YEAR` not in", values, "year");
            return (Criteria) this;
        }

        public Criteria andYearBetween(Integer value1, Integer value2) {
            addCriterion("`YEAR` between", value1, value2, "year");
            return (Criteria) this;
        }

        public Criteria andYearNotBetween(Integer value1, Integer value2) {
            addCriterion("`YEAR` not between", value1, value2, "year");
            return (Criteria) this;
        }

        public Criteria andMonthIsNull() {
            addCriterion("`MONTH` is null");
            return (Criteria) this;
        }

        public Criteria andMonthIsNotNull() {
            addCriterion("`MONTH` is not null");
            return (Criteria) this;
        }

        public Criteria andMonthEqualTo(Integer value) {
            addCriterion("`MONTH` =", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthNotEqualTo(Integer value) {
            addCriterion("`MONTH` <>", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthGreaterThan(Integer value) {
            addCriterion("`MONTH` >", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthGreaterThanOrEqualTo(Integer value) {
            addCriterion("`MONTH` >=", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthLessThan(Integer value) {
            addCriterion("`MONTH` <", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthLessThanOrEqualTo(Integer value) {
            addCriterion("`MONTH` <=", value, "month");
            return (Criteria) this;
        }

        public Criteria andMonthIn(List<Integer> values) {
            addCriterion("`MONTH` in", values, "month");
            return (Criteria) this;
        }

        public Criteria andMonthNotIn(List<Integer> values) {
            addCriterion("`MONTH` not in", values, "month");
            return (Criteria) this;
        }

        public Criteria andMonthBetween(Integer value1, Integer value2) {
            addCriterion("`MONTH` between", value1, value2, "month");
            return (Criteria) this;
        }

        public Criteria andMonthNotBetween(Integer value1, Integer value2) {
            addCriterion("`MONTH` not between", value1, value2, "month");
            return (Criteria) this;
        }

        public Criteria andDayIsNull() {
            addCriterion("`DAY` is null");
            return (Criteria) this;
        }

        public Criteria andDayIsNotNull() {
            addCriterion("`DAY` is not null");
            return (Criteria) this;
        }

        public Criteria andDayEqualTo(Integer value) {
            addCriterion("`DAY` =", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotEqualTo(Integer value) {
            addCriterion("`DAY` <>", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayGreaterThan(Integer value) {
            addCriterion("`DAY` >", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayGreaterThanOrEqualTo(Integer value) {
            addCriterion("`DAY` >=", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayLessThan(Integer value) {
            addCriterion("`DAY` <", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayLessThanOrEqualTo(Integer value) {
            addCriterion("`DAY` <=", value, "day");
            return (Criteria) this;
        }

        public Criteria andDayIn(List<Integer> values) {
            addCriterion("`DAY` in", values, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotIn(List<Integer> values) {
            addCriterion("`DAY` not in", values, "day");
            return (Criteria) this;
        }

        public Criteria andDayBetween(Integer value1, Integer value2) {
            addCriterion("`DAY` between", value1, value2, "day");
            return (Criteria) this;
        }

        public Criteria andDayNotBetween(Integer value1, Integer value2) {
            addCriterion("`DAY` not between", value1, value2, "day");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekIsNull() {
            addCriterion("DAY_OF_WEEK is null");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekIsNotNull() {
            addCriterion("DAY_OF_WEEK is not null");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekEqualTo(Integer value) {
            addCriterion("DAY_OF_WEEK =", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekNotEqualTo(Integer value) {
            addCriterion("DAY_OF_WEEK <>", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekGreaterThan(Integer value) {
            addCriterion("DAY_OF_WEEK >", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekGreaterThanOrEqualTo(Integer value) {
            addCriterion("DAY_OF_WEEK >=", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekLessThan(Integer value) {
            addCriterion("DAY_OF_WEEK <", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekLessThanOrEqualTo(Integer value) {
            addCriterion("DAY_OF_WEEK <=", value, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekIn(List<Integer> values) {
            addCriterion("DAY_OF_WEEK in", values, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekNotIn(List<Integer> values) {
            addCriterion("DAY_OF_WEEK not in", values, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekBetween(Integer value1, Integer value2) {
            addCriterion("DAY_OF_WEEK between", value1, value2, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andDayOfWeekNotBetween(Integer value1, Integer value2) {
            addCriterion("DAY_OF_WEEK not between", value1, value2, "dayOfWeek");
            return (Criteria) this;
        }

        public Criteria andWeekIsNull() {
            addCriterion("WEEK is null");
            return (Criteria) this;
        }

        public Criteria andWeekIsNotNull() {
            addCriterion("WEEK is not null");
            return (Criteria) this;
        }

        public Criteria andWeekEqualTo(Integer value) {
            addCriterion("WEEK =", value, "week");
            return (Criteria) this;
        }

        public Criteria andWeekNotEqualTo(Integer value) {
            addCriterion("WEEK <>", value, "week");
            return (Criteria) this;
        }

        public Criteria andWeekGreaterThan(Integer value) {
            addCriterion("WEEK >", value, "week");
            return (Criteria) this;
        }

        public Criteria andWeekGreaterThanOrEqualTo(Integer value) {
            addCriterion("WEEK >=", value, "week");
            return (Criteria) this;
        }

        public Criteria andWeekLessThan(Integer value) {
            addCriterion("WEEK <", value, "week");
            return (Criteria) this;
        }

        public Criteria andWeekLessThanOrEqualTo(Integer value) {
            addCriterion("WEEK <=", value, "week");
            return (Criteria) this;
        }

        public Criteria andWeekIn(List<Integer> values) {
            addCriterion("WEEK in", values, "week");
            return (Criteria) this;
        }

        public Criteria andWeekNotIn(List<Integer> values) {
            addCriterion("WEEK not in", values, "week");
            return (Criteria) this;
        }

        public Criteria andWeekBetween(Integer value1, Integer value2) {
            addCriterion("WEEK between", value1, value2, "week");
            return (Criteria) this;
        }

        public Criteria andWeekNotBetween(Integer value1, Integer value2) {
            addCriterion("WEEK not between", value1, value2, "week");
            return (Criteria) this;
        }

        public Criteria andYearweekIsNull() {
            addCriterion("YEARWEEK is null");
            return (Criteria) this;
        }

        public Criteria andYearweekIsNotNull() {
            addCriterion("YEARWEEK is not null");
            return (Criteria) this;
        }

        public Criteria andYearweekEqualTo(Integer value) {
            addCriterion("YEARWEEK =", value, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekNotEqualTo(Integer value) {
            addCriterion("YEARWEEK <>", value, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekGreaterThan(Integer value) {
            addCriterion("YEARWEEK >", value, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekGreaterThanOrEqualTo(Integer value) {
            addCriterion("YEARWEEK >=", value, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekLessThan(Integer value) {
            addCriterion("YEARWEEK <", value, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekLessThanOrEqualTo(Integer value) {
            addCriterion("YEARWEEK <=", value, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekIn(List<Integer> values) {
            addCriterion("YEARWEEK in", values, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekNotIn(List<Integer> values) {
            addCriterion("YEARWEEK not in", values, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekBetween(Integer value1, Integer value2) {
            addCriterion("YEARWEEK between", value1, value2, "yearweek");
            return (Criteria) this;
        }

        public Criteria andYearweekNotBetween(Integer value1, Integer value2) {
            addCriterion("YEARWEEK not between", value1, value2, "yearweek");
            return (Criteria) this;
        }
    }

    /**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table tb_base_calendar
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
     * This class corresponds to the database table tb_base_calendar
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