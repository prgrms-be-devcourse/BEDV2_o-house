package com.prgrms.ohouse.domain.community.model.housewarming;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface HousewarmingPostRepository {
	HousewarmingPost save(HousewarmingPost post);

	Optional<HousewarmingPost> findById(Long postId);

	void delete(HousewarmingPost hwPost);

	void incrementViewCount(Long postId);

	Slice<HousewarmingPost> findSliceBy(Pageable pageable);
}
