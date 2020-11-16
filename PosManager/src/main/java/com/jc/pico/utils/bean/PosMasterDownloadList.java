package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 1.M_DOWNLOAD_LIST:자료수신(변경수신목록)
 * 2016. 7. 20, green, create
 */
public class PosMasterDownloadList implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 매장코드
	 */
	@JsonProperty(value="CD_STORE")
	private String cdStore;
	/**
	 * 업데이트번호
	 */
	@JsonProperty(value="UPDATE_NUM")
	private Integer updateNum;
	/**
	 * 수신받을정보
	 */
	@JsonProperty(value="TABLE_NAME")
	private String tableName;
	/**
	 * 삭제구분(1:Yes,0:No)
	 */
	@JsonProperty(value="YN_DELETE")
	private Integer ynDelete;
	/**
	 * Getter 매장코드
	 */
	public String getCdStore() {
		return cdStore;
	}
	/**
	 * Setter 매장코드
	 * @param cdStore 매장코드
	 */
	public void setCdStore(String cdStore) {
		this.cdStore = cdStore;
	}
	/**
	 * Getter 업데이트번호
	 */
	public Integer getUpdateNum() {
		return updateNum;
	}
	/**
	 * Setter 업데이트번호
	 * @param updateNum 업데이트번호
	 */
	public void setUpdateNum(Integer updateNum) {
		this.updateNum = updateNum;
	}
	/**
	 * Getter 수신받을정보
	 */
	public String getTableName() {
		return tableName;
	}
	/**
	 * Setter 수신받을정보
	 * @param tableName 수신받을정보
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	/**
	 * Getter 삭제구분(1:Yes,0:No)
	 */
	public Integer getYnDelete() {
		return ynDelete;
	}
	/**
	 * Setter 삭제구분(1:Yes,0:No)
	 * @param ynDelete 삭제구분(1:Yes,0:No)
	 */
	public void setYnDelete(Integer ynDelete) {
		this.ynDelete = ynDelete;
	}
}
