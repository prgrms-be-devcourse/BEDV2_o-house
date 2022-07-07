package com.prgrms.ohouse.web.community.question.api;

import static com.prgrms.ohouse.infrastructure.TestDataProvider.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.MediaType.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.RequestPartFieldsSnippet;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.community.model.question.QuestionComment;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.web.community.question.requests.QuestionCommentCreateRequest;
import com.prgrms.ohouse.web.community.question.requests.QuestionCommentUpdateRequest;

@SpringBootTest
@AutoConfigureRestDocs
@AutoConfigureMockMvc
@Transactional
class RestQuestionCommentControllerV0Test {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TestDataProvider testDataProvider;
	@MockBean
	UserAuditorAware auditor;

	static RequestPartFieldsSnippet createRequestFieldSnippet =
		requestPartFields("request",
			fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용"));

	static RequestPartFieldsSnippet updateRequestFieldSnippet =
		requestPartFields("request",
			fieldWithPath("content").type(JsonFieldType.STRING).description("수정할 댓글 내용"));

	@Test
	@DisplayName("질문 게시글에 대한 댓글 생성 요청 후 성공")
	void registerNewQuestionComment() throws Exception {
		User user = testDataProvider.insertGuestUser("guest");
		QuestionPost savedPost =
			testDataProvider.insertQuestionPostWithUser(auditor, user, "처음 설정한 제목", "처음 설정한 내용");

		when(auditor.getCurrentAuditor()).thenReturn(Optional.of(user));

		byte[] request = objectMapper.writeValueAsBytes(new QuestionCommentCreateRequest("댓글 내용"));

		//Request
		ResultActions result = mockMvc.perform(multipart("/api/v0/questions/" + savedPost.getId() + "/comments")
			.file(new MockMultipartFile("request", null, APPLICATION_JSON_VALUE, request))
			.file(new MockMultipartFile("file", "fav1.png", IMAGE_PNG_VALUE,
				"someImage".getBytes(StandardCharsets.UTF_8)))
			.header(AUTHORIZATION, GUEST_TOKEN));

		//ResultAssertions
		ResultActions resultAssertions = result.andExpect(status().isCreated())
			.andExpect(redirectedUrlPattern("/api/v0/questions/" + savedPost.getId() + "/*"))
			.andDo(print());

		//RestDocs
		resultAssertions.andDo(
			document("registerNewQuestionComment", createRequestFieldSnippet)
		);
	}

	@Test
	@DisplayName("질문 게시글에 대한 댓글 수정 요청 후 성공")
	void editQuestionComment() throws Exception {
		User user = testDataProvider.insertGuestUser("guest");
		QuestionPost savedPost =
			testDataProvider.insertQuestionPostWithUser(auditor, user, "처음 설정한 제목", "처음 설정한 내용");
		QuestionComment savedComment =
			testDataProvider.insertQuestionCommentWithUser(auditor, user, savedPost, "처음 설정한 댓글 내용");

		when(auditor.getCurrentAuditor()).thenReturn(Optional.of(user));

		byte[] request = objectMapper.writeValueAsBytes(new QuestionCommentUpdateRequest("수정된 댓글 내용"));

		//Request
		ResultActions result = mockMvc.perform(
			multipart("/api/v0/questions/" + savedPost.getId() + "/" + savedComment.getId())
				.file(new MockMultipartFile("request", null, APPLICATION_JSON_VALUE, request))
				.file(new MockMultipartFile("file", "fav1.png", IMAGE_PNG_VALUE,
					"someImage".getBytes(StandardCharsets.UTF_8)))
				.header(AUTHORIZATION, GUEST_TOKEN));

		//ResultAssertions
		ResultActions resultAssertions = result.andExpect(status().isOk())
			.andDo(print());

		//RestDocs
		resultAssertions.andDo(
			document("editQuestionComment", updateRequestFieldSnippet)
		);
	}

	@Test
	@DisplayName("질문 게시글에 대한 댓글 삭제 요청 후 성공")
	void deleteQuestionComment() throws Exception {
		User user = testDataProvider.insertGuestUser("guest");
		QuestionPost savedPost =
			testDataProvider.insertQuestionPostWithUser(auditor, user, "처음 설정한 제목", "처음 설정한 내용");
		QuestionComment savedComment =
			testDataProvider.insertQuestionCommentWithUser(auditor, user, savedPost, "처음 설정한 댓글 내용");

		when(auditor.getCurrentAuditor()).thenReturn(Optional.of(user));

		//Request
		ResultActions result = mockMvc.perform(
			delete("/api/v0/questions/" + savedPost.getId() + "/" + savedComment.getId())
				.header(AUTHORIZATION, GUEST_TOKEN));

		//ResultAssertions
		ResultActions resultAssertions = result.andExpect(status().isOk())
			.andDo(print());
	}
}