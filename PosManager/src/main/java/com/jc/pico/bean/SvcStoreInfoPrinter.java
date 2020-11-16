package com.jc.pico.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcStoreInfoPrinter {

    private String printerNo;
    private String posNo;
    private String ip;
    private String ipPort;
	public String getPrinterNo() {
		return printerNo;
	}
	public String getPosNo() {
		return posNo;
	}
	public String getIp() {
		return ip;
	}
	public String getIpPort() {
		return ipPort;
	}
	public void setPrinterNo(String printerNo) {
		this.printerNo = printerNo;
	}
	public void setPosNo(String posNo) {
		this.posNo = posNo;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}
	@Override
	public String toString() {
		return "SvcStoreInfoPrinter [printerNo=" + printerNo + ", posNo=" + posNo + ", ip=" + ip + ", ipPort=" + ipPort
				+ "]";
	}

}