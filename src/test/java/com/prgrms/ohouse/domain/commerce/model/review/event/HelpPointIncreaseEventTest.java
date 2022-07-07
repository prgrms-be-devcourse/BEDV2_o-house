package com.prgrms.ohouse.domain.commerce.model.review.event;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.prgrms.ohouse.domain.commerce.application.ReviewService;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.infrastructure.TestDataProvider;

@SpringBootTest
class HelpPointIncreaseEventTest {
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private ReviewRepository reviewRepository;

	@Autowired
	private TestDataProvider dataProvider;

	@DisplayName("HelpPointIncrease 이벤트 테스트")
	@Test
	void testSimpleSends() throws Exception {
		Review review = dataProvider.insertPhotoReview();

		reviewService.publishHelpPointIncreasingEvent(review.getId());
		Thread.sleep(1000);

		Review found = reviewRepository.findById(review.getId()).get();
		assertThat(found.getHelpPoint()).isEqualTo(review.getHelpPoint() + 1);
	}
}