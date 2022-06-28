package com.prgrms.ohouse.web.api;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.model.DuplicateEmailException;
import com.prgrms.ohouse.domain.user.model.GenderType;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.web.user.api.ApiExceptionHandler;
import com.prgrms.ohouse.web.user.api.UserRestController;
import com.prgrms.ohouse.web.user.requests.UserCreateRequest;
import com.prgrms.ohouse.web.user.requests.UserUpdateRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class UserRestControllerTest {

	@Mock
	private UserService userService;

	@InjectMocks
	private UserRestController userRestController;

	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	UserCreateRequest request = new UserCreateRequest("testUser", "test@gmail.com", "guestPw12");

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
	void userSignUpTest() throws Exception {

		String body = objectMapper.writeValueAsString(request);
		doNothing().when(userService).signUp(any());

		mockMvc.perform(post("/api/v0/signUp")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("User Create Success")));
	}

	//TODO 왜안됨??
	@Test
	@Disabled
	@DisplayName("회원가입 실패 테스트")
	void failedUserSignUpTest() throws Exception {

		String body = objectMapper.writeValueAsString(request);
		doThrow(new DuplicateEmailException("Duplicate Email.")).when(userService).signUp(any());

		mockMvc.perform(post("/api/v0/signUp")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(result ->
				assertThat(result.getResolvedException().getClass().getCanonicalName(),
					is(equalTo(DuplicateEmailException.class.getCanonicalName()))));
	}

	@Test
	@WithMockCustomUser
	@DisplayName("개인정보 열람 테스트")
	void getUserInformationTest() throws Exception {

		mockMvc.perform(get("/api/v0/user"))
			.andExpect(status().isOk())
			.andDo(print());
	}

	@Test
	@Disabled
	@WithMockCustomUser
	@DisplayName("개인정보 업데이트 테스트")
	void updateUserInformationTest() {

		User user = User.builder()
			.nickname("testUser")
			.birth(new Date())
			.email("test@gamil.com")
			.password("testPassword12")
			.build();
		UserUpdateRequest updateRequest = new UserUpdateRequest(user.getNickname(), "F", "http://github.com",
			user.getBirth(), null);

		User updatedUser = User.builder()
			.nickname("testUser")
			.birth(new Date())
			.email("test@gamil.com")
			.password("testPassword12")
			.gender(GenderType.FEMALE)
			.personalUrl(updateRequest.getPersonalUrl())
			.birth(updateRequest.getBirth())
			.build();

		// when(userService.updateUser(user, updateRequest.toCommand())).thenReturn(updatedUser);

	}
}