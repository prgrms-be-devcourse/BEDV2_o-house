package com.prgrms.ohouse.domain.commerce.web;

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
