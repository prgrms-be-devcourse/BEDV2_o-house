package com.prgrms.ohouse.domain.commerce.application;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.command.ReviewRegisterCommand;
import com.prgrms.ohouse.domain.commerce.application.impl.ReviewServiceImpl;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.review.PageInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.infrastructure.file.LocalFileUploader;

@SpringBootTest(properties = "spring.profiles.active:test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

	@AfterAll
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
	void testReviewInquiryOrderByDate(){
		List<Review> reviews = dataProvider.insert40Review();
		Product product = reviews.get(0).getProduct();
		Pageable pageable = PageRequest.of(2, 10, Sort.Direction.DESC, "createdAt");

		PagedReviewInformation result = reviewService.loadAllProductReviews(pageable, product);

		PageInformation page = result.getPageInformation();
		assertThat(page.getNumberOfElements()).isEqualTo(10);
		assertThat(page.getTotalElements()).isEqualTo(40);
		assertThat(page.getPageNumber()).isEqualTo(2);
		assertThat(page.getTotalPages()).isEqualTo(4);
		assertThat(result.getContent()).hasSize(10);
	}
}