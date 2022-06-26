package com.prgrms.ohouse.domain.commerce.model.review;

import java.util.Optional;

public interface ReviewRepository {
	Review save(Review review);

	Optional<Review> findById(Long id);
}
