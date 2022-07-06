package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostComment;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostCommentRepository;

public interface JpaHousewarmingPostCommentRepository
	extends JpaRepository<HousewarmingPostComment, Long>, HousewarmingPostCommentRepository {

}
