package com.prgrms.ohouse.web.api;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
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

	@DisplayName("일반 리뷰 생성 테스트")
	@Test
	void testNormalReviewRegister() throws Exception {
		Product product = dataProvider.insertProduct();
		User user = dataProvider.insertUser();
		String reviewContent = "후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기";
		ReviewCreateRequest request = new ReviewCreateRequest(product.getId(), user.getId(), 3, reviewContent);
		String json = mapper.writeValueAsString(request);

		MvcResult mvcResult = mockMvc.perform(multipart("/api/v0/reviews")
				.part(new MockPart("request", json.getBytes(StandardCharsets.UTF_8))))
			.andExpect(status().isCreated())
			.andDo(document("review-create", preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestPartFields("request",
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("회원 번호"),
					fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 번호"),
					fieldWithPath("reviewPoint").type(JsonFieldType.NUMBER).description("리뷰 점수"),
					fieldWithPath("contents").type(JsonFieldType.STRING).description("리뷰 내용")
				),
				responseFields(
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 리뷰 번호"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("완료 메세지")
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

	@DisplayName("포토 리뷰 생성 테스트")
	@Test
	void testPhotoReviewRegister() throws Exception {
		Product product = dataProvider.insertProduct();
		User user = dataProvider.insertUser();
		MockMultipartFile image = new MockMultipartFile(
			"review-image",
			"test.png",
			"image/png",
			"<<png data>>".getBytes());
		String reviewContent = "후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기 후기";
		ReviewCreateRequest request = new ReviewCreateRequest(product.getId(), user.getId(), 3, reviewContent);
		String json = mapper.writeValueAsString(request);

		MvcResult mvcResult = mockMvc.perform(multipart("/api/v0/reviews")
				.file(image)
				.part(new MockPart("request", json.getBytes(StandardCharsets.UTF_8))))
			.andExpect(status().isCreated())
			.andDo(document("photo-review-create",
				preprocessRequest(prettyPrint()),
				preprocessResponse(prettyPrint()),
				requestParts(
					partWithName("review-image").description("리뷰 이미지 파일"),
					partWithName("request").description("생성할 리뷰 정보")
				),
				requestPartFields("request",
					fieldWithPath("userId").type(JsonFieldType.NUMBER).description("회원 번호"),
					fieldWithPath("productId").type(JsonFieldType.NUMBER).description("상품 번호"),
					fieldWithPath("reviewPoint").type(JsonFieldType.NUMBER).description("리뷰 점수"),
					fieldWithPath("contents").type(JsonFieldType.STRING).description("리뷰 내용")
				)
				,
				responseFields(
					fieldWithPath("id").type(JsonFieldType.NUMBER).description("생성된 리뷰 아이디"),
					fieldWithPath("message").type(JsonFieldType.STRING).description("완료 메세지")
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

	@DisplayName("일반 리뷰 페이징 조회 테스트")
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
					parameterWithName("page").description("조회할 페이지 번호"),
					parameterWithName("size").description("페이지 당 리뷰 개수"),
					parameterWithName("sort").description("정렬 기준 1.'createdAt,desc' 2.'helpPoint',desc")
				),
				responseFields(
					fieldWithPath("reviews.[]").type(JsonFieldType.ARRAY).description("리뷰"),
					fieldWithPath("reviews.[].id").type(JsonFieldType.NUMBER).description("리뷰 id"),
					fieldWithPath("reviews.[].user").type(JsonFieldType.OBJECT).description("작성자"),
					fieldWithPath("reviews.[].user.username").type(JsonFieldType.STRING).description("작성자 닉네임"),
					fieldWithPath("reviews.[].reviewPoint").type(JsonFieldType.NUMBER).description("리뷰 점수"),
					fieldWithPath("reviews.[].contents").type(JsonFieldType.STRING).description("리뷰 내용"),
					fieldWithPath("reviews.[].helpPoint").type(JsonFieldType.NUMBER).description("도움 점수"),
					fieldWithPath("reviews.[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
					fieldWithPath("pageInformation").type(JsonFieldType.OBJECT).description("페이지 정보"),
					fieldWithPath("pageInformation.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
					fieldWithPath("pageInformation.pageSize").type(JsonFieldType.NUMBER)
						.description("페이지에 포함된 최대 리뷰 개수"),
					fieldWithPath("pageInformation.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 개수"),
					fieldWithPath("pageInformation.numberOfElements").type(JsonFieldType.NUMBER)
						.description("현재 페이지에 포함된 리뷰 개수"),
					fieldWithPath("pageInformation.totalElements").type(JsonFieldType.NUMBER).description("총 리뷰 개수")
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

	@DisplayName("사진 리뷰 페이징 조회 테스트")
	@Test
	void testPhotoReviewPagingTest() throws Exception {
		List<Review> reviews = dataProvider.insert40PhotoReview();
		Product product = reviews.get(0).getProduct();
		int pageSize = 10;

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
					parameterWithName("reviewType").description("조회할 리뷰 종류 NORMAL, PHOTO"),
					parameterWithName("page").description("조회할 리뷰 페이지 번호"),
					parameterWithName("size").description("페이지 당 리뷰 개수"),
					parameterWithName("sort").description("정렬 기준 1.'createdAt,desc' 2.'helpPoint',desc")
				),
				responseFields(
					fieldWithPath("reviews.[]").type(JsonFieldType.ARRAY).description("리뷰"),
					fieldWithPath("reviews.[].id").type(JsonFieldType.NUMBER).description("리뷰 id"),
					fieldWithPath("reviews.[].user").type(JsonFieldType.OBJECT).description("작성자"),
					fieldWithPath("reviews.[].user.username").type(JsonFieldType.STRING).description("작성자 닉네임"),
					fieldWithPath("reviews.[].reviewPoint").type(JsonFieldType.NUMBER).description("리뷰 점수"),
					fieldWithPath("reviews.[].reviewImageUrl").type(JsonFieldType.STRING).description("리뷰 사진 경로"),
					fieldWithPath("reviews.[].contents").type(JsonFieldType.STRING).description("리뷰 내용"),
					fieldWithPath("reviews.[].helpPoint").type(JsonFieldType.NUMBER).description("도움 점수"),
					fieldWithPath("reviews.[].createdAt").type(JsonFieldType.STRING).description("작성 날짜"),
					fieldWithPath("pageInformation").type(JsonFieldType.OBJECT).description("페이지 정보"),
					fieldWithPath("pageInformation.pageNumber").type(JsonFieldType.NUMBER).description("현재 페이지 번호"),
					fieldWithPath("pageInformation.pageSize").type(JsonFieldType.NUMBER)
						.description("페이지에 포함된 최대 리뷰 개수"),
					fieldWithPath("pageInformation.totalPages").type(JsonFieldType.NUMBER).description("총 페이지 개수"),
					fieldWithPath("pageInformation.numberOfElements").type(JsonFieldType.NUMBER)
						.description("현재 페이지에 포함된 리뷰 개수"),
					fieldWithPath("pageInformation.totalElements").type(JsonFieldType.NUMBER).description("총 리뷰 개수")
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
}
