package com.prgrms.ohouse.web.user.results;

import lombok.Data;

@Data
public class ErrorResult {

	private int code;
	private String message;

	private ErrorResult(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ErrorResult build(ErrorCode errorCode) {
		return new ErrorResult(errorCode.getCode(), errorCode.getMessage());
	}
}
