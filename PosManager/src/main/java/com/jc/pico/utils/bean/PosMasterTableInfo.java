package com.jc.pico.utils.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 20. M_TABLE_INFO : 테이블 정보
 * 2016. 7. 21, green, create
 */
public class PosMasterTableInfo implements Serializable {
	private final static long serialVersionUID = 1L;

	/**
	 * 회사코드
	 */
	@JsonProperty(value="CD_COMPANY")
	private String cdCompany;
	/**
	 * 매장코드
	 */
	@JsonProperty(value="CD_STORE")
	private String cdStore;
	/**
	 * 테이블주문가능유무
	 */
	@JsonProperty(value="YN_ORDER")
	private Integer ynOrder;
	/**
	 * 테이블번호
	 */
	@JsonProperty(value="NO_TABLE")
	private Long noTable;
	/**
	 * 상세코드
	 */
	@JsonProperty(value="CD_GROUP")
	private Integer cdGroup;
	/**
	 * 테이블섹션코드
	 */
	@JsonProperty(value="CD_SECTION")
	private Long cdSection;
	/**
	 * 테이블명
	 */
	@JsonProperty(value="NM_TABLE")
	private String nmTable;
	/**
	 * 사용여부
	 */
	@JsonProperty(value="YN_USE")
	private Integer ynUse;
	/**
	 * X좌표
	 */
	@JsonProperty(value="NO_LEFT")
	private Integer noLeft;
	/**
	 * Y좌표
	 */
	@JsonProperty(value="NO_TOP")
	private Integer noTop;
	/**
	 * 넓이
	 */
	@JsonProperty(value="NO_WIDTH")
	private Integer noWidth;
	/**
	 * 높이
	 */
	@JsonProperty(value="NO_HEIGHT")
	private Integer noHeight;
	/**
	 * 기본색상1
	 */
	@JsonProperty(value="CL_DEFAULT1")
	private String clDefault1;
	/**
	 * 기본색상2
	 */
	@JsonProperty(value="CL_DEFAULT2")
	private String clDefault2;
	/**
	 * 기본색상3
	 */
	@JsonProperty(value="CL_DEFAULT3")
	private String clDefault3;
	/**
	 * 기본색상4
	 */
	@JsonProperty(value="CL_DEFAULT4")
	private String clDefault4;
	/**
	 * 기본색상5
	 */
	@JsonProperty(value="CL_DEFAULT5")
	private String clDefault5;
	/**
	 * 기본색상6
	 */
	@JsonProperty(value="CL_DEFAULT6")
	private String clDefault6;
	/**
	 * 기본색상7
	 */
	@JsonProperty(value="CL_DEFAULT7")
	private String clDefault7;
	/**
	 * 기본색상8
	 */
	@JsonProperty(value="CL_DEFAULT8")
	private String clDefault8;
	/**
	 * 색상1
	 */
	@JsonProperty(value="CL_CHANGE1")
	private String clChange1;
	/**
	 * 색상2
	 */
	@JsonProperty(value="CL_CHANGE2")
	private String clChange2;
	/**
	 * 색상3
	 */
	@JsonProperty(value="CL_CHANGE3")
	private String clChange3;
	/**
	 * 색상4
	 */
	@JsonProperty(value="CL_CHANGE4")
	private String clChange4;
	/**
	 * 색상5
	 */
	@JsonProperty(value="CL_CHANGE5")
	private String clChange5;
	/**
	 * 색상6
	 */
	@JsonProperty(value="CL_CHANGE6")
	private String clChange6;
	/**
	 * 색상7
	 */
	@JsonProperty(value="CL_CHANGE7")
	private String clChange7;
	/**
	 * 색상8
	 */
	@JsonProperty(value="CL_CHANGE8")
	private String clChange8;
	/**
	 * 등록일시
	 */
	@JsonProperty(value="DT_INSERT")
	private String dtInsert;
	/**
	 * 등록자
	 */
	@JsonProperty(value="CD_EMPLOYEE_INSERT")
	private String cdEmployeeInsert;
	/**
	 * 수정일시
	 */
	@JsonProperty(value="DT_UPDATE")
	private String dtUpdate;
	/**
	 * 수정자
	 */
	@JsonProperty(value="CD_EMPLOYEE_UPDATE")
	private String cdEmployeeUpdate;
	/**
	 * Getter 회사코드
	 */
	public String getCdCompany() {
		return cdCompany;
	}
	/**
	 * Setter 회사코드
	 * @param cdCompany 회사코드
	 */
	public void setCdCompany(String cdCompany) {
		this.cdCompany = cdCompany;
	}
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
	 * Getter 테이블주문가능유무
	 */
	public Integer getYnOrder() {
		return ynOrder;
	}
	/**
	 * Setter 테이블주문가능유무
	 * @param ynOrder 테이블주문가능유무
	 */
	public void setYnOrder(Integer ynOrder) {
		this.ynOrder = ynOrder;
	}
	/**
	 * Getter 테이블번호
	 */
	public Long getNoTable() {
		return noTable;
	}
	/**
	 * Setter 테이블번호
	 * @param noTable 테이블번호
	 */
	public void setNoTable(Long noTable) {
		this.noTable = noTable;
	}
	/**
	 * Getter 상세코드
	 */
	public Integer getCdGroup() {
		return cdGroup;
	}
	/**
	 * Setter 상세코드
	 * @param cdGroup 상세코드
	 */
	public void setCdGroup(Integer cdGroup) {
		this.cdGroup = cdGroup;
	}
	/**
	 * Getter 테이블섹션코드
	 */
	public Long getCdSection() {
		return cdSection;
	}
	/**
	 * Setter 테이블섹션코드
	 * @param cdSection 테이블섹션코드
	 */
	public void setCdSection(Long cdSection) {
		this.cdSection = cdSection;
	}
	/**
	 * Getter 테이블명
	 */
	public String getNmTable() {
		return nmTable;
	}
	/**
	 * Setter 테이블명
	 * @param nmTable 테이블명
	 */
	public void setNmTable(String nmTable) {
		this.nmTable = nmTable;
	}

