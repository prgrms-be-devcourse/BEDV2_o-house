package com.prgrms.ohouse.domain.community.application.impl;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.prgrms.ohouse.domain.community.application.FollowingFeedInfoResult;
import com.prgrms.ohouse.domain.community.application.FollowingFeedService;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FollowingFeedServiceImpl implements FollowingFeedService {
	private final UserRepository userRepository;
	private final HousewarmingPostRepository housewarmingPostRepository;

	@Override
	public Slice<FollowingFeedInfoResult> getFeedPosts(Long authId, Pageable pageable) {
		User user = userRepository.findById(authId).orElseThrow(() ->
			new AccessDeniedException("Login required."));
		return housewarmingPostRepository.findSliceByfollowingUser(user, pageable);
	}
}
