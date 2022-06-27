package com.prgrms.ohouse.domain.commerce.model.review;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.prgrms.ohouse.domain.commerce.model.product.Attribute;
import com.prgrms.ohouse.domain.commerce.model.product.Category;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Color;
import com.prgrms.ohouse.domain.commerce.model.product.enums.FourthCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.RootCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.SecondCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Shipping;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Size;
import com.prgrms.ohouse.domain.commerce.model.product.enums.ThirdCategory;
import com.prgrms.ohouse.domain.user.model.Address;
import com.prgrms.ohouse.domain.user.model.User;

class ReviewTest {
	@DisplayName("리뷰의 만족도가 0~5 범위를 벗어나면 IllegalArgumentException 발생")
	@ParameterizedTest
	@ValueSource(ints = {-1, 6})
	void testReviewPointRangeException(int reviewPoint) {
		User user = User.builder()
			.email("test@email.com")
			.nickname("nickname")
			.password("password")
			.followerCount(0)
			.defaultAddress(new Address())
			.build();
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		Product product = Product.of("product", 2000, "content", category,
			Attribute.of(Color.BLUE, Size.NORMAL, "brand",
				Shipping.NORMAL));
		String contents = "reviewreviewreviewreviewreviewreviewreviewreviewreviewreviewreview";

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> Review.createReview(product, user, reviewPoint, contents));
	}

	@DisplayName("리뷰 내용의 길이가 20자 미만이면 IllegalArgumentException 발생")
	@Test
	void testReviewContents() {
		User user = User.builder()
			.email("test@email.com")
			.nickname("nickname")
			.password("password")
			.followerCount(0)
			.defaultAddress(new Address())
			.build();
		Category category = Category.of(RootCategory.FURNITURE, SecondCategory.BED, ThirdCategory.FRAME,
			FourthCategory.NORMAL);
		Product product = Product.of("product", 2000, "content", category,
			Attribute.of(Color.BLUE, Size.NORMAL, "brand",
				Shipping.NORMAL));
		String contents = "short review";

		Assertions.assertThrows(IllegalArgumentException.class,
			() -> Review.createReview(product, user, 3, contents));
	}
}