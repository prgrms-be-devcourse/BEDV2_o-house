package com.prgrms.ohouse.web.results;

import lombok.Data;

@Data
public class ApiResult<T> {

	private final int code;
	private String message;
	private T response;

	private ApiResult(int code, String message, T response) {
		this.code = code;
		this.message = message;
		this.response = response;
	}

	public static <T> ApiResult<T> create(int code, String message, T response) {
		return new ApiResult<>(code, message, response);
	}

	public static ApiResult error(int code, String message) {
		return new ApiResult(code, message, null);
	}

}
