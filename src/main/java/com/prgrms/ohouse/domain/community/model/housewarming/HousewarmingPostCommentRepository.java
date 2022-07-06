package com.prgrms.ohouse.domain.community.model.housewarming;

import java.util.Optional;

public interface HousewarmingPostCommentRepository {
	HousewarmingPostComment save(HousewarmingPostComment comment);

	Optional<HousewarmingPostComment> findById(Long commentId);
}
