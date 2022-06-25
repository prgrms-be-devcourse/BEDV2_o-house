package com.prgrms.ohouse.web.api;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.model.DuplicateEmailException;
import com.prgrms.ohouse.web.requests.UserCreateRequest;

class UserRestControllerTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserRestController userRestController;
	private MockMvc mockMvc;
	private ObjectMapper objectMapper = new ObjectMapper();

	UserCreateRequest request = new UserCreateRequest("testuser", "test@gmail.com", "testPassword12");


	@BeforeEach
	void setup() {
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
			.setControllerAdvice(ApiExceptionHandler.class)
			.alwaysDo(print())
			.build();
	}

	@Test
	@DisplayName("회원가입 테스트")
	void userSignUpTest() throws Exception{

		String body = objectMapper.writeValueAsString(request);

		doNothing().when(userService).signUp(any());

		mockMvc.perform(post("/api/v0/user/signUp")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("User Create Success")));
	}

	@Test
	@DisplayName("회원가입 실패 테스트")
	void failedUserSignUpTest() throws Exception{

		String body = objectMapper.writeValueAsString(request);

		doThrow(new DuplicateEmailException("Duplicate Email.")).when(userService).signUp(any());

		mockMvc.perform(post("/api/v0/user/signUp")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(result ->
				assertThat(result.getResolvedException().getClass().getCanonicalName(),
					is(equalTo(DuplicateEmailException.class.getCanonicalName()))));
	}
}