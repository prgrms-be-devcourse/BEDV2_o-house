package com.prgrms.ohouse.domain.commerce.model.review;

import java.time.LocalDateTime;

import com.prgrms.ohouse.domain.user.model.User;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewInformation {
	private Long id;
	private User user;
	private int reviewPoint;
	private String contents;
	private int helpPoint;
	private LocalDateTime createdAt;

	public static ReviewInformation from(Review review){
		return ReviewInformation.builder()
			.id(review.getId())
			.user(review.getUser())
			.reviewPoint(review.getReviewPoint())
			.contents(review.getContents())
			.helpPoint(review.getHelpPoint())
			.createdAt(review.getCreatedAt())
			.build();
	}
}
