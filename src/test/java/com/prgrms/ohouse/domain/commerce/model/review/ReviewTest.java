package com.prgrms.ohouse.domain.commerce.model.review;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.ohouse.domain.commerce.model.review.dummy.Products;
import com.prgrms.ohouse.domain.commerce.model.review.dummy.User;

class ReviewTest {
	@DisplayName("리뷰의 만족도가 0~5 범위를 벗어나면 IllegalArgumentException 발생")
	@ParameterizedTest
	@ValueSource(ints = {-1, 6})
	void testReviewPointRangeException(int reviewPoint) {
		User user = new User();
		Products item = new Products();
		String contents = "reviewreviewreviewreviewreviewreviewreviewreviewreviewreviewreview";

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> Review.createNormalReview(item, user, reviewPoint, contents));
	}

	@DisplayName("리뷰 내용의 길이가 20자 미만이면 IllegalArgumentException 발생")
	@Test
	void testReviewContents() {
		User user = new User();
		Products item = new Products();
		String contents = "short review";

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> Review.createNormalReview(item, user, 3, contents));
	}
}