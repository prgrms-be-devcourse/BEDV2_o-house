package com.prgrms.ohouse.domain.commerce.model.review;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class PagedReviewInformation {
	private PageInformation pageInformation;
	private List<ReviewInformation> reviews = new ArrayList<>();

	public static PagedReviewInformation of(PageInformation pageInformation, List<Review> reviews) {
		PagedReviewInformation instance = new PagedReviewInformation();
		instance.pageInformation = pageInformation;
		reviews.forEach(review -> instance.reviews.add(ReviewInformation.from(review)));
		return instance;
	}


}
