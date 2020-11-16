package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POS 주문기에 Response를 주기 위한 기본 Result
 * @author green
 *
 */
public class PosResult implements Serializable {
	private static final long serialVersionUID = 1L;

	public static SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");

	@JsonProperty(value="SUCCESS")
	private boolean success = false; // 성공여부

	@JsonProperty(value="RESULT_MSG")
	private String resultMsg = null; // 응답메시지

	@JsonProperty(value="ROWS")
	private int rows = 0; // 결과 레코드 개수

	@JsonProperty(value="DATAS")
	private List<?> datas = null; // 결과 데이터 Array

	public PosResult() {}

	/**
	 * 기본 생성자
	 * @param success 성공여부
	 * @param resultMsg 결과메시지
	 * @param rows 결과 rows
	 * @param datas 결과 데이터
	 */
	public PosResult(boolean success, String resultMsg, int rows, List<Object> datas) {
		this.success = success;
		this.resultMsg = resultMsg;
		this.rows = rows;
		this.datas = datas;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public List<?> getDatas() {
		return datas;
	}

	public void setDatas(List<?> datas) {
		this.datas = datas;
	}
}
