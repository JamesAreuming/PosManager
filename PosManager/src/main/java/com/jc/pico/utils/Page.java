package com.jc.pico.utils;

import java.util.List;

/**
 * 페이징 처리가 필요한 리스트를 꾸밀때 사용하는 클래스 입니다.
 * 
 * @author s
 *
 */
public class Page<T> {
	private Paginate paginate = null;
	private List<T> list = null;
	private T data = null;
	
	public void setPaginate(long totalCount, int currentPage, int countPerPage, int pagePerBlock, String keyword, String keytype) {
		paginate = new Paginate(totalCount, currentPage, countPerPage, pagePerBlock, keyword, keytype);
	}
	
	public void setPaginate(long totalCount, int currentPage, int countPerPage, int pagePerBlock, String keyword) {
		paginate = new Paginate(totalCount, currentPage, countPerPage, pagePerBlock, keyword);
	}
	
	public void setPaginate(long totalCount, int currentPage, int countPerPage, int pagePerBlock) {
		paginate = new Paginate(totalCount, currentPage, countPerPage, pagePerBlock);
	}
	
	public void setPaginate(long totalCount, int currentPage, int countPerPage) {
		paginate = new Paginate(totalCount, currentPage, countPerPage);
	}

	public Paginate getPaginate() {
		return paginate;
	}

	public void setPaginate(Paginate pageDivBean) {
		this.paginate = pageDivBean;
	}

	public List<T> getList() {
		return list;
	}

	public void setList(List<T> list) {
		this.list = list;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "Page [paginate=" + paginate + ", list=" + list + ", data=" + data + "]";
	}

}
