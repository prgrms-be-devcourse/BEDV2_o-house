package com.prgrms.ohouse.web.commerce.results;

import lombok.Data;

@Data
public class ReviewUpdateResult {
	private String message;
	private Long id;

	public static ReviewUpdateResult ok(Long id) {
		ReviewUpdateResult instance = new ReviewUpdateResult();
		instance.setMessage("review modified successful");
		instance.setId(id);
		return instance;
	}
}
