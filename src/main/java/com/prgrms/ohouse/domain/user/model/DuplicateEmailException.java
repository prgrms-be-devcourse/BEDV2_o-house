package com.prgrms.ohouse.domain.user.model;

public class DuplicateEmailException extends RuntimeException{

	public DuplicateEmailException(String msg) {
		super(msg);
	}
}
