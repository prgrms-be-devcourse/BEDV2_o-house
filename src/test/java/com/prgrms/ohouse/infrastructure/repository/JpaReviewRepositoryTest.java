package com.prgrms.ohouse.infrastructure.repository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.infrastructure.TestDataProvider;

@SpringBootTest(properties = "spring.profiles.active:local")
@Transactional
class JpaReviewRepositoryTest {
	@Autowired
	private ReviewRepository reviewRepository;
	@Autowired
	private TestDataProvider dataProvider;

	@DisplayName("상품 리뷰를 날짜순으로 페이징 하여 조회한다.")
	@Test
	void test() {
		List<Review> reviews = dataProvider.insert40Review();
		Product target = reviews.get(0).getProduct();
		Pageable pageable = PageRequest.of(0, 10, Sort.Direction.DESC, "createdAt");

		Page<Review> result = reviewRepository.findByProduct(target, pageable);

		assertThat(result.getTotalPages()).isEqualTo(4);
		assertThat(result.getTotalElements()).isEqualTo(40);
		assertThat(result.getContent()).hasSize(10);
	}
}