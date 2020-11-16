package com.jc.pico.utils.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 포스 연동 프린터 정보
 * 
 * @author hyo 2016.09.09
 *
 */
public class PosPrinterInfo {

	/**
	 * 1.회사 코드
	 */
	@JsonProperty(value = "CD_COMPANY")
	private String cdCompany;

	/**
	 * 2.상점 코드
	 */
	@JsonProperty(value = "CD_STORE")
	private String cdStore;

	/**
	 * 3.프린터 번호
	 */
	@JsonProperty(value = "CD_PRINTER")
	private String cdPrinter;
	
	/**
	 * 4.프린터 타입001: 키친프린터 002: 영수증프린
	 */
	@JsonProperty(value = "PRINTER_TYPE")
	private String printerType;
	
	/**
	 * 5.프린터 회사코드
	 */
	@JsonProperty(value = "PRINTER_COMPANY_CD")
	private String printerCompanyCd;
	
	/**
	 * 6.프린터명
	 */
	@JsonProperty(value = "NM_PRINTER")
	private String nmPrinter;

	/**
	 * 7.프린터기종코드 (공통코드 863)
	 */
	@JsonProperty(value = "CD_MODEL")
	private String cdModel;
	
	/**
	 * 8.포트코드 (공통코드 66)
	 */
	@JsonProperty(value = "NO_POS")
	private String noPos;

	/**
	 * 9.IP ADDRESS
	 */
	@JsonProperty(value = "IP")
	private String ip;
	
	/**
	 * 10.IP PORT
	 */
	@JsonProperty(value = "IP_PORT")
	private String ipPort;
	
	/**
	 * 11.MAC_ADDRESS
	 */
	@JsonProperty(value = "MAC_ADDRESS")
	private String macAddress;
	
	/**
	 * 12.DEVICE FILE NAME
	 */
	@JsonProperty(value = "DEVICE_FILE")
	private String deviceFile;
	
	/**
	 * 13.프린터 디바이스 명
	 */
	@JsonProperty(value = "DEVICE_NM")
	private String deviceNm;
	
	/**
	 * 14.BITS IN A BYTE
	 */
	@JsonProperty(value = "BITS")
	private String bits;
	
	/**
	 * 15.PARITY
	 */
	@JsonProperty(value = "PARITY")
	private String parity;
	
	/**
	 * 16.STOP BITS
	 */
	@JsonProperty(value = "STOP_BITS")
	private String stopBits;
		
	/**
	 * 17.포트코드 (공통코드 866)
	 */
	@JsonProperty(value = "CD_PORT")
	private String cdPort;

	/**
	 * 18.속도
	 */
	@JsonProperty(value = "BAUDRATE")
	private String baudrate;

	/**
	 * 19.출력 매수
	 */
	@JsonProperty(value = "CNT_PAGE")
	private String cntPage;
	
	/**
	 * 20.블루투스 자동 비활성화 여부 (yes:1,no:0)
	 */
	@JsonProperty(value = "IS_DISABLE_BLUETOOTH")
	private Integer isDisableBluetooth;
	
	/**
	 * 21.블루투스 자동 활성화 여부 (yes:1,no:0)
	 */
	@JsonProperty(value = "IS_ENABLE_BLUETOOTH")
	private Integer isEnableBluetooth;
	
	
	/**
	 * 22.연결유형
	 */
	@JsonProperty(value = "CONNECTION_TYPE")
	private String connectionType;

	/**
	 * 23.USB Vendor ID
	 */
	@JsonProperty(value = "USB_VID")
	private String usbVid;
	
	/**
	 * 24.USB Product ID
	 */
	@JsonProperty(value = "USB_PID")
	private String usbPid;
	
	/**
	 * 25.USB 프로토콜
	 */
	@JsonProperty(value = "USB_PROTOCOL")
	private String usbProtocol;
	
	
	/**
	 * 26.사용유무 (1:사용, 0:미사용)
	 */
	@JsonProperty(value = "YN_USE")
	private Integer ynUse;
	

	/**
	 * 27.프린터 접속 비밀번호
	 */
	@JsonProperty(value = "ACCESS_PWD")
	private String accessPwd;
	
	/**
	 * 28.프린터 접속 사용자 비밀번호
	 */
	@JsonProperty(value = "USER_PWD")
	private String userPwd;

	public String getCdCompany() {
		return cdCompany;
	}

	public void setCdCompany(String cdCompany) {
		this.cdCompany = cdCompany;
	}

	public String getCdStore() {
		return cdStore;
	}

	public void setCdStore(String cdStore) {
		this.cdStore = cdStore;
	}

	public String getCdPrinter() {
		return cdPrinter;
	}

