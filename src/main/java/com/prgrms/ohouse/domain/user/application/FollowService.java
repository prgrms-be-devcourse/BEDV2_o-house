package com.prgrms.ohouse.domain.user.application;

public interface FollowService {

	void followUser(Long userId, Long toUserId);

	void unfollowUser(Long userId, Long toUserId);
}
