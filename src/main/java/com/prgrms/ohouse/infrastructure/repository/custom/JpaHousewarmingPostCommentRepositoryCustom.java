package com.prgrms.ohouse.infrastructure.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.domain.community.application.HousewarmingPostCommentInfoResult;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;

public interface JpaHousewarmingPostCommentRepositoryCustom {
	Slice<HousewarmingPostCommentInfoResult> findSliceByHwPost(Pageable pageable, HousewarmingPost housewarmingPost);
}
