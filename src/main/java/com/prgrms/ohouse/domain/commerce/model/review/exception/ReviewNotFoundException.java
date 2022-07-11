package com.prgrms.ohouse.domain.commerce.model.review.exception;

public class ReviewNotFoundException extends RuntimeException {
	public ReviewNotFoundException() {
		super("fail to find review");
	}
}
