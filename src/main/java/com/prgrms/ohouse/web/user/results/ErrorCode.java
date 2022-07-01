package com.prgrms.ohouse.web.user.results;

import lombok.Getter;

@Getter
public enum ErrorCode {

	INVALID_INPUT_VALUE(400, "Invalid input value."),
	DUPLICATED_EMAIL(409, "Duplicated Email."),
	DUPLICATED_NICKNAME(409, "Duplicated Nickname"),
	FAILED_LOGIN(400, "Login Failed."),
	UNAUTHORIZED_REQUEST(401, "Unauthorized Request");

	private final int code;
	private final String message;

	ErrorCode(final int code, final String message) {
		this.code = code;
		this.message = message;
	}

}