	public Integer getYnUse() {
		return ynUse;
	}

	public void setYnUse(Integer ynUse) {
		this.ynUse = ynUse;
	}

	/**
	 * Getter X좌표
	 */
	public Integer getNoLeft() {
		return noLeft;
	}
	/**
	 * Setter X좌표
	 * @param noLeft X좌표
	 */
	public void setNoLeft(Integer noLeft) {
		this.noLeft = noLeft;
	}
	/**
	 * Getter Y좌표
	 */
	public Integer getNoTop() {
		return noTop;
	}
	/**
	 * Setter Y좌표
	 * @param noTop Y좌표
	 */
	public void setNoTop(Integer noTop) {
		this.noTop = noTop;
	}
	/**
	 * Getter 넓이
	 */
	public Integer getNoWidth() {
		return noWidth;
	}
	/**
	 * Setter 넓이
	 * @param noWidth 넓이
	 */
	public void setNoWidth(Integer noWidth) {
		this.noWidth = noWidth;
	}
	/**
	 * Getter 높이
	 */
	public Integer getNoHeight() {
		return noHeight;
	}
	/**
	 * Setter 높이
	 * @param noHeight 높이
	 */
	public void setNoHeight(Integer noHeight) {
		this.noHeight = noHeight;
	}
	/**
	 * Getter 기본색상1
	 */
	public String getClDefault1() {
		return clDefault1;
	}
	/**
	 * Setter 기본색상1
	 * @param clDefault1 기본색상1
	 */
	public void setClDefault1(String clDefault1) {
		this.clDefault1 = clDefault1;
	}
	/**
	 * Getter 기본색상2
	 */
	public String getClDefault2() {
		return clDefault2;
	}
	/**
	 * Setter 기본색상2
	 * @param clDefault2 기본색상2
	 */
	public void setClDefault2(String clDefault2) {
		this.clDefault2 = clDefault2;
	}
	/**
	 * Getter 기본색상3
	 */
	public String getClDefault3() {
		return clDefault3;
	}
	/**
	 * Setter 기본색상3
	 * @param clDefault3 기본색상3
	 */
	public void setClDefault3(String clDefault3) {
		this.clDefault3 = clDefault3;
	}
	/**
	 * Getter 기본색상4
	 */
	public String getClDefault4() {
		return clDefault4;
	}
	/**
	 * Setter 기본색상4
	 * @param clDefault4 기본색상4
	 */
	public void setClDefault4(String clDefault4) {
		this.clDefault4 = clDefault4;
	}
	/**
	 * Getter 기본색상5
	 */
	public String getClDefault5() {
		return clDefault5;
	}
	/**
	 * Setter 기본색상5
	 * @param clDefault5 기본색상5
	 */
	public void setClDefault5(String clDefault5) {
		this.clDefault5 = clDefault5;
	}
	/**
	 * Getter 기본색상6
	 */
	public String getClDefault6() {
		return clDefault6;
	}
	/**
	 * Setter 기본색상6
	 * @param clDefault6 기본색상6
	 */
	public void setClDefault6(String clDefault6) {
		this.clDefault6 = clDefault6;
	}
	/**
	 * Getter 기본색상7
	 */
	public String getClDefault7() {
		return clDefault7;
	}
	/**
	 * Setter 기본색상7
	 * @param clDefault7 기본색상7
	 */
	public void setClDefault7(String clDefault7) {
		this.clDefault7 = clDefault7;
	}
	/**
	 * Getter 기본색상8
	 */
	public String getClDefault8() {
		return clDefault8;
	}
	/**
	 * Setter 기본색상8
	 * @param clDefault8 기본색상8
	 */
	public void setClDefault8(String clDefault8) {
		this.clDefault8 = clDefault8;
	}
	/**
	 * Getter 색상1
	 */
	public String getClChange1() {
		return clChange1;
	}
	/**
	 * Setter 색상1
	 * @param clChange1 색상1
	 */
	public void setClChange1(String clChange1) {
		this.clChange1 = clChange1;
	}
	/**
	 * Getter 색상2
	 */
	public String getClChange2() {
		return clChange2;
	}
	/**
	 * Setter 색상2
	 * @param clChange2 색상2
	 */
	public void setClChange2(String clChange2) {
		this.clChange2 = clChange2;
	}
	/**
	 * Getter 색상3
	 */
	public String getClChange3() {
		return clChange3;
	}
	/**
	 * Setter 색상3
	 * @param clChange3 색상3
	 */
	public void setClChange3(String clChange3) {
		this.clChange3 = clChange3;
	}
	/**
	 * Getter 색상4
	 */
	public String getClChange4() {
		return clChange4;
	}
	/**
	 * Setter 색상4
	 * @param clChange4 색상4
	 */
	public void setClChange4(String clChange4) {
		this.clChange4 = clChange4;
	}
	/**
	 * Getter 색상5
	 */
	public String getClChange5() {
		return clChange5;
	}
	/**
	 * Setter 색상5
	 * @param clChange5 색상5
	 */
	public void setClChange5(String clChange5) {
		this.clChange5 = clChange5;
	}
	/**
	 * Getter 색상6
	 */
	public String getClChange6() {
		return clChange6;
	}
	/**
	 * Setter 색상6
	 * @param clChange6 색상6
	 */
	public void setClChange6(String clChange6) {
		this.clChange6 = clChange6;
	}
	/**
	 * Getter 색상7
	 */
	public String getClChange7() {
		return clChange7;
	}
	/**
	 * Setter 색상7
	 * @param clChange7 색상7
	 */
	public void setClChange7(String clChange7) {
		this.clChange7 = clChange7;
	}
	/**
	 * Getter 색상8
	 */
	public String getClChange8() {
		return clChange8;
	}
	/**
	 * Setter 색상8
	 * @param clChange8 색상8
	 */
	public void setClChange8(String clChange8) {
		this.clChange8 = clChange8;
	}
	/**
	 * Getter 등록일시
	 */
	public String getDtInsert() {
		return dtInsert;
	}
	/**
	 * Setter 등록일시
	 * @param dtInsert 등록일시
	 */
	public void setDtInsert(String dtInsert) {
		this.dtInsert = dtInsert;
	}
	/**
	 * Getter 등록자
	 */
	public String getCdEmployeeInsert() {
		return cdEmployeeInsert;
	}
	/**
	 * Setter 등록자
	 * @param cdEmployeeInsert 등록자
	 */
	public void setCdEmployeeInsert(String cdEmployeeInsert) {
		this.cdEmployeeInsert = cdEmployeeInsert;
	}
	/**
	 * Getter 수정일시
	 */
	public String getDtUpdate() {
		return dtUpdate;
	}
	/**
	 * Setter 수정일시
	 * @param dtUpdate 수정일시
	 */
	public void setDtUpdate(String dtUpdate) {
		this.dtUpdate = dtUpdate;
	}
	/**
	 * Getter 수정자
	 */
	public String getCdEmployeeUpdate() {
		return cdEmployeeUpdate;
	}
	/**
	 * Setter 수정자
	 * @param cdEmployeeUpdate 수정자
	 */
	public void setCdEmployeeUpdate(String cdEmployeeUpdate) {
		this.cdEmployeeUpdate = cdEmployeeUpdate;
	}
}
