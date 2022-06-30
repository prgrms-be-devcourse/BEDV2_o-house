package com.prgrms.ohouse.domain.user.application.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.user.application.FollowService;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;
import com.prgrms.ohouse.domain.user.model.exception.UserNotFoundException;
import com.prgrms.ohouse.domain.user.model.follow.Follow;
import com.prgrms.ohouse.domain.user.model.follow.FollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FollowServiceImpl implements FollowService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	@Transactional
	public void followUser(Long userId, Long toUserId) {
		User fromUser = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("AuthUser not found. Try Again."));

		User toUser = userRepository.findById(toUserId)
			.orElseThrow(() -> new UserNotFoundException("User to follow not found. Try Again."));

		followRepository.save(Follow.create(fromUser, toUser));
	}

	@Transactional
	public void unfollowUser(Long userId, Long toUserId) {
		User fromUser = userRepository.findById(userId)
			.orElseThrow(() -> new UserNotFoundException("AuthUser not found. Try Again."));

		User toUser = userRepository.findById(toUserId)
			.orElseThrow(() -> new UserNotFoundException("User to unfollow not found. Try Again."));

		Optional<Follow> findFollow = followRepository.findByFromUserAndToUser(fromUser, toUser);
		if (findFollow.isPresent()) {
			followRepository.delete(findFollow.get());
		}

	}

}
