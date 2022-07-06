package com.prgrms.ohouse.domain.user.application.impl;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;
import com.prgrms.ohouse.domain.user.model.follow.Follow;
import com.prgrms.ohouse.domain.user.model.follow.FollowRepository;

@Transactional
@SpringBootTest
class FollowServiceImplTest {

	@Autowired
	private FollowServiceImpl followService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private FollowRepository followRepository;

	User from = User.builder()
		.nickname("from_user")
		.email("from@gmail.com")
		.password("testPassword12")
		.build();

	User to = User.builder()
		.nickname("to_user")
		.email("to@gmail.com")
		.password("testPassword12")
		.build();

	@Test
	void followUserTest() {
		userRepository.save(from);
		userRepository.save(to);
		followService.followUser(from.getId(), to.getId());

		Optional<Follow> follow = followRepository.findByFromUserAndToUser(from, to);

		assertThat(follow.isPresent(), is(true));
	}

	@Test
	void unfollowUserTest() {
		userRepository.save(from);
		userRepository.save(to);

		followService.followUser(from.getId(), to.getId());
		followService.unfollowUser(from.getId(), to.getId());

		Optional<Follow> follow = followRepository.findByFromUserAndToUser(from, to);

		assertThat(follow.isPresent(), is(false));

	}
}