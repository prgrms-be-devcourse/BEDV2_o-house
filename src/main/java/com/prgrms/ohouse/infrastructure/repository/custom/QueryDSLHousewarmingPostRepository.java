package com.prgrms.ohouse.infrastructure.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.domain.community.application.HousewarmingPostInfoResult;

public interface QueryDSLHousewarmingPostRepository {
	void incrementViewCount(Long postId);

	Slice<HousewarmingPostInfoResult> findSliceBy(Pageable pageable);
}
