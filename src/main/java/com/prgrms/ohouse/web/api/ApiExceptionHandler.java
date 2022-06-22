package com.prgrms.ohouse.web.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.ohouse.domain.user.model.DuplicateEmailException;
import com.prgrms.ohouse.web.results.ApiResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ApiResult> handleIllegalArgument(IllegalArgumentException e) {
		log.warn(e.getMessage(), e);
		ApiResult body = ApiResult.error(400, e.getMessage());
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ApiResult> handlerDuplicateEmail(DuplicateEmailException e) {
		log.warn(e.getMessage(), e);
		ApiResult body = ApiResult.error(400, e.getMessage());
		return ResponseEntity.badRequest().body(body);
	}
}
