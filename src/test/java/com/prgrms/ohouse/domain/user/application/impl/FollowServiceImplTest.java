package com.prgrms.ohouse.domain.user.application.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;
import com.prgrms.ohouse.domain.user.model.follow.Follow;
import com.prgrms.ohouse.domain.user.model.follow.FollowRepository;

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

	@BeforeEach
	void setup() {
		userRepository.save(from);
		userRepository.save(to);
	}

	@Test
	@Transactional
	void followUserTest() {
		followService.followUser(from.getId(), to.getId());
		Optional<Follow> follow = followRepository.findByFromUserAndToUser(from, to);

		assertThat(from.getFollowingCount(), is(1));
		assertThat(to.getFollowerCount(), is(1));
		assertThat(follow.isPresent(), is(true));
		assertThat(follow.get().getFromUser(), is(equalTo(from)));
		assertThat(follow.get().getToUser(), is(equalTo(to)));
	}
}