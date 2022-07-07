package com.prgrms.ohouse.domain.commerce.model.review.exception;

public class ReviewUpdateFailException extends RuntimeException {

	public ReviewUpdateFailException(Throwable cause) {
		super(cause);
	}

	public ReviewUpdateFailException(String message, Throwable cause) {
		super(message, cause);
	}
}
