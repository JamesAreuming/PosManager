package com.jc.pico.controller.clerk;

import java.security.InvalidParameterException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.pico.exception.DataNotFoundException;
import com.jc.pico.exception.DataNotRegisteredException;
import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.NoPermissionException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.ClerkResult;
import com.jc.pico.utils.bean.ClerkResult.ErrorCode;

@ControllerAdvice(basePackageClasses = { ClerkApiController.class })
public class ClerkApiErrorController {

	private static Logger logger = LoggerFactory.getLogger(ClerkApiErrorController.class);

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<ClerkResult> haneleException(HttpServletRequest request, Exception e) {
		logger.error("haneleException", e);
		ClerkResult result = new ClerkResult();
		result.setError(ClerkResult.ErrorCode.ERROR.code, e.getMessage());
		return new ResponseEntity<>(result, HttpStatus.SERVICE_UNAVAILABLE);
	}

	/**
	 * 조회 결과 없음
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DataNotFoundException.class)
	@ResponseBody
	public ResponseEntity<ClerkResult> haneleException(HttpServletRequest request, DataNotFoundException e) {
		ClerkResult result = new ClerkResult();
		result.setError(ClerkResult.ErrorCode.DATA_NOT_FOUND.code, e.getMessage());
		return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
	}

	/**
	 * 데이터 등록 실패
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(DataNotRegisteredException.class)
	@ResponseBody
	public ResponseEntity<ClerkResult> handleDataNotRegistered(HttpServletRequest request,
			DataNotRegisteredException e) {
		ClerkResult result = new ClerkResult();
		result.setError(ClerkResult.ErrorCode.DATA_NOT_FOUND.code, e.getMessage());
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 처리 불가 에러
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(RequestResolveException.class)
	@ResponseBody
	public ResponseEntity<ClerkResult> handleDataNotRegistered(HttpServletRequest request, RequestResolveException e) {
		ClerkResult result = new ClerkResult();
		result.setError(e.getCode(), e.getMessage());
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 잘못된 파라미터로의 요청
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(InvalidParamException.class)
	public ResponseEntity<ClerkResult> handleInvalidParam(HttpServletRequest request, InvalidParamException e) {
		ClerkResult result = new ClerkResult();
		result.setError(ErrorCode.INVALID_PARAMETER, "Invalid paramter");
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 잘못된 파라미터
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(InvalidParameterException.class)
	@ResponseBody
	public ResponseEntity<ClerkResult> handleInvalidParameterException(HttpServletRequest request,
			InvalidParameterException e) {
		ClerkResult result = new ClerkResult();
		result.setError(ErrorCode.INVALID_PARAMETER, e.getMessage());
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 접근 권한 없음
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(NoPermissionException.class)
	public ResponseEntity<ClerkResult> handleNoPermissionException(HttpServletRequest request,
			NoPermissionException e) {
		ClerkResult result = new ClerkResult();
		result.setError(ErrorCode.NO_PERMISSION, "No permission");
		return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
	}

}
