package com.jc.pico.exception;

/**
 * 공통 에러 코드와 메시지를 담은 예외
 * 계획된 에러에 사용
 *
 */
public class RequestResolveException extends Exception {

	private static final long serialVersionUID = 1L;

	private String code;

	public RequestResolveException(String code, String message) {
		super(message);
		this.code = code;
	}

	public RequestResolveException(String code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;

	}

	public String getCode() {
		return code;
	}

}
