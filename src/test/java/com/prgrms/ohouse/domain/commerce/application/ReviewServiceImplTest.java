package com.prgrms.ohouse.domain.commerce.application;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.commands.ReviewRegisterCommand;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;
import com.prgrms.ohouse.infrastructure.TestDataProvider;
import com.prgrms.ohouse.infrastructure.file.LocalFileUploader;

@SpringBootTest(properties = "spring.profiles.active:test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class ReviewServiceImplTest {
	@Autowired
	ReviewRepository reviewRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	ProductRepository productRepository;
	@Autowired
	LocalFileUploader fileUploader;
	@Autowired
	private ReviewService reviewService;
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
		assertThat(found.getReviewImageUrl()).isNotNull();
	}
}