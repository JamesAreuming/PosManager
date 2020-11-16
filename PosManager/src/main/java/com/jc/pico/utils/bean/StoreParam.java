package com.jc.pico.utils.bean;

public class StoreParam {

	private SingleMap header;
	private SingleMap data;

	public SingleMap getHeader() {
		return header;
	}

	public void setHeader(SingleMap header) {
		this.header = header;
	}

	public SingleMap getData() {
		return data;
	}

	public void setData(SingleMap data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "StoreParam [header=" + header + ", data=" + data + "]";
	}

}
