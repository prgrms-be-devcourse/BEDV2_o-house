package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;

public interface JpaReviewRepository extends ReviewRepository, JpaRepository<Review, Long> {

	@Override
	Page<Review> findByProduct(Product product,Pageable pageable);
}
