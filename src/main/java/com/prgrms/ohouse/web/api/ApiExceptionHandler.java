package com.prgrms.ohouse.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.ohouse.domain.user.model.DuplicateEmailException;
import com.prgrms.ohouse.domain.user.model.FailedLoginException;
import com.prgrms.ohouse.web.results.ErrorCode;
import com.prgrms.ohouse.web.results.ErrorResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResult> handleIllegalArgument(IllegalArgumentException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.INVALID_INPUT_VALUE);
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ErrorResult> handleDuplicateEmail(DuplicateEmailException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.DUPLICATED_EMAIL);
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(FailedLoginException.class)
	public ResponseEntity<ErrorResult> handleFailedLogin(FailedLoginException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.FAILED_LOGIN);
		return ResponseEntity.badRequest().body(body);
	}
}
