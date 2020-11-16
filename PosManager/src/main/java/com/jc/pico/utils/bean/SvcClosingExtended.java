package com.jc.pico.utils.bean;

import java.util.List;

import com.jc.pico.bean.SvcClosing;
import com.jc.pico.bean.SvcClosingDetail;

/**
 * 마감을 확장한 Bean
 * @author green
 *
 */
public class SvcClosingExtended extends SvcClosing {
	/**
	 * 마감 상세 리스트
	 */
	private List<SvcClosingDetail> svcClosingDetails = null;

	/**
	 * 마감 상세 리스트 Getter
	 * @return 마감 상세 리스트
	 */
	public List<SvcClosingDetail> getSvcClosingDetails() {
		return svcClosingDetails;
	}

	/**
	 * 마감 상세 리스트 Setter
	 * @param svcClosingDetails 마감 상세 리스트
	 */
	public void setSvcClosingDetails(List<SvcClosingDetail> svcClosingDetails) {
		this.svcClosingDetails = svcClosingDetails;
	}
}
