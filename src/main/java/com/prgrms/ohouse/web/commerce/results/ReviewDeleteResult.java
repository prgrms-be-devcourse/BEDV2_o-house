package com.prgrms.ohouse.web.commerce.results;

import lombok.Data;

@Data
public class ReviewDeleteResult {
	private String message;
	private Long id;

	public static ReviewDeleteResult ok(Long id) {
		ReviewDeleteResult instance = new ReviewDeleteResult();
		instance.setMessage("review deleted successful");
		instance.setId(id);
		return instance;
	}
}
