package com.prgrms.ohouse.domain.commerce.model.review;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewInformation {
	private Long id;
	private ReviewAuthor user;
	private int reviewPoint;
	private String contents;
	private int helpPoint;
	private LocalDateTime createdAt;

	public static ReviewInformation from(Review review){
		return ReviewInformation.builder()
			.id(review.getId())
			.user(ReviewAuthor.from(review.getUser()))
			.reviewPoint(review.getReviewPoint())
			.contents(review.getContents())
			.helpPoint(review.getHelpPoint())
			.createdAt(review.getCreatedAt())
			.build();
	}
}
