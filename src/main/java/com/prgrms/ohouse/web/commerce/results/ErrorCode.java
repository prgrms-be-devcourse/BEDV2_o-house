package com.prgrms.ohouse.web.commerce.results;

public enum ErrorCode {
	INVALID_USER_ERROR(400, "Invalid  value."),

	DUPLICATED_EMAIL(409, "Duplicated Email."),
	DUPLICATED_NICKNAME(409, "Duplicated Nickname"),
	FAILED_LOGIN(400, "Login Failed."),
	UNAUTHORIZED_REQUEST(401, "Unauthorized Request"),
	INTERNAL_ERROR(500, "Internal Server Error."),
	FAILED_FOLLOW(409, "Cannot follow this user."),
	FAILED_UNFOLLOW(409, "Cannot unfollow this user.");

	private final int code;
	private final String message;

	ErrorCode(final int code, final String message) {
		this.code = code;
		this.message = message;
	}

}
