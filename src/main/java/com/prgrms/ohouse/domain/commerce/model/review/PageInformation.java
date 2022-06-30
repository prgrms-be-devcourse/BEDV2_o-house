package com.prgrms.ohouse.domain.commerce.model.review;

import org.springframework.data.domain.Page;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Data
public class PageInformation {
	private int pageNumber;
	private int pageSize;
	private int totalPages;
	private int numberOfElements;
	private long totalElements;

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