	public void setCdPrinter(String cdPrinter) {
		this.cdPrinter = cdPrinter;
	}

	public String getPrinterType() {
		return printerType;
	}

	public void setPrinterType(String printerType) {
		this.printerType = printerType;
	}

	public String getPrinterCompanyCd() {
		return printerCompanyCd;
	}

	public void setPrinterCompanyCd(String printerCompanyCd) {
		this.printerCompanyCd = printerCompanyCd;
	}

	public String getNmPrinter() {
		return nmPrinter;
	}

	public void setNmPrinter(String nmPrinter) {
		this.nmPrinter = nmPrinter;
	}

	public String getCdModel() {
		return cdModel;
	}

	public void setCdModel(String cdModel) {
		this.cdModel = cdModel;
	}

	public String getNoPos() {
		return noPos;
	}

	public void setNoPos(String noPos) {
		this.noPos = noPos;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getIpPort() {
		return ipPort;
	}

	public void setIpPort(String ipPort) {
		this.ipPort = ipPort;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}

	public String getDeviceFile() {
		return deviceFile;
	}

	public void setDeviceFile(String deviceFile) {
		this.deviceFile = deviceFile;
	}

	public String getDeviceNm() {
		return deviceNm;
	}

	public void setDeviceNm(String deviceNm) {
		this.deviceNm = deviceNm;
	}

	public String getBits() {
		return bits;
	}

	public void setBits(String bits) {
		this.bits = bits;
	}

	public String getParity() {
		return parity;
	}

	public void setParity(String parity) {
		this.parity = parity;
	}

	public String getStopBits() {
		return stopBits;
	}

	public void setStopBits(String stopBits) {
		this.stopBits = stopBits;
	}

	public String getCdPort() {
		return cdPort;
	}

	public void setCdPort(String cdPort) {
		this.cdPort = cdPort;
	}

	public String getBaudrate() {
		return baudrate;
	}

	public void setBaudrate(String baudrate) {
		this.baudrate = baudrate;
	}

	public String getCntPage() {
		return cntPage;
	}

	public void setCntPage(String cntPage) {
		this.cntPage = cntPage;
	}

	public Integer getIsDisableBluetooth() {
		return isDisableBluetooth;
	}

	public void setIsDisableBluetooth(Integer isDisableBluetooth) {
		this.isDisableBluetooth = isDisableBluetooth;
	}

	public Integer getIsEnableBluetooth() {
		return isEnableBluetooth;
	}

	public void setIsEnableBluetooth(Integer isEnableBluetooth) {
		this.isEnableBluetooth = isEnableBluetooth;
	}

	public String getConnectionType() {
		return connectionType;
	}

	public void setConnectionType(String connectionType) {
		this.connectionType = connectionType;
	}

	public String getUsbVid() {
		return usbVid;
	}

	public void setUsbVid(String usbVid) {
		this.usbVid = usbVid;
	}

	public String getUsbPid() {
		return usbPid;
	}

	public void setUsbPid(String usbPid) {
		this.usbPid = usbPid;
	}

	public String getUsbProtocol() {
		return usbProtocol;
	}

	public void setUsbProtocol(String usbProtocol) {
		this.usbProtocol = usbProtocol;
	}

	public Integer getYnUse() {
		return ynUse;
	}

	public void setYnUse(Integer ynUse) {
		this.ynUse = ynUse;
	}

	public String getAccessPwd() {
		return accessPwd;
	}

	public void setAccessPwd(String accessPwd) {
		this.accessPwd = accessPwd;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	@Override
	public String toString() {
		return "PosPrinterInfo [cdCompany=" + cdCompany + ", cdStore=" + cdStore + ", cdPrinter=" + cdPrinter
				+ ", printerType=" + printerType + ", printerCompanyCd=" + printerCompanyCd + ", nmPrinter=" + nmPrinter
				+ ", cdModel=" + cdModel + ", noPos=" + noPos + ", ip=" + ip + ", ipPort=" + ipPort + ", macAddress="
				+ macAddress + ", deviceFile=" + deviceFile + ", deviceNm=" + deviceNm + ", bits=" + bits + ", parity="
				+ parity + ", stopBits=" + stopBits + ", cdPort=" + cdPort + ", baudrate=" + baudrate + ", cntPage="
				+ cntPage + ", isDisableBluetooth=" + isDisableBluetooth + ", isEnableBluetooth=" + isEnableBluetooth
				+ ", connectionType=" + connectionType + ", usbVid=" + usbVid + ", usbPid=" + usbPid + ", usbProtocol="
				+ usbProtocol + ", ynUse=" + ynUse + ", accessPwd=" + accessPwd + ", userPwd=" + userPwd + "]";
	}
	


}
