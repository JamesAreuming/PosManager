package com.jc.pico.controller.store;

import java.security.InvalidParameterException;

import javax.naming.NoPermissionException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jc.pico.exception.InvalidParamException;
import com.jc.pico.exception.RequestResolveException;
import com.jc.pico.utils.bean.StoreResult;
import com.jc.pico.utils.bean.StoreResult.ErrorCode;

@ControllerAdvice(basePackageClasses = { StoreApiController.class })
public class StoreApiErrorController {

//	@Autowired
//	private MessageSource messageSource;

	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<StoreResult> haneleException(HttpServletRequest request, Exception e) {
		StoreResult result = new StoreResult();
		result.setError(ErrorCode.ERROR, e.getMessage());
		return new ResponseEntity<>(result, HttpStatus.SERVICE_UNAVAILABLE);
	}

	@ExceptionHandler(NoPermissionException.class)
	@ResponseBody
	public ResponseEntity<StoreResult> haneleNoPermissionException(HttpServletRequest request,
			NoPermissionException e) {

		StoreResult result = new StoreResult();
		result.setError(ErrorCode.NO_PERMISSION, "No permission.");
//		result.setError(ErrorCode.NO_PERMISSION,
//				messageSource.getMessage(ErrorCode.NO_PERMISSION.code, new String[] {}, request.getLocale()));
		return new ResponseEntity<>(result, HttpStatus.FORBIDDEN);
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
	public ResponseEntity<StoreResult> handleDataNotRegistered(HttpServletRequest request,
			RequestResolveException e) {
		StoreResult result = new StoreResult();
		result.setError(e.getCode(), e.getMessage());
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
	public ResponseEntity<StoreResult> handleInvalidParameterException(HttpServletRequest request,
			InvalidParameterException e) {
		StoreResult result = new StoreResult();
		result.setError(ErrorCode.INVALID_PARAMETER, e.getMessage());
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 잘못된 파라미터
	 * 
	 * @param request
	 * @param e
	 * @return
	 */
	@ExceptionHandler(InvalidParamException.class)
	@ResponseBody
	public ResponseEntity<StoreResult> handleInvalidParamException(HttpServletRequest request,
			InvalidParamException e) {
		StoreResult result = new StoreResult();
		result.setError(ErrorCode.INVALID_PARAMETER, e.getMessage());
		return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
	}

}
