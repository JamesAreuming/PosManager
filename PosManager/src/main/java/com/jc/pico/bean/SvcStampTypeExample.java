package com.jc.pico.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SvcStampTypeExample {
    /**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	protected String orderByClause;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	protected boolean distinct;
	/**
	 * This field was generated by MyBatis Generator. This field corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	protected List<Criteria> oredCriteria;

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public SvcStampTypeExample() {
		oredCriteria = new ArrayList<Criteria>();
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setOrderByClause(String orderByClause) {
		this.orderByClause = orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public String getOrderByClause() {
		return orderByClause;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public boolean isDistinct() {
		return distinct;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public List<Criteria> getOredCriteria() {
		return oredCriteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void or(Criteria criteria) {
		oredCriteria.add(criteria);
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Criteria or() {
		Criteria criteria = createCriteriaInternal();
		oredCriteria.add(criteria);
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public Criteria createCriteria() {
		Criteria criteria = createCriteriaInternal();
		if (oredCriteria.size() == 0) {
			oredCriteria.add(criteria);
		}
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	protected Criteria createCriteriaInternal() {
		Criteria criteria = new Criteria();
		return criteria;
	}

	/**
	 * This method was generated by MyBatis Generator. This method corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
	 */
	public void clear() {
		oredCriteria.clear();
		orderByClause = null;
		distinct = false;
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
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

		public Criteria andIdEqualTo(Integer value) {
			addCriterion("ID =", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotEqualTo(Integer value) {
			addCriterion("ID <>", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThan(Integer value) {
			addCriterion("ID >", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdGreaterThanOrEqualTo(Integer value) {
			addCriterion("ID >=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThan(Integer value) {
			addCriterion("ID <", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdLessThanOrEqualTo(Integer value) {
			addCriterion("ID <=", value, "id");
			return (Criteria) this;
		}

		public Criteria andIdIn(List<Integer> values) {
			addCriterion("ID in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotIn(List<Integer> values) {
			addCriterion("ID not in", values, "id");
			return (Criteria) this;
		}

		public Criteria andIdBetween(Integer value1, Integer value2) {
			addCriterion("ID between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andIdNotBetween(Integer value1, Integer value2) {
			addCriterion("ID not between", value1, value2, "id");
			return (Criteria) this;
		}

		public Criteria andTypeNmIsNull() {
			addCriterion("TYPE_NM is null");
			return (Criteria) this;
		}

		public Criteria andTypeNmIsNotNull() {
			addCriterion("TYPE_NM is not null");
			return (Criteria) this;
		}

		public Criteria andTypeNmEqualTo(String value) {
			addCriterion("TYPE_NM =", value, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmNotEqualTo(String value) {
			addCriterion("TYPE_NM <>", value, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmGreaterThan(String value) {
			addCriterion("TYPE_NM >", value, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmGreaterThanOrEqualTo(String value) {
			addCriterion("TYPE_NM >=", value, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmLessThan(String value) {
			addCriterion("TYPE_NM <", value, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmLessThanOrEqualTo(String value) {
			addCriterion("TYPE_NM <=", value, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmLike(String value) {
			addCriterion("TYPE_NM like", value, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmNotLike(String value) {
			addCriterion("TYPE_NM not like", value, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmIn(List<String> values) {
			addCriterion("TYPE_NM in", values, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmNotIn(List<String> values) {
			addCriterion("TYPE_NM not in", values, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmBetween(String value1, String value2) {
			addCriterion("TYPE_NM between", value1, value2, "typeNm");
			return (Criteria) this;
		}

		public Criteria andTypeNmNotBetween(String value1, String value2) {
			addCriterion("TYPE_NM not between", value1, value2, "typeNm");
			return (Criteria) this;
		}

		public Criteria andStartTmIsNull() {
			addCriterion("START_TM is null");
			return (Criteria) this;
		}

		public Criteria andStartTmIsNotNull() {
			addCriterion("START_TM is not null");
			return (Criteria) this;
		}

		public Criteria andStartTmEqualTo(String value) {
			addCriterion("START_TM =", value, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmNotEqualTo(String value) {
			addCriterion("START_TM <>", value, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmGreaterThan(String value) {
			addCriterion("START_TM >", value, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmGreaterThanOrEqualTo(String value) {
			addCriterion("START_TM >=", value, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmLessThan(String value) {
			addCriterion("START_TM <", value, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmLessThanOrEqualTo(String value) {
			addCriterion("START_TM <=", value, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmLike(String value) {
			addCriterion("START_TM like", value, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmNotLike(String value) {
			addCriterion("START_TM not like", value, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmIn(List<String> values) {
			addCriterion("START_TM in", values, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmNotIn(List<String> values) {
			addCriterion("START_TM not in", values, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmBetween(String value1, String value2) {
			addCriterion("START_TM between", value1, value2, "startTm");
			return (Criteria) this;
		}

		public Criteria andStartTmNotBetween(String value1, String value2) {
			addCriterion("START_TM not between", value1, value2, "startTm");
			return (Criteria) this;
		}

		public Criteria andEndTmIsNull() {
			addCriterion("END_TM is null");
			return (Criteria) this;
		}

		public Criteria andEndTmIsNotNull() {
			addCriterion("END_TM is not null");
			return (Criteria) this;
		}

		public Criteria andEndTmEqualTo(String value) {
			addCriterion("END_TM =", value, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmNotEqualTo(String value) {
			addCriterion("END_TM <>", value, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmGreaterThan(String value) {
			addCriterion("END_TM >", value, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmGreaterThanOrEqualTo(String value) {
			addCriterion("END_TM >=", value, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmLessThan(String value) {
			addCriterion("END_TM <", value, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmLessThanOrEqualTo(String value) {
			addCriterion("END_TM <=", value, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmLike(String value) {
			addCriterion("END_TM like", value, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmNotLike(String value) {
			addCriterion("END_TM not like", value, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmIn(List<String> values) {
			addCriterion("END_TM in", values, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmNotIn(List<String> values) {
			addCriterion("END_TM not in", values, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmBetween(String value1, String value2) {
			addCriterion("END_TM between", value1, value2, "endTm");
			return (Criteria) this;
		}

		public Criteria andEndTmNotBetween(String value1, String value2) {
			addCriterion("END_TM not between", value1, value2, "endTm");
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

		public Criteria andWeekEqualTo(String value) {
			addCriterion("WEEK =", value, "week");
			return (Criteria) this;
		}

		public Criteria andWeekNotEqualTo(String value) {
			addCriterion("WEEK <>", value, "week");
			return (Criteria) this;
		}

		public Criteria andWeekGreaterThan(String value) {
			addCriterion("WEEK >", value, "week");
			return (Criteria) this;
		}

		public Criteria andWeekGreaterThanOrEqualTo(String value) {
			addCriterion("WEEK >=", value, "week");
			return (Criteria) this;
		}

		public Criteria andWeekLessThan(String value) {
			addCriterion("WEEK <", value, "week");
			return (Criteria) this;
		}

		public Criteria andWeekLessThanOrEqualTo(String value) {
			addCriterion("WEEK <=", value, "week");
			return (Criteria) this;
		}

		public Criteria andWeekLike(String value) {
			addCriterion("WEEK like", value, "week");
			return (Criteria) this;
		}

		public Criteria andWeekNotLike(String value) {
			addCriterion("WEEK not like", value, "week");
			return (Criteria) this;
		}

		public Criteria andWeekIn(List<String> values) {
			addCriterion("WEEK in", values, "week");
			return (Criteria) this;
		}

		public Criteria andWeekNotIn(List<String> values) {
			addCriterion("WEEK not in", values, "week");
			return (Criteria) this;
		}

		public Criteria andWeekBetween(String value1, String value2) {
			addCriterion("WEEK between", value1, value2, "week");
			return (Criteria) this;
		}

		public Criteria andWeekNotBetween(String value1, String value2) {
			addCriterion("WEEK not between", value1, value2, "week");
			return (Criteria) this;
		}

		public Criteria andItemCdIsNull() {
			addCriterion("ITEM_CD is null");
			return (Criteria) this;
		}

		public Criteria andItemCdIsNotNull() {
			addCriterion("ITEM_CD is not null");
			return (Criteria) this;
		}

		public Criteria andItemCdEqualTo(String value) {
			addCriterion("ITEM_CD =", value, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdNotEqualTo(String value) {
			addCriterion("ITEM_CD <>", value, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdGreaterThan(String value) {
			addCriterion("ITEM_CD >", value, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdGreaterThanOrEqualTo(String value) {
			addCriterion("ITEM_CD >=", value, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdLessThan(String value) {
			addCriterion("ITEM_CD <", value, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdLessThanOrEqualTo(String value) {
			addCriterion("ITEM_CD <=", value, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdLike(String value) {
			addCriterion("ITEM_CD like", value, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdNotLike(String value) {
			addCriterion("ITEM_CD not like", value, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdIn(List<String> values) {
			addCriterion("ITEM_CD in", values, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdNotIn(List<String> values) {
			addCriterion("ITEM_CD not in", values, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdBetween(String value1, String value2) {
			addCriterion("ITEM_CD between", value1, value2, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItemCdNotBetween(String value1, String value2) {
			addCriterion("ITEM_CD not between", value1, value2, "itemCd");
			return (Criteria) this;
		}

		public Criteria andItmeGroupIsNull() {
			addCriterion("ITME_GROUP is null");
			return (Criteria) this;
		}

		public Criteria andItmeGroupIsNotNull() {
			addCriterion("ITME_GROUP is not null");
			return (Criteria) this;
		}

		public Criteria andItmeGroupEqualTo(String value) {
			addCriterion("ITME_GROUP =", value, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupNotEqualTo(String value) {
			addCriterion("ITME_GROUP <>", value, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupGreaterThan(String value) {
			addCriterion("ITME_GROUP >", value, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupGreaterThanOrEqualTo(String value) {
			addCriterion("ITME_GROUP >=", value, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupLessThan(String value) {
			addCriterion("ITME_GROUP <", value, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupLessThanOrEqualTo(String value) {
			addCriterion("ITME_GROUP <=", value, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupLike(String value) {
			addCriterion("ITME_GROUP like", value, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupNotLike(String value) {
			addCriterion("ITME_GROUP not like", value, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupIn(List<String> values) {
			addCriterion("ITME_GROUP in", values, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupNotIn(List<String> values) {
			addCriterion("ITME_GROUP not in", values, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupBetween(String value1, String value2) {
			addCriterion("ITME_GROUP between", value1, value2, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItmeGroupNotBetween(String value1, String value2) {
			addCriterion("ITME_GROUP not between", value1, value2, "itmeGroup");
			return (Criteria) this;
		}

		public Criteria andItemQtyIsNull() {
			addCriterion("ITEM_QTY is null");
			return (Criteria) this;
		}

		public Criteria andItemQtyIsNotNull() {
			addCriterion("ITEM_QTY is not null");
			return (Criteria) this;
		}

		public Criteria andItemQtyEqualTo(Integer value) {
			addCriterion("ITEM_QTY =", value, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyNotEqualTo(Integer value) {
			addCriterion("ITEM_QTY <>", value, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyGreaterThan(Integer value) {
			addCriterion("ITEM_QTY >", value, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyGreaterThanOrEqualTo(Integer value) {
			addCriterion("ITEM_QTY >=", value, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyLessThan(Integer value) {
			addCriterion("ITEM_QTY <", value, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyLessThanOrEqualTo(Integer value) {
			addCriterion("ITEM_QTY <=", value, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyIn(List<Integer> values) {
			addCriterion("ITEM_QTY in", values, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyNotIn(List<Integer> values) {
			addCriterion("ITEM_QTY not in", values, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyBetween(Integer value1, Integer value2) {
			addCriterion("ITEM_QTY between", value1, value2, "itemQty");
			return (Criteria) this;
		}

		public Criteria andItemQtyNotBetween(Integer value1, Integer value2) {
			addCriterion("ITEM_QTY not between", value1, value2, "itemQty");
			return (Criteria) this;
		}

		public Criteria andOrderPayIsNull() {
			addCriterion("ORDER_PAY is null");
			return (Criteria) this;
		}

		public Criteria andOrderPayIsNotNull() {
			addCriterion("ORDER_PAY is not null");
			return (Criteria) this;
		}

		public Criteria andOrderPayEqualTo(Integer value) {
			addCriterion("ORDER_PAY =", value, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayNotEqualTo(Integer value) {
			addCriterion("ORDER_PAY <>", value, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayGreaterThan(Integer value) {
			addCriterion("ORDER_PAY >", value, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayGreaterThanOrEqualTo(Integer value) {
			addCriterion("ORDER_PAY >=", value, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayLessThan(Integer value) {
			addCriterion("ORDER_PAY <", value, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayLessThanOrEqualTo(Integer value) {
			addCriterion("ORDER_PAY <=", value, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayIn(List<Integer> values) {
			addCriterion("ORDER_PAY in", values, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayNotIn(List<Integer> values) {
			addCriterion("ORDER_PAY not in", values, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayBetween(Integer value1, Integer value2) {
			addCriterion("ORDER_PAY between", value1, value2, "orderPay");
			return (Criteria) this;
		}

		public Criteria andOrderPayNotBetween(Integer value1, Integer value2) {
			addCriterion("ORDER_PAY not between", value1, value2, "orderPay");
			return (Criteria) this;
		}

		public Criteria andCidIsNull() {
			addCriterion("CID is null");
			return (Criteria) this;
		}

		public Criteria andCidIsNotNull() {
			addCriterion("CID is not null");
			return (Criteria) this;
		}

		public Criteria andCidEqualTo(Integer value) {
			addCriterion("CID =", value, "cid");
			return (Criteria) this;
		}

		public Criteria andCidNotEqualTo(Integer value) {
			addCriterion("CID <>", value, "cid");
			return (Criteria) this;
		}

		public Criteria andCidGreaterThan(Integer value) {
			addCriterion("CID >", value, "cid");
			return (Criteria) this;
		}

		public Criteria andCidGreaterThanOrEqualTo(Integer value) {
			addCriterion("CID >=", value, "cid");
			return (Criteria) this;
		}

		public Criteria andCidLessThan(Integer value) {
			addCriterion("CID <", value, "cid");
			return (Criteria) this;
		}

		public Criteria andCidLessThanOrEqualTo(Integer value) {
			addCriterion("CID <=", value, "cid");
			return (Criteria) this;
		}

		public Criteria andCidIn(List<Integer> values) {
			addCriterion("CID in", values, "cid");
			return (Criteria) this;
		}

		public Criteria andCidNotIn(List<Integer> values) {
			addCriterion("CID not in", values, "cid");
			return (Criteria) this;
		}

		public Criteria andCidBetween(Integer value1, Integer value2) {
			addCriterion("CID between", value1, value2, "cid");
			return (Criteria) this;
		}

		public Criteria andCidNotBetween(Integer value1, Integer value2) {
			addCriterion("CID not between", value1, value2, "cid");
			return (Criteria) this;
		}

		public Criteria andCdtIsNull() {
			addCriterion("CDT is null");
			return (Criteria) this;
		}

		public Criteria andCdtIsNotNull() {
			addCriterion("CDT is not null");
			return (Criteria) this;
		}

		public Criteria andCdtEqualTo(Date value) {
			addCriterion("CDT =", value, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtNotEqualTo(Date value) {
			addCriterion("CDT <>", value, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtGreaterThan(Date value) {
			addCriterion("CDT >", value, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtGreaterThanOrEqualTo(Date value) {
			addCriterion("CDT >=", value, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtLessThan(Date value) {
			addCriterion("CDT <", value, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtLessThanOrEqualTo(Date value) {
			addCriterion("CDT <=", value, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtIn(List<Date> values) {
			addCriterion("CDT in", values, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtNotIn(List<Date> values) {
			addCriterion("CDT not in", values, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtBetween(Date value1, Date value2) {
			addCriterion("CDT between", value1, value2, "cdt");
			return (Criteria) this;
		}

		public Criteria andCdtNotBetween(Date value1, Date value2) {
			addCriterion("CDT not between", value1, value2, "cdt");
			return (Criteria) this;
		}

		public Criteria andMidIsNull() {
			addCriterion("MID is null");
			return (Criteria) this;
		}

		public Criteria andMidIsNotNull() {
			addCriterion("MID is not null");
			return (Criteria) this;
		}

		public Criteria andMidEqualTo(Integer value) {
			addCriterion("MID =", value, "mid");
			return (Criteria) this;
		}

		public Criteria andMidNotEqualTo(Integer value) {
			addCriterion("MID <>", value, "mid");
			return (Criteria) this;
		}

		public Criteria andMidGreaterThan(Integer value) {
			addCriterion("MID >", value, "mid");
			return (Criteria) this;
		}

		public Criteria andMidGreaterThanOrEqualTo(Integer value) {
			addCriterion("MID >=", value, "mid");
			return (Criteria) this;
		}

		public Criteria andMidLessThan(Integer value) {
			addCriterion("MID <", value, "mid");
			return (Criteria) this;
		}

		public Criteria andMidLessThanOrEqualTo(Integer value) {
			addCriterion("MID <=", value, "mid");
			return (Criteria) this;
		}

		public Criteria andMidIn(List<Integer> values) {
			addCriterion("MID in", values, "mid");
			return (Criteria) this;
		}

		public Criteria andMidNotIn(List<Integer> values) {
			addCriterion("MID not in", values, "mid");
			return (Criteria) this;
		}

		public Criteria andMidBetween(Integer value1, Integer value2) {
			addCriterion("MID between", value1, value2, "mid");
			return (Criteria) this;
		}

		public Criteria andMidNotBetween(Integer value1, Integer value2) {
			addCriterion("MID not between", value1, value2, "mid");
			return (Criteria) this;
		}

		public Criteria andMdtIsNull() {
			addCriterion("MDT is null");
			return (Criteria) this;
		}

		public Criteria andMdtIsNotNull() {
			addCriterion("MDT is not null");
			return (Criteria) this;
		}

		public Criteria andMdtEqualTo(Date value) {
			addCriterion("MDT =", value, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtNotEqualTo(Date value) {
			addCriterion("MDT <>", value, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtGreaterThan(Date value) {
			addCriterion("MDT >", value, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtGreaterThanOrEqualTo(Date value) {
			addCriterion("MDT >=", value, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtLessThan(Date value) {
			addCriterion("MDT <", value, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtLessThanOrEqualTo(Date value) {
			addCriterion("MDT <=", value, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtIn(List<Date> values) {
			addCriterion("MDT in", values, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtNotIn(List<Date> values) {
			addCriterion("MDT not in", values, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtBetween(Date value1, Date value2) {
			addCriterion("MDT between", value1, value2, "mdt");
			return (Criteria) this;
		}

		public Criteria andMdtNotBetween(Date value1, Date value2) {
			addCriterion("MDT not between", value1, value2, "mdt");
			return (Criteria) this;
		}
	}

	/**
	 * This class was generated by MyBatis Generator. This class corresponds to the database table TB_SVC_STAMP_TYPE
	 * @mbggenerated  Fri May 20 15:37:02 KST 2016
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

	/**
     * This class was generated by MyBatis Generator.
     * This class corresponds to the database table TB_SVC_STAMP_TYPE
     *
     * @mbggenerated do_not_delete_during_merge Wed May 18 10:12:48 KST 2016
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }
}