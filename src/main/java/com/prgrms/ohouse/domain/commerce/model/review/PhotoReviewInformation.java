package com.prgrms.ohouse.domain.commerce.model.review;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PhotoReviewInformation extends ReviewInformation {
	private String reviewImageUrl;

	public PhotoReviewInformation(Long id, ReviewAuthor user, int reviewPoint, String contents, int helpPoint,
		LocalDateTime createdAt, String reviewImageUrl) {
		super(id, user, reviewPoint, contents, helpPoint, createdAt);
		this.reviewImageUrl = reviewImageUrl;
	}

	public static PhotoReviewInformation from(Review review) {
		return new PhotoReviewInformation(review.getId(), ReviewAuthor.from(review.getUser()), review.getReviewPoint(),
			review.getContents(), review.getHelpPoint(), review.getCreatedAt(), review.getReviewImage().getUrl());
	}
}
