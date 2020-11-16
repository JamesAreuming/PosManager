package com.jc.pico.utils.bean;

import java.util.List;

import com.jc.pico.bean.SvcSalesItem;
import com.jc.pico.bean.SvcSalesItemOpt;

public class SvcSalesItemExtended extends SvcSalesItem {

	private List<SvcSalesItemOpt> options;

	public List<SvcSalesItemOpt> getOptions() {
		return options;
	}

	public void setOptions(List<SvcSalesItemOpt> options) {
		this.options = options;
	}

}
