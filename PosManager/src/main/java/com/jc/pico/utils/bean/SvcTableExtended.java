package com.jc.pico.utils.bean;

import com.jc.pico.bean.SvcTable;

public class SvcTableExtended extends SvcTable {

	private SvcOrderExtended order;

	public SvcOrderExtended getOrder() {
		return order;
	}

	public void setOrder(SvcOrderExtended order) {
		this.order = order;
	}

	@Override
	public String toString() {
		return "SvcTableExtended [order=" + order + "]";
	}

}
