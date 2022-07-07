package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostComment;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostCommentRepository;
import com.prgrms.ohouse.infrastructure.repository.custom.JpaHousewarmingPostCommentRepositoryCustom;

public interface JpaHousewarmingPostCommentRepository
	extends JpaRepository<HousewarmingPostComment, Long>, HousewarmingPostCommentRepository,
	JpaHousewarmingPostCommentRepositoryCustom {

}
