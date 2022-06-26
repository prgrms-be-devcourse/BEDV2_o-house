package com.prgrms.ohouse.domain.commerce.domain.application.command;

import java.util.List;

import org.springframework.data.domain.Slice;

import lombok.Getter;

@Getter
public class SliceCommand<T> {
	private final List<T> contents;
	private final int size;
	private final boolean isLastPage;
	private final boolean hasNext;

	public SliceCommand(Slice<T> slice) {
		this.contents = slice.getContent();
		this.size = slice.getSize();
		this.isLastPage = slice.isLast();
		this.hasNext = slice.hasNext();
	}
}
