package com.prgrms.ohouse.web.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.ohouse.domain.user.model.exception.FollowFailedException;
import com.prgrms.ohouse.domain.user.model.exception.UnFollowFailedException;
import com.prgrms.ohouse.domain.user.model.exception.UserNotFoundException;
import com.prgrms.ohouse.web.user.results.ErrorCode;
import com.prgrms.ohouse.web.user.results.ErrorResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice(basePackageClasses = FollowRestController.class)
public class FollowApiExceptionHandler {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResult> handleUserNotFound(UserNotFoundException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.INTERNAL_ERROR);
		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(FollowFailedException.class)
	public ResponseEntity<ErrorResult> handleFailedFollow(FollowFailedException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.FAILED_FOLLOW);
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(UnFollowFailedException.class)
	public ResponseEntity<ErrorResult> handleFailedFollow(UnFollowFailedException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.build(ErrorCode.FAILED_UNFOLLOW);
		return new ResponseEntity<>(body, HttpStatus.CONFLICT);
	}
}
