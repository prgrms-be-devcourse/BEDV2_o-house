package com.prgrms.ohouse.web.user.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.prgrms.ohouse.config.WithMockCustomUser;
import com.prgrms.ohouse.domain.common.security.AuthUtils;
import com.prgrms.ohouse.domain.user.application.FollowService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class FollowRestControllerTest {

	@Mock
	private FollowService followService;

	@InjectMocks
	private FollowRestController followRestController;

	private MockMvc mockMvc;
	private Long userId = 1L;

	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(followRestController)
			.setControllerAdvice(FollowApiExceptionHandler.class)
			.alwaysDo(print())
			.build();
	}

	@Test
	@WithMockCustomUser
	void followTest() throws Exception {

		doNothing().when(followService).followUser(AuthUtils.getAuthUser().getId(), userId);
		mockMvc.perform(post("/api/v0/user/{userId}/follow", userId))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("User Follow Success")));
	}

	@Test
	@WithMockCustomUser
	void unfollowTest() throws Exception {
		doNothing().when(followService).unfollowUser(AuthUtils.getAuthUser().getId(), userId);
		mockMvc.perform(delete("/api/v0/user/{userId}/follow", userId))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("User Unfollow Success")));
	}

}