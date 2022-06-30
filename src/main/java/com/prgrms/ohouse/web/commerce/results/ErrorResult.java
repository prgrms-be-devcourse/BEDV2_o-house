package com.prgrms.ohouse.web.commerce.results;

import org.springframework.http.HttpStatus;

public class ErrorResult {
	private HttpStatus code;
	private String message;

	private ErrorResult(HttpStatus code, String message) {
		this.code = code;
		this.message = message;
	}

	public static ErrorResult of(HttpStatus code, String message) {
		return new ErrorResult(code, message);
	}
}
