package com.prgrms.ohouse.domain.community.model.housewarming;

import java.util.Optional;

public interface HousewarmingPostRepository {
	HousewarmingPost save(HousewarmingPost post);

	Optional<HousewarmingPost> findById(Long postId);

	void delete(HousewarmingPost hwPost);
}
