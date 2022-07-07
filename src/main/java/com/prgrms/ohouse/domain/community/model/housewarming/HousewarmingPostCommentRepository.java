package com.prgrms.ohouse.domain.community.model.housewarming;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.domain.community.application.HousewarmingPostCommentInfoResult;

public interface HousewarmingPostCommentRepository {
	HousewarmingPostComment save(HousewarmingPostComment comment);

	Optional<HousewarmingPostComment> findById(Long commentId);

	void delete(HousewarmingPostComment comment);

	Slice<HousewarmingPostCommentInfoResult> findSliceByHwPost(Pageable pageable, HousewarmingPost hwPost);
}
