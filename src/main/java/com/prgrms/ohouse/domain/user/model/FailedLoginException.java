package com.prgrms.ohouse.domain.user.model;

public class FailedLoginException extends RuntimeException{

	public FailedLoginException(String message) {
		super(message);
	}
}
