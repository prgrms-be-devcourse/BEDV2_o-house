package com.prgrms.ohouse.web.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.user.application.UserService;
import com.prgrms.ohouse.domain.user.application.commands.UserCreateCommand;
import com.prgrms.ohouse.web.user.requests.UserLoginRequest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class JwtAuthenticationFilterTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("token 발급 테스트")
	void createTokenTest() throws Exception {
		UserCreateCommand createCommand = new UserCreateCommand("guest", "guest@gmail.com", "guestPw12");
		userService.signUp(createCommand);

		String body = objectMapper.writeValueAsString(
			new UserLoginRequest(createCommand.getEmail(), createCommand.getPassword()));
		mockMvc.perform(post("/api/v0/login")
				.content(body)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(header().exists("Authorization"))
			.andDo(print());
	}

	@Test
	@DisplayName("잘못된 token 입력 -> AccessDeniedHandler 처리 테스트")
	void falseTokenInputTest() throws Exception{

		mockMvc.perform(get("/api/v0/user")
				.header("Authorization", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isForbidden())
			.andDo(print());
	}
}