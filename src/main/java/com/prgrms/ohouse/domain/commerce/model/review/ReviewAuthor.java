package com.prgrms.ohouse.domain.commerce.model.review;

import com.prgrms.ohouse.domain.user.model.User;

import lombok.Data;

@Data
public class ReviewAuthor {
	private String username;

	public static ReviewAuthor from(User user) {
		ReviewAuthor instance = new ReviewAuthor();
		instance.setUsername(user.getUsername());
		return instance;
	}
}
