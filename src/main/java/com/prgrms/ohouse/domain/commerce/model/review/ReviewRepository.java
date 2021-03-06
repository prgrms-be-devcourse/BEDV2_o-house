package com.prgrms.ohouse.domain.commerce.model.review;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.user.model.User;

public interface ReviewRepository {
	Review save(Review review);

	Optional<Review> findById(Long id);

	Page<Review> findByProduct(Product product, Pageable pageable);

	Page<Review> findByProductAndReviewType(Product product, ReviewType type, Pageable pageable);

	void delete(Review review);

	Page<Review> findByUser(User user, Pageable pageable);

}
