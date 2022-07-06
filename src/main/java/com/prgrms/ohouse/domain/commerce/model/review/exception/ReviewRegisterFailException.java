package com.prgrms.ohouse.domain.commerce.model.review.exception;

public class ReviewRegisterFailException extends RuntimeException {
	public ReviewRegisterFailException(String message) {
		super(message);
	}

	public ReviewRegisterFailException(String message, Throwable cause) {
		super(message, cause);
	}
}
