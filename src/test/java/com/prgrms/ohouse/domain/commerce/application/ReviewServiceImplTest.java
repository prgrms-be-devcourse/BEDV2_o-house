package com.prgrms.ohouse.domain.commerce.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.config.WithMockCustomUser;
import com.prgrms.ohouse.domain.commerce.application.command.ReviewRegisterCommand;
import com.prgrms.ohouse.domain.commerce.application.impl.ReviewServiceImpl;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.review.PageInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedPhotoReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;
import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.infrastructure.file.LocalFileUploader;

@SpringBootTest(properties = "spring.profiles.active:test")
@Transactional
class ReviewServiceImplTest {
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	LocalFileUploader fileUploader;
	@Autowired
	private ReviewServiceImpl reviewService;
	@Autowired
	private TestDataProvider dataProvider;
	@Autowired
	AuthUtility authUtility;

	@AfterEach
	void deleteAllFile() throws IOException {
		fileUploader.deleteAllFile();
	}

	@DisplayName("일반 리뷰 생성 테스트")
	@Test
	void testReviewCreate() {
		Product product = dataProvider.insertProduct();
		User user = dataProvider.insertUser();
		String contents = "review contents with suitable contents length over 20";
		int reviewPoint = 4;

		ReviewRegisterCommand command = new ReviewRegisterCommand(product.getId(), user.getId(), reviewPoint, contents,
			null);

		Long reviewId = reviewService.registerReview(command);
		Review found = reviewRepository.findById(reviewId).orElseThrow();

		assertThat(found.getContents()).isEqualTo(contents);
		assertThat(found.getReviewPoint()).isEqualTo(reviewPoint);
		assertThat(found.getReviewType()).isEqualTo(ReviewType.NORMAL);
	}

	@DisplayName("사진 리뷰 생성 테스트")
	@Test
	void testPhotoReviewCreate() {
		Product product = dataProvider.insertProduct();
		User user = dataProvider.insertUser();
		String contents = "review contents with suitable contents length over 20";
		int reviewPoint = 4;
		MockMultipartFile file = new MockMultipartFile(
			"thumbnail",
			"test.png",
			"image/png",
			"<<png data>>".getBytes());

		ReviewRegisterCommand command = new ReviewRegisterCommand(product.getId(), user.getId(), reviewPoint, contents,
			file);

		Long reviewId = reviewService.registerReview(command);
		Review found = reviewRepository.findById(reviewId).orElseThrow();

		assertThat(found.getContents()).isEqualTo(contents);
		assertThat(found.getReviewPoint()).isEqualTo(reviewPoint);
		assertThat(found.getReviewType()).isEqualTo(ReviewType.PHOTO);
		assertThat(found.getReviewImage()).isNotNull();
	}

	@DisplayName("특정 상품에 달린 모든 리뷰를 날짜순으로 조회 할 수 있다")
	@Test
	void testReviewInquiryOrderByDate() {
		List<Review> reviews = dataProvider.insert40NormalReview();
		Product product = reviews.get(0).getProduct();
		Pageable pageable = PageRequest.of(2, 10, Sort.Direction.DESC, "createdAt");

		PagedReviewInformation result = reviewService.loadAllProductReviews(pageable, product.getId());

		PageInformation page = result.getPageInformation();
		assertThat(page.getNumberOfElements()).isEqualTo(10);
		assertThat(page.getTotalElements()).isEqualTo(40);
		assertThat(page.getPageNumber()).isEqualTo(2);
		assertThat(page.getTotalPages()).isEqualTo(4);
		assertThat(result.getReviews()).hasSize(10);
	}

	@DisplayName("특정 상품에 달린 모든 리뷰를 도움 점수 순으로 조회 할 수 있다")
	@Test
	void testReviewInquiryOrderByHelpPoint() {
		User user = dataProvider.insertUser();
		String content = "content content content content content content content content";
		Product product = dataProvider.insertProduct();
		for (int i = 9; i >= 0; i--) {
			dataProvider.insertNormalReview(product, user, 4, content, i * i);
		}
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "helpPoint");

		PagedReviewInformation result = reviewService.loadAllProductReviews(pageable, product.getId());
		PageInformation page = result.getPageInformation();
		List<ReviewInformation> resultContent = result.getReviews();
		assertThat(page.getNumberOfElements()).isEqualTo(10);
		assertThat(page.getTotalElements()).isEqualTo(10);
		assertThat(page.getPageNumber()).isZero();
		assertThat(page.getTotalPages()).isEqualTo(1);
		assertThat(result.getReviews()).hasSize(10);
		for (int i = 9; i >= 0; i--) {
			assertThat(resultContent.get(9 - i).getHelpPoint()).isEqualTo(i * i);
		}
	}

	@DisplayName("특정 상품에 달린 사진 리뷰만 날짜순으로 조회 할 수 있다.")
	@Test
	void testOnlyPhotoReviewInquiryOrderByDate() {
		List<Review> reviews = dataProvider.insert40PhotoReview();
		Product product = reviews.get(0).getProduct();
		int pageSize = 10;
		Pageable pageable = PageRequest.of(2, pageSize, Sort.Direction.DESC, "createdAt");

		PagedPhotoReviewInformation result = reviewService.loadOnlyPhotoReviews(pageable, product.getId());

		PageInformation page = result.getPageInformation();
		assertThat(page.getNumberOfElements()).isEqualTo(10);
		assertThat(page.getTotalElements()).isEqualTo(40);
		assertThat(page.getPageNumber()).isEqualTo(2);
		assertThat(page.getTotalPages()).isEqualTo(4);
		assertThat(result.getReviews()).hasSize(10);
	}

	@DisplayName("리뷰 작성자는 자신의 리뷰를 삭제할 수 있다")
	@Test
	@WithMockCustomUser
	void testReviewDelete() {
		User authUser = authUtility.getAuthUser();
		Review review = dataProvider.insertPhotoReviewWithUser(authUser);

		reviewService.deleteReview(review.getId());

		assertTrue(reviewRepository.findById(review.getId()).isEmpty());
	}

	@DisplayName("리뷰 작성자가 아닌 사용자가 리뷰 삭제를 요청할 경우 예외를 발생시킨다")
	@Test
	@WithMockCustomUser
	void testReviewDeleteFail() {
		Review review = dataProvider.insertPhotoReview();

		assertThrows(AccessDeniedException.class, () -> reviewService.deleteReview(review.getId()));
	}

}
