package com.prgrms.ohouse.web.results;

import org.springframework.http.ResponseEntity;

public class UserLoginResult {

	private UserLoginResult() {
	}

	public static ResponseEntity<ApiResult> build(String token) {
		ApiResult result = ApiResult.create(200, "Login succeed.", null);
		return ResponseEntity.ok()
			.header("Authorization", token)
			.body(result);
	}
}
