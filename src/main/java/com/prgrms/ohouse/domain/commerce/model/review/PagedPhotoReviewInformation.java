package com.prgrms.ohouse.domain.commerce.model.review;

import java.util.ArrayList;
import java.util.List;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PagedPhotoReviewInformation {
	private PageInformation pageInformation;
	private List<PhotoReviewInformation> content = new ArrayList<>();

	public static PagedPhotoReviewInformation of(PageInformation pageInformation, List<Review> reviews) {
		PagedPhotoReviewInformation instance = new PagedPhotoReviewInformation();
		instance.pageInformation = pageInformation;
		reviews.forEach(review -> instance.content.add(PhotoReviewInformation.from(review)));
		return instance;
	}
}
