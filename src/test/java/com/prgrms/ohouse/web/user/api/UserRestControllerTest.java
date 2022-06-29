package com.prgrms.ohouse.web.user.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prgrms.ohouse.config.WithMockCustomUser;
import com.prgrms.ohouse.domain.common.security.AuthUtils;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.model.GenderType;
import com.prgrms.ohouse.domain.user.model.User;
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
		objectMapper.registerModule(new JavaTimeModule());
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

		mockMvc.perform(post("/api/v0/signup")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("User Create Success")));
	}

	@Test
	@WithMockCustomUser
	@DisplayName("개인정보 열람 테스트")
	void getUserInformationTest() throws Exception {

		mockMvc.perform(get("/api/v0/user"))
			.andExpect(status().isOk());
	}

	@Test
	@WithMockCustomUser
	@DisplayName("개인정보 업데이트 테스트(이미지 x)")
	void updateUserInformationTest() throws Exception {

		UserUpdateRequest updateRequest = new UserUpdateRequest("guest", "F", "http://github.com",
			LocalDate.now(), null);
		String body = objectMapper.writeValueAsString(updateRequest);
		MockMultipartFile jsonRequest = new MockMultipartFile("request", "updateRequest", "application/json",
			body.getBytes(
				StandardCharsets.UTF_8));

		User updatedUser = User.builder()
			.nickname(updateRequest.getNickname())
			.birth(updateRequest.getBirth())
			.email("guest@gmail.com")
			.password("guestPassword12")
			.gender(GenderType.FEMALE)
			.personalUrl(updateRequest.getPersonalUrl())
			.build();

		when(userService.updateUser(AuthUtils.getAuthUser(), updateRequest.toCommand(null))).thenReturn(updatedUser);

		mockMvc.perform(multipart("/api/v0/user")
				.file(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Update succeed.")));
	}

	@Test
	@WithMockCustomUser
	@DisplayName("개인정보 업데이트 테스트(이미지 o)")
	void updateUserInformationWithImageTest() throws Exception {

		UserUpdateRequest updateRequest = new UserUpdateRequest("guest", "F", "http://github.com",
			LocalDate.now(), null);
		String body = objectMapper.writeValueAsString(updateRequest);
		MockMultipartFile jsonRequest = new MockMultipartFile(
			"request", "updateRequest", "application/json", body.getBytes(StandardCharsets.UTF_8));
		MockMultipartFile fileRequest = new MockMultipartFile(
			"image", "test.png", "image/png", "<<png data>>".getBytes()
		);

		User updatedUser = User.builder()
			.nickname(updateRequest.getNickname())
			.birth(updateRequest.getBirth())
			.email("guest@gmail.com")
			.password("guestPassword12")
			.gender(GenderType.FEMALE)
			.personalUrl(updateRequest.getPersonalUrl())
			.build();

		when(userService.updateUser(AuthUtils.getAuthUser(), updateRequest.toCommand(fileRequest)))
			.thenReturn(updatedUser);

		mockMvc.perform(multipart("/api/v0/user")
				.file(jsonRequest)
				.file(fileRequest))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Update succeed.")));
	}
}