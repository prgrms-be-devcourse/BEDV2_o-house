package com.prgrms.ohouse.domain.commerce.model.review;

import java.time.LocalDateTime;

import com.prgrms.ohouse.domain.user.model.User;

import lombok.Getter;

@Getter
public class PhotoReviewInformation extends ReviewInformation {
	private final String reviewImageUrl;

	public PhotoReviewInformation(Long id, User user, int reviewPoint, String contents, int helpPoint,
		LocalDateTime createdAt, String reviewImageUrl) {
		super(id, user, reviewPoint, contents, helpPoint, createdAt);
		this.reviewImageUrl = reviewImageUrl;
	}

	public static PhotoReviewInformation from(Review review) {
		return new PhotoReviewInformation(review.getId(), review.getUser(), review.getReviewPoint(),
			review.getContents(), review.getHelpPoint(), review.getCreatedAt(), review.getReviewImage().getUrl());
	}
}
