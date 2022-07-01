package com.prgrms.ohouse.domain.user.model.exception;

public class DuplicateNicknameException extends RuntimeException {
	public DuplicateNicknameException(String message) {
		super(message);
	}
}
