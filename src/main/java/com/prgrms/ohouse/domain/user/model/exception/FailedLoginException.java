package com.prgrms.ohouse.domain.user.model.exception;

public class FailedLoginException extends RuntimeException{

	public FailedLoginException(String message) {
		super(message);
	}
}
