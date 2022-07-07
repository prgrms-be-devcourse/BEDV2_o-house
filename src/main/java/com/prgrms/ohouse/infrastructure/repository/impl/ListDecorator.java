package com.prgrms.ohouse.infrastructure.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.NONE)
class ListDecorator {

	static <T, R> Slice<R> decorateToSlice(List<T> list, Function<T, R> dtoMapper, Pageable pageable) {
		var dtoList = list.stream()
			.map(dtoMapper)
			.collect(Collectors.toList());
		var hasNext = pageable.getPageSize() + 1 == dtoList.size();
		if (hasNext) {
			dtoList.remove(dtoList.size() - 1);
		}
		return new SliceImpl<>(
			Collections.unmodifiableList(dtoList),
			PageRequest.of(pageable.getPageNumber(), dtoList.size()),
			hasNext);
	}
}
