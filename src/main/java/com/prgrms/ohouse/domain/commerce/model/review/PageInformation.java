package com.prgrms.ohouse.domain.commerce.model.review;

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
}

