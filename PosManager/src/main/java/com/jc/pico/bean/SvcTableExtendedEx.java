package com.jc.pico.bean;

import com.jc.pico.bean.SvcTable;
import com.jc.pico.utils.bean.SvcOrderExtended;

public class SvcTableExtendedEx extends SvcTable {
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
