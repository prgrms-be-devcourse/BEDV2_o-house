package com.prgrms.ohouse.domain.commerce.application;

import org.springframework.data.domain.Pageable;

import com.prgrms.ohouse.domain.commerce.application.command.ReviewRegisterCommand;
import com.prgrms.ohouse.domain.commerce.application.command.ReviewUpdateCommand;
import com.prgrms.ohouse.domain.commerce.model.review.PagedPhotoReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedReviewInformation;

public interface ReviewService {

	Long registerReview(ReviewRegisterCommand command);

	PagedReviewInformation loadAllProductReviews(Pageable pageable, Long productId);

	PagedPhotoReviewInformation loadOnlyPhotoReviews(Pageable pageable, Long productId);

	void deleteReview(Long reviewId);

	void updateReview(ReviewUpdateCommand command);

	PagedReviewInformation loadMyAllReviews(Pageable pageable);
}
