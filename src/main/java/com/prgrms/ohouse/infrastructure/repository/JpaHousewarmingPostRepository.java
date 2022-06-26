package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPost;
import com.prgrms.ohouse.domain.community.model.post.hwpost.HousewarmingPostRepository;

@Repository
public interface JpaHousewarmingPostRepository extends JpaRepository<HousewarmingPost, Long>,
	HousewarmingPostRepository {
}
