package com.jc.pico.utils.bean;

import java.util.List;

import com.jc.pico.bean.SvcItem;
import com.jc.pico.bean.SvcItemImg;
import com.jc.pico.bean.SvcItemOpt;
import com.jc.pico.bean.SvcItemOptDtl;

/**
 * 상품 정보, 상품 옵션, 상품 옵션 상세를 포함
 * 
 * @author hyo
 *
 */
public class SvcItemExtended extends SvcItem {
	
	/**
	 * 카테고리 코드
	 */
	private String catCd;
	
	/**
	 * 카테고리 이름
	 */
	private String catNm;
	
	private List<SvcItemOptExtended> options;
	private List<SvcItemImg> images;	
	
	
	public String getCatCd() {
		return catCd;
	}

	public void setCatCd(String catCd) {
		this.catCd = catCd;
	}	

	public String getCatNm() {
		return catNm;
	}

	public void setCatNm(String catNm) {
		this.catNm = catNm;
	}

	public List<SvcItemOptExtended> getOptions() {
		return options;
	}

	public void setOptions(List<SvcItemOptExtended> options) {
		this.options = options;
	}

	public List<SvcItemImg> getImages() {
		return images;
	}

	public void setImages(List<SvcItemImg> images) {
		this.images = images;
	}

	@Override
	public String toString() {
		return "SvcItemExtended [options=" + options + ", images=" + images + "]";
	}

	/**
	 * 자동으로 생성되는 SvcItemOpt에 하위 항목을 추가로 가지는 확장 클래스
	 * 
	 * @author hyo
	 *
	 */
	public static class SvcItemOptExtended extends SvcItemOpt {

		private List<SvcItemOptDtl> details;

		public List<SvcItemOptDtl> getDetails() {
			return details;
		}

		public void setDetails(List<SvcItemOptDtl> details) {
			this.details = details;
		}

		@Override
		public String toString() {
			return "SvcItemOptExtended [details=" + details + "]";
		}

	}

}
