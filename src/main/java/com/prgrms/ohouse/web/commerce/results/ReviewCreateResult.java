package com.prgrms.ohouse.web.commerce.results;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter(AccessLevel.PRIVATE)
public class ReviewCreateResult {
	private String message;
	private Long id;

	public static ReviewCreateResult ok(Long id) {
		ReviewCreateResult instance = new ReviewCreateResult();
		instance.setMessage("new review created successful");
		instance.setId(id);
		return instance;
	}
}
