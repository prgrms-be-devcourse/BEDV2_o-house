package com.prgrms.ohouse.web.community.api;

import static com.prgrms.ohouse.infrastructure.TestDataProvider.*;
import static org.assertj.core.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.community.application.HousewarmingPostInfoResult;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostComment;
import com.prgrms.ohouse.domain.community.model.housewarming.HousewarmingPostRepository;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.web.ApiDocumentUtils;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@AutoConfigureRestDocs
class RestHousewarmingPostControllerTest {

	private static final ObjectMapper json = new ObjectMapper();

	private static final String HW_URL = "/api/v0/hwpost";
	@Autowired
	MockMvc mockMvc;

	@Autowired
	HousewarmingPostRepository postRepository;

	@Autowired
	TestDataProvider fixtureProvider;

	@MockBean
	UserAuditorAware auditorAware;

	@Value("${app.host}")
	private String host;

	@Test
	@DisplayName("집들이 게시물 생성 요청을 애플리케이션에 전달한 뒤 생성된 게시물의 URI를 응답한다.")
	void HousewarmingCrateRequest() throws Exception {
		var payload = json.writeValueAsBytes(Map.of(
			"title", "baka",
			"content", "content{{image}}content1{{image}}",
			"housingTypeCode", "PRIVATE_ROOM",
			"area", 2,
			"constructionFee", 200,
			"stylingFee", 200,
			"familyType", "SINGLE",
			"workerType", "SELF"
		));
		// Given
		var payloadPart = new MockPart(
			"payload",
			payload);
		payloadPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		var images =
			List.of(
				new MockMultipartFile("image", "fav.png", "image/png", "chunk1".getBytes()),
				new MockMultipartFile("image", "fav2.png", "image/png", "chunk2".getBytes())
			);
		fixtureProvider.insertGuestUser("guest");
		MockMultipartHttpServletRequestBuilder multipartRequest = (MockMultipartHttpServletRequestBuilder)multipart(
			HW_URL).header("Authorization", GUEST_TOKEN);
		images.forEach(multipartRequest::file);
		multipartRequest.part(payloadPart);
		// When
		var result = mockMvc.perform(multipartRequest);

		// Then
		result
			.andDo(print())
			.andExpectAll(
				status().isCreated(),
				header().string("Location", Matchers.matchesRegex(host + "api/v0/hwpost/\\d+$"))
			)
			.andDo(document("hwpost-create",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				requestParts(
					partWithName("image").description("집들이 컨텐츠 이미지"),
					partWithName("payload").description("생성 집들이 정보")
				),
				requestPartFields("payload",
					fieldWithPath("title").type(JsonFieldType.STRING).description("집들이 제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("집들이 내용"),
					fieldWithPath("housingTypeCode").type(JsonFieldType.STRING).description("주거 형태"),
					fieldWithPath("housingDescription").type(JsonFieldType.STRING)
						.description("주거 형태 상세 정보")
						.optional(),
					fieldWithPath("familyType").type(JsonFieldType.STRING).description("가족 형태"),
					fieldWithPath("workerType").type(JsonFieldType.STRING).description("작업자"),
					fieldWithPath("stylingFee").type(JsonFieldType.NUMBER).description("홈스타일링 예산"),
					fieldWithPath("constructionFee").type(JsonFieldType.NUMBER).description("공사/시공 예산"),
					fieldWithPath("area").type(JsonFieldType.NUMBER).description("평수"),
					fieldWithPath("familyDescription").type(JsonFieldType.STRING).description("가족 상세 정보").optional(),
					fieldWithPath("familyCount").type(JsonFieldType.NUMBER).description("가족 수").optional(),
					fieldWithPath("company").type(JsonFieldType.STRING).description("시공/스타일링 업체 정보").optional(),
					fieldWithPath("workDuration").type(JsonFieldType.NUMBER).description("공사 기간").optional(),
					fieldWithPath("workUnit").type(JsonFieldType.STRING).description("공사 기간 단위").optional(),
					fieldWithPath("workTarget").type(JsonFieldType.STRING).description("시공 대상").optional(),
					fieldWithPath("workerDescription").type(JsonFieldType.STRING).description("시공업체 상세 정보").optional(),
					fieldWithPath("copyrightHolder").type(JsonFieldType.STRING).description("저작권 표기").optional(),
					fieldWithPath("linkPayloads").type(JsonFieldType.ARRAY).description("연관 링크").optional(),
					fieldWithPath("districtCode").type(JsonFieldType.STRING).description("지역 코드").optional(),
					fieldWithPath("districtDescription").type(JsonFieldType.STRING).description("지역 상세 정보").optional()
				)
			))
		;
	}

	@Test
	@DisplayName("인증 받은 사용자의 삭제 요청을 받으면 삭제 성공 응답을 보내야 한다.")
	void delete_hwpost_with_proper_authorization() throws Exception {

		// Given
		var post = fixtureProvider.insertHousewarmingPostWithAuthor(auditorAware,
			fixtureProvider.insertGuestUser("guest"), 1);
		var postId = post.getId();
		// When
		var result = mockMvc.perform(delete(HW_URL + "/" + postId).header("Authorization", GUEST_TOKEN));

		// Then
		assertThat(postRepository.findById(postId)).isEmpty();
		result.andExpectAll(
				status().is2xxSuccessful()
			).andDo(print())
			.andDo(document("hwpost-delete-success",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse()
			))
		;
	}

	@Test
	@DisplayName("컨텐츠에 대한 권한이 없는 사용자의 삭제 요청을 받으면 권한 없음 응답을 보내야 한다.")
	void deleting_hwpost_fails_with_wrong_authorization_failure_error() throws Exception {

		// Given
		fixtureProvider.insertGuestUser("guest");
		var savedPost = fixtureProvider
			.insertHousewarmingPostWithAuthor(auditorAware, fixtureProvider.insertGuestUser("guest2"), 2);
		var postId = savedPost.getId();
		// When
		var result = mockMvc.perform(delete(HW_URL + "/" + postId).header("Authorization", GUEST_TOKEN));

		// Then
		assertThat(postRepository.findById(postId)).isNotEmpty();
		result.andExpectAll(
				status().isUnauthorized()
			).andDo(print())
			.andDo(document("hwpost-delete-failure",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse()
			));
	}

	@Test
	@DisplayName("등록된 집들이 게시물의 정보를 조회할 수 있다.")
	void acquire_information_of_requested_housewarming_post() throws Exception {

		// Given
		var user = fixtureProvider.insertGuestUser("guest");
		var savedPost = fixtureProvider.insertHousewarmingPostWithAuthor(auditorAware, user, 2);

		// When
		var result = mockMvc.perform(get(HW_URL + "/" + savedPost.getId()));

		// Then
		var expectedPayload = HousewarmingPostInfoResult.from(savedPost);
		expectedPayload.incrementViewCount();
		result.andExpectAll(
				status().is2xxSuccessful(),
				content().json(json.writeValueAsString(expectedPayload))
			).andDo(print())
			.andDo(document("hwpost-singleQuery",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				responseFields(
					fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("작성자 ID"),
					fieldWithPath("userNickname").type(JsonFieldType.STRING).description("작성자 이름"),
					fieldWithPath("title").type(JsonFieldType.STRING).description("집들이 제목"),
					fieldWithPath("content").type(JsonFieldType.STRING).description("집들이 내용"),
					fieldWithPath("housingType").type(JsonFieldType.STRING).description("주거 형태"),
					fieldWithPath("housingDescription").type(JsonFieldType.STRING)
						.description("주거 형태 상세 정보")
						.optional(),
					fieldWithPath("visitCount").type(JsonFieldType.NUMBER).description("조회수"),
					fieldWithPath("scrapCount").type(JsonFieldType.NUMBER).description("스크랩 회수"),
					fieldWithPath("family.type").type(JsonFieldType.STRING).description("가족 형태"),
					fieldWithPath("family.description").type(JsonFieldType.STRING).description("가족 상세 정보").optional(),
					fieldWithPath("family.familyCount").type(JsonFieldType.NUMBER).description("가족 수").optional(),
					fieldWithPath("budget.stylingFee").type(JsonFieldType.NUMBER).description("홈스타일링 예산"),
					fieldWithPath("budget.constructionFee").type(JsonFieldType.NUMBER).description("공사/시공 예산"),
					fieldWithPath("budget.total").type(JsonFieldType.NUMBER).description("총예산"),
					fieldWithPath("area").type(JsonFieldType.NUMBER).description("평수"),
					fieldWithPath("company").type(JsonFieldType.STRING).description("시공/스타일링 업체 정보").optional(),
					fieldWithPath("workMetadata.workerType").type(JsonFieldType.STRING).description("작업자"),
					fieldWithPath("workMetadata.duration").type(JsonFieldType.NUMBER).description("공사 기간").optional(),
					fieldWithPath("workMetadata.unit").type(JsonFieldType.STRING).description("공사 기간 단위").optional(),
					fieldWithPath("workMetadata.workTarget").type(JsonFieldType.STRING).description("시공 대상").optional(),
					fieldWithPath("workMetadata.workerDescription").type(JsonFieldType.STRING)
						.description("시공업체 상세 정보")
						.optional(),
					fieldWithPath("copyrightHolder").type(JsonFieldType.STRING).description("저작권 표기").optional(),
					fieldWithPath("linkPayloads").type(JsonFieldType.ARRAY).description("연관 링크").optional(),
					fieldWithPath("districtCode").type(JsonFieldType.STRING).description("지역 코드").optional(),
					fieldWithPath("districtDescription").type(JsonFieldType.STRING).description("지역 상세 정보").optional(),
					fieldWithPath("images").type(JsonFieldType.ARRAY).description("포함 이미지 상세 정보").optional()
				)
			));
	}

	@Test
	@DisplayName("원하는 개수 만큼의 집들이 컨텐츠 목록과 목록에 대한 메타 데이터를 반환한다.")
	void return_posts_with_correct_metadata() throws Exception {
		int page = 0;
		int queriedSize = 19;
		int storedSize = 20;

		// Given
		var author = fixtureProvider.insertGuestUser("guest");
		for (int i = 1; i <= storedSize; i++) {
			fixtureProvider.insertHousewarmingPostWithAuthor(auditorAware, author, i);
		}
		// When
		var result = mockMvc.perform(
			get(HW_URL).param("page", String.valueOf(page))
				.param("size", String.valueOf(queriedSize)));

		// Then
		result
			.andDo(print())
			.andExpectAll(
				jsonPath("size").value(queriedSize),
				jsonPath("hasNext").value(true),
				jsonPath("contents").exists()
			).andDo(document("hwpost-singleQuery",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				// responseFields(
				// 	fieldWithPath("size").type(JsonFieldType.NUMBER).description("집들이 목록 크기"),
				// 	fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 집들이 목록 존재 여부"),
				// 	fieldWithPath("lastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 확인")
				// ),

				responseFields(applyPathPrefix("contents[]", List.of(
							fieldWithPath("postId").type(JsonFieldType.NUMBER).description("게시물 ID"),
							fieldWithPath("userId").type(JsonFieldType.NUMBER).description("작성자 ID"),
							fieldWithPath("userNickname").type(JsonFieldType.STRING).description("작성자 이름"),
							fieldWithPath("title").type(JsonFieldType.STRING).description("집들이 제목"),
							fieldWithPath("content").type(JsonFieldType.STRING).description("집들이 내용"),
							fieldWithPath("housingType").type(JsonFieldType.STRING).description("주거 형태"),
							fieldWithPath("housingDescription").type(JsonFieldType.STRING)
								.description("주거 형태 상세 정보")
								.optional(),
							fieldWithPath("visitCount").type(JsonFieldType.NUMBER).description("조회수"),
							fieldWithPath("scrapCount").type(JsonFieldType.NUMBER).description("스크랩 회수"),
							fieldWithPath("family.type").type(JsonFieldType.STRING).description("가족 형태"),
							fieldWithPath("family.description").type(JsonFieldType.STRING).description("가족 상세 정보").optional(),
							fieldWithPath("family.familyCount").type(JsonFieldType.NUMBER).description("가족 수").optional(),
							fieldWithPath("budget.stylingFee").type(JsonFieldType.NUMBER).description("홈스타일링 예산"),
							fieldWithPath("budget.constructionFee").type(JsonFieldType.NUMBER).description("공사/시공 예산"),
							fieldWithPath("budget.total").type(JsonFieldType.NUMBER).description("총예산"),
							fieldWithPath("area").type(JsonFieldType.NUMBER).description("평수"),
							fieldWithPath("company").type(JsonFieldType.STRING).description("시공/스타일링 업체 정보").optional(),
							fieldWithPath("workMetadata.workerType").type(JsonFieldType.STRING).description("작업자"),
							fieldWithPath("workMetadata.duration").type(JsonFieldType.NUMBER).description("공사 기간").optional(),
							fieldWithPath("workMetadata.unit").type(JsonFieldType.STRING).description("공사 기간 단위").optional(),
							fieldWithPath("workMetadata.workTarget").type(JsonFieldType.STRING).description("시공 대상").optional(),
							fieldWithPath("workMetadata.workerDescription").type(JsonFieldType.STRING)
								.description("시공업체 상세 정보")
								.optional(),
							fieldWithPath("copyrightHolder").type(JsonFieldType.STRING).description("저작권 표기").optional(),
							fieldWithPath("linkPayloads").type(JsonFieldType.ARRAY).description("연관 링크").optional(),
							fieldWithPath("districtCode").type(JsonFieldType.STRING).description("지역 코드").optional(),
							fieldWithPath("districtDescription").type(JsonFieldType.STRING).description("지역 상세 정보").optional(),
							fieldWithPath("images").type(JsonFieldType.ARRAY).description("포함 이미지 상세 정보").optional()
						)
					)
				).and(
					fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
					fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("페이지 이후 게시물 존재 여부"),
					fieldWithPath("lastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부"))
			));

	}

	@Test
	@DisplayName("사용자는 자신이 작성한 집들이 게시글을 수정한다.")
	void authorized_user_update_its_post() throws Exception {

		// Given
		var author = fixtureProvider.insertGuestUser("guest");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(auditorAware, author, 1);
		var payload = json.writeValueAsBytes(Map.of(
			"title", "updated",
			"content", "updated content{{image}}content1{{image}}content2{{image}}",
			"housingTypeCode", "PRIVATE_ROOM",
			"area", 2,
			"constructionFee", 200,
			"stylingFee", 200,
			"familyType", "SINGLE",
			"workerType", "SELF"
		));
		var payloadPart = new MockPart(
			"payload",
			payload);
		payloadPart.getHeaders().setContentType(MediaType.APPLICATION_JSON);
		var images =
			List.of(
				new MockMultipartFile("image", "fav.png", "image/png", "chunk1".getBytes()),
				new MockMultipartFile("image", "fav2.png", "image/png", "chunk2".getBytes()),
				new MockMultipartFile("image", "fav3.png", "image/png", "chunk3".getBytes())
			);
		MockMultipartHttpServletRequestBuilder multipartRequest = (MockMultipartHttpServletRequestBuilder)multipart(
			HW_URL + "/" + targetPost.getId()).header("Authorization", GUEST_TOKEN);
		images.forEach(multipartRequest::file);
		multipartRequest.part(payloadPart);

		// When
		var result = mockMvc.perform(multipartRequest);

		// Then
		result.andExpectAll(
			status().is2xxSuccessful(),
			header().string("Location", host + "api/v0/hwpost" + "/" + targetPost.getId())
		).andDo(document("hwpost-update",
			ApiDocumentUtils.getDocumentRequest(),
			ApiDocumentUtils.getDocumentResponse(),
			requestParts(
				partWithName("image").description("집들이 컨텐츠 이미지"),
				partWithName("payload").description("수정 집들이 정보")
			),
			requestPartFields("payload",
				fieldWithPath("title").type(JsonFieldType.STRING).description("집들이 제목"),
				fieldWithPath("content").type(JsonFieldType.STRING).description("집들이 내용"),
				fieldWithPath("housingTypeCode").type(JsonFieldType.STRING).description("주거 형태"),
				fieldWithPath("housingDescription").type(JsonFieldType.STRING)
					.description("주거 형태 상세 정보")
					.optional(),
				fieldWithPath("familyType").type(JsonFieldType.STRING).description("가족 형태"),
				fieldWithPath("workerType").type(JsonFieldType.STRING).description("작업자"),
				fieldWithPath("stylingFee").type(JsonFieldType.NUMBER).description("홈스타일링 예산"),
				fieldWithPath("constructionFee").type(JsonFieldType.NUMBER).description("공사/시공 예산"),
				fieldWithPath("area").type(JsonFieldType.NUMBER).description("평수"),
				fieldWithPath("familyDescription").type(JsonFieldType.STRING).description("가족 상세 정보").optional(),
				fieldWithPath("familyCount").type(JsonFieldType.NUMBER).description("가족 수").optional(),
				fieldWithPath("company").type(JsonFieldType.STRING).description("시공/스타일링 업체 정보").optional(),
				fieldWithPath("workDuration").type(JsonFieldType.NUMBER).description("공사 기간").optional(),
				fieldWithPath("workUnit").type(JsonFieldType.STRING).description("공사 기간 단위").optional(),
				fieldWithPath("workTarget").type(JsonFieldType.STRING).description("시공 대상").optional(),
				fieldWithPath("workerDescription").type(JsonFieldType.STRING).description("시공업체 상세 정보").optional(),
				fieldWithPath("copyrightHolder").type(JsonFieldType.STRING).description("저작권 표기").optional(),
				fieldWithPath("linkPayloads").type(JsonFieldType.ARRAY).description("연관 링크").optional(),
				fieldWithPath("districtCode").type(JsonFieldType.STRING).description("지역 코드").optional(),
				fieldWithPath("districtDescription").type(JsonFieldType.STRING).description("지역 상세 정보").optional()

			)
		));
		assertThat(targetPost.getTitle()).isEqualTo("updated");
		assertThat(targetPost.getImages()).hasSize(3);

	}

	@Test
	@DisplayName("집들이 게시물에 대한 댓글을 작성한다.")
	void add_comment_to_housewarming_post() throws Exception {

		// Given
		var postAuthor = fixtureProvider.insertGuestUser("postAuthor");
		fixtureProvider.insertGuestUser("guest");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(auditorAware, postAuthor, 1);
		var payload = json.writeValueAsString(Map.of("postId", targetPost.getId(), "comment", "댓글1"));

		// When
		var result = mockMvc.perform(
			post(HW_URL + "/comment")
				.content(payload)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", GUEST_TOKEN)
		);

		// Then
		result
			.andExpectAll(
				status().isCreated(),
				header().string("Location", Matchers.matchesRegex(host + "api/v0/hwpost/comment/\\d+$"))

			)
			.andDo(document("hwpost-comment-create",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				requestFields(
					fieldWithPath("postId").type(JsonFieldType.NUMBER).description("집들이 ID"),
					fieldWithPath("comment").type(JsonFieldType.STRING).description("댓글 내용")
				)
			));
	}

	@Test
	@DisplayName("자신이 작성한 집들이 댓글을 수정한다.")
	void user_update_its_comment() throws Exception {

		// Given
		var postAuthor = fixtureProvider.insertGuestUser("postAuthor");
		var commentAuthor = fixtureProvider.insertGuestUser("guest");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(auditorAware, postAuthor, 1);
		HousewarmingPostComment targetComment = fixtureProvider.insertHousewarmingPostCommentWithAuthor(
			auditorAware,
			commentAuthor,
			targetPost,
			1);
		var payload = json.writeValueAsString(Map.of("commentId", targetComment.getId(), "comment", "updated"));

		// When
		var result = mockMvc.perform(
			put(HW_URL + "/comment")
				.content(payload)
				.contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", GUEST_TOKEN)
		);

		// Then
		result.andExpectAll(
				status().isOk()
			)
			.andDo(document("hwpost-comment-update",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				requestFields(
					fieldWithPath("commentId").type(JsonFieldType.NUMBER).description("수정 대상 집들이 댓글 ID"),
					fieldWithPath("comment").type(JsonFieldType.STRING).description("수정 댓글 내용")
				)
			));

	}

	@Test
	@DisplayName("자신이 작성한 집들이 댓글을 삭제한다.")
	void user_remove_its_comment() throws Exception {

		// Given
		var postAuthor = fixtureProvider.insertGuestUser("postAuthor");
		var commentAuthor = fixtureProvider.insertGuestUser("guest");
		var targetPost = fixtureProvider.insertHousewarmingPostWithAuthor(auditorAware, postAuthor, 1);
		HousewarmingPostComment targetComment = fixtureProvider.insertHousewarmingPostCommentWithAuthor(
			auditorAware,
			commentAuthor,
			targetPost,
			1);

		// When
		var result = mockMvc.perform(
			RestDocumentationRequestBuilders.delete(HW_URL + "/comment/{commentId}", targetComment.getId())
				.header("Authorization", GUEST_TOKEN)
		);

		// Then
		result.andExpectAll(
				status().isOk()
			)
			.andDo(document("hwpost-comment-delete",
				ApiDocumentUtils.getDocumentRequest(),
				ApiDocumentUtils.getDocumentResponse(),
				pathParameters(
					parameterWithName("commentId").description("삭제 대상 집들이 댓글 ID")
				)
			));
	}

}
