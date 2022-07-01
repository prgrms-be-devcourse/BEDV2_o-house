package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.infrastructure.repository.custom.JpaHousewarmingPostRepositoryCustom;

// @Repository
public interface JpaHousewarmingPostRepository extends JpaRepository<HousewarmingPost, Long>,
	HousewarmingPostRepository, JpaHousewarmingPostRepositoryCustom {
}
