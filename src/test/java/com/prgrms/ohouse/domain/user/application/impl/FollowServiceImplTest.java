package com.prgrms.ohouse.domain.user.application.impl;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

	@AfterEach
	void teardown() {
		followRepository.deleteAll();
		userRepository.deleteAll();
	}

	@Test
	void followUserTest() {
		followService.followUser(from.getId(), to.getId());

		Optional<Follow> follow = followRepository.findByFromUserAndToUser(from, to);
		Optional<User> findByFromUser = userRepository.findById(from.getId());
		Optional<User> findByToUser = userRepository.findById(to.getId());

		assertThat(findByFromUser.get().getFollowingCount(), is(1));
		assertThat(findByToUser.get().getFollowerCount(), is(1));
		assertThat(follow.isPresent(), is(true));
	}

	@Test
	void unfollowUserTest() {
		followService.followUser(from.getId(), to.getId());
		followService.unfollowUser(from.getId(), to.getId());

		Optional<Follow> follow = followRepository.findByFromUserAndToUser(from, to);
		Optional<User> findByFromUser = userRepository.findById(from.getId());
		Optional<User> findByToUser = userRepository.findById(to.getId());

		assertThat(findByFromUser.get().getFollowingCount(), is(0));
		assertThat(findByToUser.get().getFollowerCount(), is(0));
		assertThat(follow.isPresent(), is(false));

	}

	@Test
	@DisplayName("카운트 동시성 테스트")
	void addCountsTest() throws InterruptedException {

		int count =20;
		ExecutorService service = Executors.newFixedThreadPool(count);
		ArrayList<User> followedUserList = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			User user = User.builder()
				.nickname("user" + i)
				.email("user" + i + "@gmail.com")
				.password("testPassword12")
				.build();
			userRepository.save(user);
			followedUserList.add(user);
		}

		System.out.println("Thread start=========");
		for (int i = 0; i < count; i++) {
			int index = i;
			service.execute(() -> {
				try {
					followService.followUser(from.getId(), followedUserList.get(index).getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			});
		}

		Thread.sleep(1000);
		User findUser = userRepository.findById(from.getId()).get();
		System.out.println("Thread end =======");
		assertThat(findUser.getFollowingCount(), is(count));
	}
}