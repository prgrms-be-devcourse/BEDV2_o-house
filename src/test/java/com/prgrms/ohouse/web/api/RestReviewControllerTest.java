package com.prgrms.ohouse.web.api;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.prgrms.ohouse.domain.commerce.application.ReviewService;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.review.PageInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedPhotoReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.web.commerce.requests.ReviewCreateRequest;
import com.prgrms.ohouse.web.commerce.requests.ReviewUpdateRequest;
import com.prgrms.ohouse.web.commerce.results.ReviewCreateResult;

@SpringBootTest(properties = "spring.profiles.active:test")
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@Transactional
class RestReviewControllerTest {
	@Autowired
	TestDataProvider dataProvider;
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	@Autowired
	private ReviewService reviewService;

	@Value("${jwt.headerName}")
	private String tokenHeaderName;

	@DisplayName("?????? ?????? ?????? ?????????")
	@Test
	void testNormalReviewRegister() throws Exception {
		Product product = dataProvider.insertProduct();
		User user = dataProvider.insertGuestUser("guest");
		String reviewContent = "?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ??????";
		ReviewCreateRequest request = new ReviewCreateRequest(product.getId(), user.getId(), 3, reviewContent);
		String json = mapper.writeValueAsString(request);

		MvcResult mvcResult = mockMvc.perform(multipart("/api/v0/reviews")
				.part(new MockPart("request", json.getBytes(StandardCharsets.UTF_8)))
				.header(tokenHeaderName, TestDataProvider.GUEST_TOKEN))
			.andExpect(status().isCreated())
			.andDo(document("review-create", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName(tokenHeaderName).description("JWT ??????")
				),
				requestPartFields("request",
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("productId").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("reviewPoint").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("contents").type(JsonFieldType.STRING).description("?????? ??????")
				),
				responseFields(
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? ??????"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????")
				)
			))
			.andReturn();

		String location = mvcResult.getResponse().getHeader("location");
		String content = mvcResult.getResponse().getContentAsString();
		ReviewCreateResult result = mapper.readValue(content, ReviewCreateResult.class);
		assertDoesNotThrow(() -> {
			Review review = reviewRepository.findById(result.getId()).get();
			assertThat(review.getContents()).isEqualTo(reviewContent);
			assertThat(location).isEqualTo("http://localhost:8080/api/v0/reviews/" + review.getId());
		});
	}

	@DisplayName("?????? ?????? ?????? ?????????")
	@Test
	void testPhotoReviewRegister() throws Exception {
		Product product = dataProvider.insertProduct();
		User user = dataProvider.insertGuestUser("guest");
		MockMultipartFile image = new MockMultipartFile(
			"review-image",
			"test.png",
			"image/png",
			"<<png data>>".getBytes());
		String reviewContent = "?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ?????? ??????";
		ReviewCreateRequest request = new ReviewCreateRequest(product.getId(), user.getId(), 3, reviewContent);
		String json = mapper.writeValueAsString(request);

		MvcResult mvcResult = mockMvc.perform(multipart("/api/v0/reviews")
				.file(image)
				.part(new MockPart("request", json.getBytes(StandardCharsets.UTF_8)))
				.header(tokenHeaderName, TestDataProvider.GUEST_TOKEN))
			.andExpect(status().isCreated())
			.andDo(document("photo-review-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName(tokenHeaderName).description("JWT ??????")
				),
				requestParts(
					partWithName("review-image").optional().description("?????? ????????? ??????"),
					partWithName("request").description("????????? ?????? ??????")
				),
				requestPartFields("request",
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("productId").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("reviewPoint").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("contents").type(JsonFieldType.STRING).description("?????? ??????")
				)
				,
				responseFields(
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????????")
				)
			))
			.andDo(print())
			.andReturn();

		String location = mvcResult.getResponse().getHeader("location");
		String content = mvcResult.getResponse().getContentAsString();

		ReviewCreateResult result = mapper.readValue(content, ReviewCreateResult.class);
		assertDoesNotThrow(() -> {
			Review review = reviewRepository.findById(result.getId()).get();
			assertThat(review.getContents()).isEqualTo(reviewContent);
			assertThat(location).isEqualTo("http://localhost:8080/api/v0/reviews/" + review.getId());
		});
	}

	@DisplayName("?????? ?????? ????????? ?????? ?????????")
	@Test
	void testReviewPagingTest() throws Exception {
		List<Review> reviews = dataProvider.insert40NormalReview();
		Product product = reviews.get(0).getProduct();

		MvcResult mvcResult = mockMvc.perform(get("/api/v0/reviews/" + product.getId())
				.param("page", "0")
				.param("size", "10")
				.param("sort", "createdAt,desc"))
			.andDo(print())
			.andDo(document("review-inquiry",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("page").description("????????? ????????? ??????"),
					parameterWithName("size").description("????????? ??? ?????? ??????"),
					parameterWithName("sort").description("?????? ?????? 1.'createdAt,desc' 2.'helpPoint',desc")
				),
				responseFields(
					fieldWithPath("reviews.[]").type(JsonFieldType.ARRAY).description("??????"),
					fieldWithPath("reviews.[].id").type(JsonFieldType.NUMBER).description("?????? id"),
					fieldWithPath("reviews.[].user").type(JsonFieldType.OBJECT).description("?????????"),
					fieldWithPath("reviews.[].user.username").type(JsonFieldType.STRING).description("????????? ?????????"),
					fieldWithPath("reviews.[].reviewPoint").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("reviews.[].contents").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("reviews.[].helpPoint").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("reviews.[].createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("pageInformation").type(JsonFieldType.OBJECT).description("????????? ??????"),
					fieldWithPath("pageInformation.pageNumber").type(JsonFieldType.NUMBER).description("?????? ????????? ??????"),
					fieldWithPath("pageInformation.pageSize").type(JsonFieldType.NUMBER)
						.description("???????????? ????????? ?????? ?????? ??????"),
					fieldWithPath("pageInformation.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ??????"),
					fieldWithPath("pageInformation.numberOfElements").type(JsonFieldType.NUMBER)
						.description("?????? ???????????? ????????? ?????? ??????"),
					fieldWithPath("pageInformation.totalElements").type(JsonFieldType.NUMBER).description("??? ?????? ??????")
				)
			))
			.andExpect(status().isOk())
			.andReturn();

		String response = mvcResult.getResponse().getContentAsString();
		PagedReviewInformation result = mapper.readValue(response, PagedReviewInformation.class);
		PageInformation page = result.getPageInformation();
		assertThat(page.getNumberOfElements()).isEqualTo(10);
		assertThat(page.getTotalElements()).isEqualTo(40);
		assertThat(page.getPageNumber()).isZero();
		assertThat(page.getTotalPages()).isEqualTo(4);
		assertThat(result.getReviews()).hasSize(10);
	}

	@DisplayName("?????? ?????? ????????? ?????? ?????????")
	@Test
	void testPhotoReviewPagingTest() throws Exception {
		List<Review> reviews = dataProvider.insert40PhotoReview();
		Product product = reviews.get(0).getProduct();

		MvcResult mvcResult = mockMvc.perform(get("/api/v0/reviews/" + product.getId())
				.param("page", "0")
				.param("size", "10")
				.param("sort", "createdAt,desc")
				.param("reviewType", ReviewType.PHOTO.name()))
			.andDo(print())
			.andDo(document("photo-review-inquiry",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParameters(
					parameterWithName("reviewType").description("????????? ?????? ?????? NORMAL, PHOTO"),
					parameterWithName("page").description("????????? ?????? ????????? ??????"),
					parameterWithName("size").description("????????? ??? ?????? ??????"),
					parameterWithName("sort").description("?????? ?????? 1.'createdAt,desc' 2.'helpPoint',desc")
				),
				responseFields(
					fieldWithPath("reviews.[]").type(JsonFieldType.ARRAY).description("??????"),
					fieldWithPath("reviews.[].id").type(JsonFieldType.NUMBER).description("?????? id"),
					fieldWithPath("reviews.[].user").type(JsonFieldType.OBJECT).description("?????????"),
					fieldWithPath("reviews.[].user.username").type(JsonFieldType.STRING).description("????????? ?????????"),
					fieldWithPath("reviews.[].reviewPoint").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("reviews.[].reviewImageUrl").type(JsonFieldType.STRING).description("?????? ?????? ??????"),
					fieldWithPath("reviews.[].contents").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("reviews.[].helpPoint").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("reviews.[].createdAt").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("pageInformation").type(JsonFieldType.OBJECT).description("????????? ??????"),
					fieldWithPath("pageInformation.pageNumber").type(JsonFieldType.NUMBER).description("?????? ????????? ??????"),
					fieldWithPath("pageInformation.pageSize").type(JsonFieldType.NUMBER)
						.description("???????????? ????????? ?????? ?????? ??????"),
					fieldWithPath("pageInformation.totalPages").type(JsonFieldType.NUMBER).description("??? ????????? ??????"),
					fieldWithPath("pageInformation.numberOfElements").type(JsonFieldType.NUMBER)
						.description("?????? ???????????? ????????? ?????? ??????"),
					fieldWithPath("pageInformation.totalElements").type(JsonFieldType.NUMBER).description("??? ?????? ??????")
				)
			))
			.andExpect(status().isOk())
			.andReturn();

		String response = mvcResult.getResponse().getContentAsString();
		PagedPhotoReviewInformation result = mapper.readValue(response, PagedPhotoReviewInformation.class);
		PageInformation page = result.getPageInformation();
		assertThat(page.getNumberOfElements()).isEqualTo(10);
		assertThat(page.getTotalElements()).isEqualTo(40);
		assertThat(page.getPageNumber()).isZero();
		assertThat(page.getTotalPages()).isEqualTo(4);
		assertThat(result.getReviews()).hasSize(10);
	}

	@DisplayName("?????? ??????")
	@Test
	void testReviewDelete() throws Exception {
		Product product = dataProvider.insertProduct();
		User user = dataProvider.insertGuestUser("guest");
		Review review = dataProvider.insertNormalReview(product, user, 5,
			"review review review review review review review review review", 0);
		MvcResult mvcResult = mockMvc.perform(delete("/api/v0/reviews/" + review.getId())
				.header(tokenHeaderName, TestDataProvider.GUEST_TOKEN)
			)
			.andDo(print())
			.andDo(document("review-delete",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName(tokenHeaderName).description("JWT ??????")
				),
				responseFields(
					fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ??????"),
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? id")
				)
			))
			.andExpect(status().isOk())
			.andReturn();
	}

	@DisplayName("?????? ??????")
	@Test
	void testReviewUpdate() throws Exception {
		Product product = dataProvider.insertProduct();
		User user = dataProvider.insertGuestUser("guest");
		Review review = dataProvider.insertNormalReview(product, user, 5,
			"review review review review review review review review review", 0);

		ReviewUpdateRequest request = new ReviewUpdateRequest(review.getId(), 5,
			"reviewupdate reviewupdate reviewupdate reviewupdate reviewupdate");
		String json = mapper.writeValueAsString(request);

		MvcResult mvcResult = mockMvc.perform(multipart("/api/v0/reviews/" + review.getId())
				.part(new MockPart("request", json.getBytes(StandardCharsets.UTF_8)))
				.header(tokenHeaderName, TestDataProvider.GUEST_TOKEN))
			.andExpect(status().isOk())
			.andDo(document("review-update",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestHeaders(
					headerWithName(tokenHeaderName).description("JWT ??????")
				),
				requestParts(
					partWithName("review-image").optional().description("????????? ????????? ??????"),
					partWithName("request").description("????????? ?????? ??????")
				),
				requestPartFields("request",
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? id"),
					fieldWithPath("reviewPoint").type(JsonFieldType.NUMBER).description("?????? ??????"),
					fieldWithPath("contents").type(JsonFieldType.STRING).description("?????? ??????")
				),
				responseFields(
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("????????? ?????? ?????????"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("?????? ?????? ?????????")
				)
			))
			.andDo(print())
			.andReturn();
	}
}
