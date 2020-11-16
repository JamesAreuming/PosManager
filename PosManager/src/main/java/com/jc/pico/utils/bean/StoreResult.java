package com.jc.pico.utils.bean;

public class StoreResult {

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

	public void setSuccess(boolean success) {
		if (success) {
			setError(ErrorCode.SUCCESS, null);
		} else {
			setError(ErrorCode.ERROR, "");
		}
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

		SUCCESS("EST0000") // 성공
		, NO_PERMISSION("EST0001") // 접근 권한 없음
		, NOT_SUPPORTED_DATE_RANGE("EST0002") // 지원하지 않는 날짜 범위
		, NOT_REGISTERED_DEVICE("EST0003") // 등록되지 않은 디바이스
		, INVALID_PARAMETER("EST0004") // 잘못된 파라미터
		, ERROR_LOGIN_LIMIT_EXCEEDED("EST0005") // 로그인 시도 횟수 초과
		, ERROR("EST9999"); // 그외 비계획 에러 

		public final String code;

		ErrorCode(String code) {
			this.code = code;
		}

	} // ErrorCode

	@Override
	public String toString() {
		return "StoreResult [header=" + header + ", data=" + data + "]";
	}
}
