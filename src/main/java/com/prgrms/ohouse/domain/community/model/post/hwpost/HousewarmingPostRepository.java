package com.prgrms.ohouse.domain.community.model.post.hwpost;

import java.util.Optional;

public interface HousewarmingPostRepository {
	HousewarmingPost save(HousewarmingPost post);

	Optional<HousewarmingPost> findById(Long postId);
}
