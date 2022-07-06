package com.prgrms.ohouse.domain.commerce.model.review.exception;

public class ReviewUpdateFailException extends RuntimeException {
	public ReviewUpdateFailException(String message) {
		super(message);
	}

	public ReviewUpdateFailException(String message, Throwable cause) {
		super(message, cause);
	}
}
