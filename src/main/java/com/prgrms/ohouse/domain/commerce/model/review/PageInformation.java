package com.prgrms.ohouse.domain.commerce.model.review;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PageInformation {
	private final int pageNumber;
	private final int pageSize;
	private final int totalPages;
	private final int numberOfElements;
	private final long totalElements;

	public static PageInformation createNewPageInformation(Page<Review> reviewPage) {
		return PageInformation.builder()
			.totalElements(reviewPage.getTotalElements())
			.numberOfElements(reviewPage.getNumberOfElements())
			.totalPages(reviewPage.getTotalPages())
			.pageNumber(reviewPage.getNumber())
			.pageSize(reviewPage.getSize())
			.build();
	}
}

