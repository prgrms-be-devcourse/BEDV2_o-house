package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.user.model.follow.Follow;
import com.prgrms.ohouse.domain.user.model.follow.FollowRepository;

public interface JpaFollowRepository extends FollowRepository, JpaRepository<Follow, Long> {
}
