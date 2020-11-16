package com.jc.pico.utils.bean;

public class ClerkResult {

	private Header header;
	private Object data;

	public Header getHeader() {
		return header;
	}

	public void setHeader(Header header) {
		this.header = header;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public void setError(ErrorCode errorCode, String errMsg) {
		if (header == null) {
			header = new Header();
		}
		header.setErrCd(errorCode.code);
		header.setErrMsg(errMsg);
	}

	public void setError(String errorCode, String errMsg) {
		if (header == null) {
			header = new Header();
		}
		header.setErrCd(errorCode);
		header.setErrMsg(errMsg);
	}

	public void setSuccess() {
		setError(ErrorCode.SUCCESS, null);
	}

	public static class Header {

		private String errCd;
		private String errMsg;

		public String getErrCd() {
			return errCd;
		}

		public void setErrCd(String errCd) {
			this.errCd = errCd;
		}

		public String getErrMsg() {
			return errMsg;
		}

		public void setErrMsg(String errMsg) {
			this.errMsg = errMsg;
		}
	}

	public static enum ErrorCode {

		SUCCESS("ECL0000") // 성공
		, ERROR("ECL9999") // 비계획 에러 (서버에러)
		, NO_PERMISSION("ECL0001") // 접근 권한 없음
		, INVALID_PARAMETER("ECL0002") // 잘못된 파라미터
		, DATA_NOT_FOUND("ECL0003") // 데이터 없음
		, LICENSE_INVALID("ECL0004") // 존재하지 않는 라이센스
		, LICENSE_USED("ECL0005") // 사용중인 라이센스 
		, LICENSE_UNUSED("ECL0006") // 미사용 라이센스
		, POS_NOT_OPENED("ECL0007") // 포스 오픈 전 상태
		, POS_CLOSED("ECL0008") // 포스 마감 상태
		, TABLE_NOT_FOUND("ECL0009") // 테이블이 존재하지 않음
		, ORDER_NOT_FOUND("ECL0010") // 주문이 존재하지 않음
		, ORDER_EXIST("ECL0011") // 주문이 이미 존재
		, COUPON_NOT_FOUND("ECL0012") // 쿠폰이 존재하지 않음
		, COUPON_DISABLED("ECL0013") // 쿠폰 사용이 중단됨.
		, ORDER_SAVE_FAILED("ECL0014") // 주문 저장 실패
		, STORE_NOT_FOUND("ECL0015") // 매장이 존재하지 않음
		, ORDER_CAN_NOT_APPROVED("ECL0016") // 주문이 승인되지 못함
		, STORE_NOT_ALIVE("ECL0017") // 매장이 살아 있지 않음 (포스가 응답 없음)
		, TABLE_NOT_PERMISSION("ECL0018") // 테이블 주문 권한이 없음
		, ERROR_LOGIN_LIMIT_EXCEEDED("ECL0019") // 로그인 시도 횟수 초과
		
		,STORE_INFO_NOT_FOUND("ECL0100");
		;

		public final String code;

		private ErrorCode(String code) {
			this.code = code;
		}

	} // ErrorCode

}
