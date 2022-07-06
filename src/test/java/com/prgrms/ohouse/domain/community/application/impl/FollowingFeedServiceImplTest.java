package com.prgrms.ohouse.domain.community.application.impl;

import static org.hamcrest.MatcherAssert.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.community.application.FollowingFeedService;
import com.prgrms.ohouse.domain.user.application.FollowService;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.domain.user.model.UserRepository;
import com.prgrms.ohouse.infrastructure.TestDataProvider;

@Transactional
@SpringBootTest(properties = "spring.profiles.active:test")
class FollowingFeedServiceImplTest {

	@Autowired
	private TestDataProvider dataProvider;

	@MockBean
	private UserAuditorAware userAuditorAware;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FollowService followService;

	@Autowired
	private FollowingFeedService followingFeedService;

	@Test
	void getFollowingFeedTest() {
		//given
		//--posts
		var inFeedUser = dataProvider.insertGuestUser("InUser");
		dataProvider.insertHousewarmingPostWithAuthor(userAuditorAware, inFeedUser, 1);
		dataProvider.insertHousewarmingPostWithAuthor(userAuditorAware, inFeedUser, 2 );
		reset(userAuditorAware);

		var outFeedUser = dataProvider.insertGuestUser("OutUser");
		dataProvider.insertHousewarmingPostWithAuthor(userAuditorAware, outFeedUser, 3);
		dataProvider.insertHousewarmingPostWithAuthor(userAuditorAware, outFeedUser, 4 );

		//--authUser
		User fromUser = User.builder()
			.nickname("hj")
			.email("hjhj@gmail.com")
			.password("hjhjPass12")
			.build();
		userRepository.save(fromUser);

		//--follow
		followService.followUser(fromUser.getId(),inFeedUser.getId());

		//when
		PageRequest pageRequest = PageRequest.of(0, 5);
		var result = followingFeedService.getFeedPosts(fromUser.getId(), pageRequest);

		//then
		var content = result.getContent();
		assertThat(content, is(hasSize(2)));
		assertThat(content.get(0).getAuthor_nickname(), is(equalTo(inFeedUser.getNickname())));
		assertThat(content.get(1).getAuthor_nickname(), is(equalTo(inFeedUser.getNickname())));
	}

}