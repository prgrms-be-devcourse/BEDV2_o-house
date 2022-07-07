package com.prgrms.ohouse.web.community.api;

import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.community.application.FollowingFeedInfoResult;
import com.prgrms.ohouse.domain.community.application.FollowingFeedService;
import com.prgrms.ohouse.domain.community.application.PostCategory;
import com.prgrms.ohouse.domain.user.model.User;

@ExtendWith(RestDocumentationExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Transactional
@AutoConfigureMockMvc
class RestFollowingFeedControllerTest {

	@MockBean
	AuthUtility authUtility;

	@MockBean
	FollowingFeedService followingFeedService;

	@Autowired
	RestFollowingFeedController followingFeedController;

	MockMvc mockMvc;
	private Long authUserId = 1L;
	User user = spy(
		User.builder()
			.nickname("guest")
			.email("guest@gmail.com")
			.password("guestPassword12").build()
	);

	@BeforeEach
	void setup(RestDocumentationContextProvider restDocumentation) {
		mockMvc = MockMvcBuilders.standaloneSetup(followingFeedController)
			.setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
			.apply(documentationConfiguration(restDocumentation))
			.alwaysDo(print())
			.build();

	}

	@Test
	void getFollowingFeedTest() throws Exception {

		//given
		PageRequest pageRequest = PageRequest.of(0, 10);
		var resultDtos = List.of(
			new FollowingFeedInfoResult(1L, PostCategory.HousewarmingPost, 10L, "following1", "number1Post",
				"number1Content{}{}", 20, null),

			new FollowingFeedInfoResult(10L, PostCategory.HousewarmingPost, 10L, "following1", "number2Post",
				"postspotstjdfjsdj???", 1000, null)
		);
		var result = new SliceImpl<>(
			resultDtos,
			pageRequest,
			false
		);

		when(authUtility.getAuthUser()).thenReturn(user);
		when(user.getId()).thenReturn(authUserId);

		when(followingFeedService.getFeedPosts(authUserId, pageRequest))
			.thenReturn(result);

		mockMvc.perform(get("/api/v0/feed")
				.param("page", "0")
				.param("size", "10")
			)
			.andExpectAll(
				status().isOk()
			)
			.andDo(document("following-feed",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("page").description("조회할 피드 페이지 번호"),
					parameterWithName("size").description("한 페이지당 조회할 게시글 개수")
				),
				responseFields(
					fieldWithPath("contents[].postId").type(JsonFieldType.NUMBER).description("게시글 ID"),
					fieldWithPath("contents[].category").type(JsonFieldType.STRING).description("게시글 카테고리"),
					fieldWithPath("contents[].authorId").type(JsonFieldType.NUMBER).description("게시글 작성자 ID"),
					fieldWithPath("contents[].authorNickname").type(JsonFieldType.STRING).description("게시글 작성자 닉네임"),
					fieldWithPath("contents[].title").type(JsonFieldType.STRING).description("게시글 제목"),
					fieldWithPath("contents[].content").type(JsonFieldType.STRING).description("게시글 내용"),
					fieldWithPath("contents[].visitCount").type(JsonFieldType.NUMBER).description("게시글 조회수"),
					fieldWithPath("contents[].images").type(JsonFieldType.ARRAY).description("게시글에 첨부된 이미지").optional(),

					fieldWithPath("size").type(JsonFieldType.NUMBER).description("페이지 사이즈"),
					fieldWithPath("hasNext").type(JsonFieldType.BOOLEAN).description("다음 페이지의 존재 여부"),
					fieldWithPath("lastPage").type(JsonFieldType.BOOLEAN).description("마지막 페이지 여부")
				)
			));
	}

}