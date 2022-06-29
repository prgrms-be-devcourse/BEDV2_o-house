package com.prgrms.ohouse.web.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.ohouse.domain.user.model.exception.DuplicateEmailException;
import com.prgrms.ohouse.domain.user.model.exception.DuplicateNicknameException;
import com.prgrms.ohouse.domain.user.model.exception.FailedLoginException;
import com.prgrms.ohouse.web.user.results.ErrorCode;
import com.prgrms.ohouse.web.user.results.ErrorResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = UserRestController.class)
public class ApiExceptionHandler {

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResult> handleIllegalArgument(IllegalArgumentException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.INVALID_INPUT_VALUE);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(DuplicateEmailException.class)
	public ResponseEntity<ErrorResult> handleDuplicateEmail(DuplicateEmailException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.DUPLICATED_EMAIL);
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(DuplicateNicknameException.class)
	public ResponseEntity<ErrorResult> handleDuplicateNickname(DuplicateNicknameException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.DUPLICATED_NICKNAME);
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(FailedLoginException.class)
	public ResponseEntity<ErrorResult> handleFailedLogin(FailedLoginException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.FAILED_LOGIN);
		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
}
