package com.jc.pico.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SvcStoreInfo {
    private String pgMerchantId;
	private String pgPrivateKey;
    private String pgPaymentKey;
    private boolean isMain;
    private List<SvcStoreInfoPrinter> printers;
    
    public String getPgMerchantId() {
		return pgMerchantId;
	}

	public void setPgMerchantId(String pgMerchantId) {
		this.pgMerchantId = pgMerchantId;
	}

	public String getPgPrivateKey() {
		return pgPrivateKey;
	}

	public void setPgPrivateKey(String pgPrivateKey) {
		this.pgPrivateKey = pgPrivateKey;
	}

	public String getPgPaymentKey() {
		return pgPaymentKey;
	}

	public void setPgPaymentKey(String pgPaymentKey) {
		this.pgPaymentKey = pgPaymentKey;
	}

	public boolean getIsMain() {
		return isMain;
	}

	public void setIsMain(boolean isMain) {
		this.isMain = isMain;
	}

	public List<SvcStoreInfoPrinter> getPrinters() {
		return printers;
	}

	public void setPrinters(List<SvcStoreInfoPrinter> printers) {
		this.printers = printers;
	}
}