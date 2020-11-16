package com.jc.pico.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NoticeExample {
    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected String orderByClause;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected boolean distinct;

    /**
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected List<Criteria> oredCriteria;

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public NoticeExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public String getOrderByClause() {
        return orderByClause;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public boolean isDistinct() {
        return distinct;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
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
     * This method corresponds to the database table tb_notice
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
     * This method corresponds to the database table tb_notice
     *
     * @mbggenerated Thu Aug 04 11:39:46 KST 2016
     */
    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tb_notice
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
     * This class corresponds to the database table tb_notice
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

        public Criteria andPlatformIdIsNull() {
            addCriterion("PLATFORM_ID is null");
            return (Criteria) this;
        }

        public Criteria andPlatformIdIsNotNull() {
            addCriterion("PLATFORM_ID is not null");
            return (Criteria) this;
        }

        public Criteria andPlatformIdEqualTo(Long value) {
            addCriterion("PLATFORM_ID =", value, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdNotEqualTo(Long value) {
            addCriterion("PLATFORM_ID <>", value, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdGreaterThan(Long value) {
            addCriterion("PLATFORM_ID >", value, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdGreaterThanOrEqualTo(Long value) {
            addCriterion("PLATFORM_ID >=", value, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdLessThan(Long value) {
            addCriterion("PLATFORM_ID <", value, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdLessThanOrEqualTo(Long value) {
            addCriterion("PLATFORM_ID <=", value, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdIn(List<Long> values) {
            addCriterion("PLATFORM_ID in", values, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdNotIn(List<Long> values) {
            addCriterion("PLATFORM_ID not in", values, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdBetween(Long value1, Long value2) {
            addCriterion("PLATFORM_ID between", value1, value2, "platformId");
            return (Criteria) this;
        }

        public Criteria andPlatformIdNotBetween(Long value1, Long value2) {
            addCriterion("PLATFORM_ID not between", value1, value2, "platformId");
            return (Criteria) this;
        }

        public Criteria andNoticeTpIsNull() {
            addCriterion("NOTICE_TP is null");
            return (Criteria) this;
        }

        public Criteria andNoticeTpIsNotNull() {
            addCriterion("NOTICE_TP is not null");
            return (Criteria) this;
        }

        public Criteria andNoticeTpEqualTo(String value) {
            addCriterion("NOTICE_TP =", value, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpNotEqualTo(String value) {
            addCriterion("NOTICE_TP <>", value, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpGreaterThan(String value) {
            addCriterion("NOTICE_TP >", value, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpGreaterThanOrEqualTo(String value) {
            addCriterion("NOTICE_TP >=", value, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpLessThan(String value) {
            addCriterion("NOTICE_TP <", value, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpLessThanOrEqualTo(String value) {
            addCriterion("NOTICE_TP <=", value, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpLike(String value) {
            addCriterion("NOTICE_TP like", value, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpNotLike(String value) {
            addCriterion("NOTICE_TP not like", value, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpIn(List<String> values) {
            addCriterion("NOTICE_TP in", values, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpNotIn(List<String> values) {
            addCriterion("NOTICE_TP not in", values, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpBetween(String value1, String value2) {
            addCriterion("NOTICE_TP between", value1, value2, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeTpNotBetween(String value1, String value2) {
            addCriterion("NOTICE_TP not between", value1, value2, "noticeTp");
            return (Criteria) this;
        }

        public Criteria andNoticeStIsNull() {
            addCriterion("NOTICE_ST is null");
            return (Criteria) this;
        }

        public Criteria andNoticeStIsNotNull() {
            addCriterion("NOTICE_ST is not null");
            return (Criteria) this;
        }

        public Criteria andNoticeStEqualTo(String value) {
            addCriterion("NOTICE_ST =", value, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStNotEqualTo(String value) {
            addCriterion("NOTICE_ST <>", value, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStGreaterThan(String value) {
            addCriterion("NOTICE_ST >", value, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStGreaterThanOrEqualTo(String value) {
            addCriterion("NOTICE_ST >=", value, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStLessThan(String value) {
            addCriterion("NOTICE_ST <", value, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStLessThanOrEqualTo(String value) {
            addCriterion("NOTICE_ST <=", value, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStLike(String value) {
            addCriterion("NOTICE_ST like", value, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStNotLike(String value) {
            addCriterion("NOTICE_ST not like", value, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStIn(List<String> values) {
            addCriterion("NOTICE_ST in", values, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStNotIn(List<String> values) {
            addCriterion("NOTICE_ST not in", values, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStBetween(String value1, String value2) {
            addCriterion("NOTICE_ST between", value1, value2, "noticeSt");
            return (Criteria) this;
        }

        public Criteria andNoticeStNotBetween(String value1, String value2) {
            addCriterion("NOTICE_ST not between", value1, value2, "noticeSt");
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

        public Criteria andEventImgPathIsNull() {
            addCriterion("EVENT_IMG_PATH is null");
            return (Criteria) this;
        }

        public Criteria andEventImgPathIsNotNull() {
            addCriterion("EVENT_IMG_PATH is not null");
            return (Criteria) this;
        }

        public Criteria andEventImgPathEqualTo(String value) {
            addCriterion("EVENT_IMG_PATH =", value, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathNotEqualTo(String value) {
            addCriterion("EVENT_IMG_PATH <>", value, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathGreaterThan(String value) {
            addCriterion("EVENT_IMG_PATH >", value, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathGreaterThanOrEqualTo(String value) {
            addCriterion("EVENT_IMG_PATH >=", value, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathLessThan(String value) {
            addCriterion("EVENT_IMG_PATH <", value, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathLessThanOrEqualTo(String value) {
            addCriterion("EVENT_IMG_PATH <=", value, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathLike(String value) {
            addCriterion("EVENT_IMG_PATH like", value, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathNotLike(String value) {
            addCriterion("EVENT_IMG_PATH not like", value, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathIn(List<String> values) {
            addCriterion("EVENT_IMG_PATH in", values, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathNotIn(List<String> values) {
            addCriterion("EVENT_IMG_PATH not in", values, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathBetween(String value1, String value2) {
            addCriterion("EVENT_IMG_PATH between", value1, value2, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andEventImgPathNotBetween(String value1, String value2) {
            addCriterion("EVENT_IMG_PATH not between", value1, value2, "eventImgPath");
            return (Criteria) this;
        }

        public Criteria andOpenIsNull() {
            addCriterion("`OPEN` is null");
            return (Criteria) this;
        }

        public Criteria andOpenIsNotNull() {
            addCriterion("`OPEN` is not null");
            return (Criteria) this;
        }

        public Criteria andOpenEqualTo(Date value) {
            addCriterion("`OPEN` =", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenNotEqualTo(Date value) {
            addCriterion("`OPEN` <>", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenGreaterThan(Date value) {
            addCriterion("`OPEN` >", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenGreaterThanOrEqualTo(Date value) {
            addCriterion("`OPEN` >=", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenLessThan(Date value) {
            addCriterion("`OPEN` <", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenLessThanOrEqualTo(Date value) {
            addCriterion("`OPEN` <=", value, "open");
            return (Criteria) this;
        }

        public Criteria andOpenIn(List<Date> values) {
            addCriterion("`OPEN` in", values, "open");
            return (Criteria) this;
        }

        public Criteria andOpenNotIn(List<Date> values) {
            addCriterion("`OPEN` not in", values, "open");
            return (Criteria) this;
        }

        public Criteria andOpenBetween(Date value1, Date value2) {
            addCriterion("`OPEN` between", value1, value2, "open");
            return (Criteria) this;
        }

        public Criteria andOpenNotBetween(Date value1, Date value2) {
            addCriterion("`OPEN` not between", value1, value2, "open");
            return (Criteria) this;
        }

        public Criteria andCloseIsNull() {
            addCriterion("`CLOSE` is null");
            return (Criteria) this;
        }

        public Criteria andCloseIsNotNull() {
            addCriterion("`CLOSE` is not null");
            return (Criteria) this;
        }

        public Criteria andCloseEqualTo(Date value) {
            addCriterion("`CLOSE` =", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseNotEqualTo(Date value) {
            addCriterion("`CLOSE` <>", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseGreaterThan(Date value) {
            addCriterion("`CLOSE` >", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseGreaterThanOrEqualTo(Date value) {
            addCriterion("`CLOSE` >=", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseLessThan(Date value) {
            addCriterion("`CLOSE` <", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseLessThanOrEqualTo(Date value) {
            addCriterion("`CLOSE` <=", value, "close");
            return (Criteria) this;
        }

        public Criteria andCloseIn(List<Date> values) {
            addCriterion("`CLOSE` in", values, "close");
            return (Criteria) this;
        }

        public Criteria andCloseNotIn(List<Date> values) {
            addCriterion("`CLOSE` not in", values, "close");
            return (Criteria) this;
        }

        public Criteria andCloseBetween(Date value1, Date value2) {
            addCriterion("`CLOSE` between", value1, value2, "close");
            return (Criteria) this;
        }

        public Criteria andCloseNotBetween(Date value1, Date value2) {
            addCriterion("`CLOSE` not between", value1, value2, "close");
            return (Criteria) this;
        }

        public Criteria andShowInAppIsNull() {
            addCriterion("SHOW_IN_APP is null");
            return (Criteria) this;
        }

        public Criteria andShowInAppIsNotNull() {
            addCriterion("SHOW_IN_APP is not null");
            return (Criteria) this;
        }

        public Criteria andShowInAppEqualTo(Boolean value) {
            addCriterion("SHOW_IN_APP =", value, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppNotEqualTo(Boolean value) {
            addCriterion("SHOW_IN_APP <>", value, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppGreaterThan(Boolean value) {
            addCriterion("SHOW_IN_APP >", value, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppGreaterThanOrEqualTo(Boolean value) {
            addCriterion("SHOW_IN_APP >=", value, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppLessThan(Boolean value) {
            addCriterion("SHOW_IN_APP <", value, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppLessThanOrEqualTo(Boolean value) {
            addCriterion("SHOW_IN_APP <=", value, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppIn(List<Boolean> values) {
            addCriterion("SHOW_IN_APP in", values, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppNotIn(List<Boolean> values) {
            addCriterion("SHOW_IN_APP not in", values, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppBetween(Boolean value1, Boolean value2) {
            addCriterion("SHOW_IN_APP between", value1, value2, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInAppNotBetween(Boolean value1, Boolean value2) {
            addCriterion("SHOW_IN_APP not between", value1, value2, "showInApp");
            return (Criteria) this;
        }

        public Criteria andShowInBackIsNull() {
            addCriterion("SHOW_IN_BACK is null");
            return (Criteria) this;
        }

        public Criteria andShowInBackIsNotNull() {
            addCriterion("SHOW_IN_BACK is not null");
            return (Criteria) this;
        }

        public Criteria andShowInBackEqualTo(Boolean value) {
            addCriterion("SHOW_IN_BACK =", value, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackNotEqualTo(Boolean value) {
            addCriterion("SHOW_IN_BACK <>", value, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackGreaterThan(Boolean value) {
            addCriterion("SHOW_IN_BACK >", value, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackGreaterThanOrEqualTo(Boolean value) {
            addCriterion("SHOW_IN_BACK >=", value, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackLessThan(Boolean value) {
            addCriterion("SHOW_IN_BACK <", value, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackLessThanOrEqualTo(Boolean value) {
            addCriterion("SHOW_IN_BACK <=", value, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackIn(List<Boolean> values) {
            addCriterion("SHOW_IN_BACK in", values, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackNotIn(List<Boolean> values) {
            addCriterion("SHOW_IN_BACK not in", values, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackBetween(Boolean value1, Boolean value2) {
            addCriterion("SHOW_IN_BACK between", value1, value2, "showInBack");
            return (Criteria) this;
        }

        public Criteria andShowInBackNotBetween(Boolean value1, Boolean value2) {
            addCriterion("SHOW_IN_BACK not between", value1, value2, "showInBack");
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
     * This class corresponds to the database table tb_notice
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
     * This class corresponds to the database table tb_notice
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