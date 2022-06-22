package com.prgrms.ohouse.web.results;

import org.springframework.http.ResponseEntity;

public class UserCreateResult {

	private UserCreateResult() {
	}

	public static ResponseEntity<ApiResult> build() {
		ApiResult result = ApiResult.create(200, "User Create Success", null);
		return ResponseEntity.ok()
			.body(result);
	}
}
