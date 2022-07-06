package com.prgrms.ohouse.web.user.api;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.operation.preprocess.Preprocessors;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.model.Gender;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.web.ApiDocumentUtils;
import com.prgrms.ohouse.web.user.requests.UserCreateRequest;
import com.prgrms.ohouse.web.user.requests.UserUpdateRequest;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@AutoConfigureMockMvc
class UserRestControllerTest {

	@MockBean
	private UserService userService;
	@MockBean
	private AuthUtility authUtility;

	@Autowired
	private UserRestController userRestController;

	private MockMvc mockMvc;
	private ObjectMapper objectMapper = new ObjectMapper();
	Long authUserId = 10L;

	User user = spy(
		User.builder()
			.nickname("guest")
			.email("guest@gmail.com")
			.password("guestPassword12")
			.gender(Gender.FEMALE)
			.build()
	);

	UserCreateRequest request = new UserCreateRequest("testUser", "test@gmail.com", "guestPw12");

	@BeforeEach
	void setup(RestDocumentationContextProvider restDocumentation) {
		MockitoAnnotations.initMocks(this);
		objectMapper.registerModule(new JavaTimeModule());
		mockMvc = MockMvcBuilders.standaloneSetup(userRestController)
			.apply(documentationConfiguration(restDocumentation))
			.setControllerAdvice(UserApiExceptionHandler.class)
			.alwaysDo(print())
			.build();

		when(authUtility.getAuthUser()).thenReturn(user);
		when(user.getId()).thenReturn(Long.valueOf(authUserId));
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
			.andExpect(content().string(containsString("User Create Success")))

			.andDo(document("signup-user",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				requestFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("가입할 유저의 닉네임"),
					fieldWithPath("email").type(JsonFieldType.STRING).description("가입할 유저의 이메일"),
					fieldWithPath("password").type(JsonFieldType.STRING).description("가입할 유저의 비밀번호")
				),
				responseBody()
			));
	}

	@Test
	@DisplayName("개인정보 열람 테스트")
	void getUserInformationTest() throws Exception {

		mockMvc.perform(get("/api/v0/user"))
			.andExpect(status().isOk())

			.andDo(document("get-user-info",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				requestBody(),
				responseFields(
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
					fieldWithPath("gender").type(JsonFieldType.STRING).description("유저 성별").optional(),
					fieldWithPath("personalUrl").type(JsonFieldType.STRING).description("유저의 개인 URL").optional(),
					fieldWithPath("birth").type(JsonFieldType.STRING).description("유저 생년월일").optional(),
					fieldWithPath("image").type(JsonFieldType.OBJECT).description("유저 이미지").optional(),
					fieldWithPath("introductions").type(JsonFieldType.STRING).description("유저 소개글").optional()
				)
			));
	}

	@Test
	@DisplayName("개인정보 업데이트 테스트(이미지 x)")
	void updateUserInformationTest() throws Exception {

		UserUpdateRequest updateRequest = new UserUpdateRequest("guest", "FEMALE", "http://github.com",
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
			.gender(Gender.FEMALE)
			.personalUrl(updateRequest.getPersonalUrl())
			.build();

		when(userService.updateUser(authUserId, updateRequest.toCommand(null))).thenReturn(updatedUser);

		mockMvc.perform(multipart("/api/v0/user")
				.file(jsonRequest))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Update succeed.")));
	}

	@Test
	@DisplayName("개인정보 업데이트 테스트(이미지 o)")
	void updateUserInformationWithImageTest() throws Exception {

		UserUpdateRequest updateRequest = new UserUpdateRequest("guest", "FEMALE", "http://github.com",
			null, "--");
		String body = objectMapper.writeValueAsString(updateRequest);
		MockMultipartFile fileRequest = new MockMultipartFile(
			"image", "test.png", "image/png", "<<png data>>".getBytes()
		);

		MockPart jsonPart = new MockPart("request", body.getBytes(StandardCharsets.UTF_8));
		jsonPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);

		User updatedUser = User.builder()
			.nickname(updateRequest.getNickname())
			.birth(updateRequest.getBirth())
			.email("guest@gmail.com")
			.password("guestPassword12")
			.gender(Gender.FEMALE)
			.personalUrl(updateRequest.getPersonalUrl())
			.build();

		when(userService.updateUser(authUserId, updateRequest.toCommand(fileRequest)))
			.thenReturn(updatedUser);

		mockMvc.perform(multipart("/api/v0/user")
				.file(fileRequest)
			.part(jsonPart))
			.andExpect(status().isOk())
			.andExpect(content().string(containsString("Update succeed.")))

			.andDo(document("update-user",
				Preprocessors.preprocessRequest(Preprocessors.prettyPrint()),
				Preprocessors.preprocessResponse(Preprocessors.prettyPrint()),
				requestParts(
					partWithName("request").description("회원정보 수정"),
					partWithName("image").description("수정할 회원 이미지").optional()
				),
				requestPartFields(
					"request",
					fieldWithPath("nickname").type(JsonFieldType.STRING).description("유저 닉네임"),
					fieldWithPath("gender").type(JsonFieldType.STRING).description("유저 성별").optional(),
					fieldWithPath("personalUrl").type(JsonFieldType.STRING).description("유저 개인 페이지").optional(),
					fieldWithPath("birth").description("유저 생년월일").optional(),
					fieldWithPath("introductions").type(JsonFieldType.STRING).description("유저 자기소개글").optional()
				),
				responseBody()
			));
	}
}