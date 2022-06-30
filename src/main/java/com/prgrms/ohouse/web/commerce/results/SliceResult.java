package com.prgrms.ohouse.web.commerce.results;

import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.Getter;

@Getter
public class SliceResult<T> {
	private final List<T> contents;
	private final int size;
	private final boolean isLastPage;
	private final boolean hasNext;

	public SliceResult(Slice<T> slice) {
		this.contents = slice.getContent();
		this.size = slice.getSize();
		this.isLastPage = slice.isLast();
		this.hasNext = slice.hasNext();
	}
}
