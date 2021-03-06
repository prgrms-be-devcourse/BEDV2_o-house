package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;

public interface JpaReviewRepository extends ReviewRepository, JpaRepository<Review, Long> {

	@Override
	Page<Review> findByProduct(Product product, Pageable pageable);

	@Override
	Page<Review> findByProductAndReviewType(Product product, ReviewType type, Pageable pageable);
}
