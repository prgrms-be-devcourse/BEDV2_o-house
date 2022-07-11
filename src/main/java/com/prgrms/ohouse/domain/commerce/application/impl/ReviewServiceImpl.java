package com.prgrms.ohouse.domain.commerce.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.ReviewService;
import com.prgrms.ohouse.domain.commerce.application.command.ReviewRegisterCommand;
import com.prgrms.ohouse.domain.commerce.application.command.ReviewUpdateCommand;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.domain.commerce.model.review.PageInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedPhotoReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;
import com.prgrms.ohouse.domain.commerce.model.review.event.HelpPointIncreaseEvent;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewDeleteFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewInquiryFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewNotFoundException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewRegisterFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewUpdateFailException;
import com.prgrms.ohouse.domain.common.event.DomainEventPublisher;
import com.prgrms.ohouse.domain.common.file.FileIOException;
import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewServiceImpl implements ReviewService {
	private static final String INVALID_PRODUCT_MESSAGE = "invalid product id";
	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final FileManager fileManager;
	private final AuthUtility authUtility;
	private final DomainEventPublisher eventPublisher;

	@Transactional
	@Override
	public Long registerReview(ReviewRegisterCommand command) {
		Product product = productRepository.findById(command.getProductId())
			.orElseThrow(() -> new ReviewRegisterFailException(INVALID_PRODUCT_MESSAGE));
		User user = userRepository.findById(command.getUserId())
			.orElseThrow(() -> new ReviewRegisterFailException("invalid user id"));
		try {
			Review review = Review.createReview(product, user, command.getReviewPoint(), command.getContents());
			review = reviewRepository.save(review);
			if (command.isPhotoReview()) {
				fileManager.store(command.getReviewImage(), review);
			}
			return review.getId();
		} catch (IllegalArgumentException | FileIOException e) {
			throw new ReviewRegisterFailException(e.getMessage(), e);
		}
	}

	@Override
	public PagedReviewInformation loadAllProductReviews(Pageable pageable, Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new ReviewInquiryFailException(INVALID_PRODUCT_MESSAGE));
		Page<Review> reviewPage = reviewRepository.findByProduct(product, pageable);
		PageInformation pageInformation = PageInformation.createNewPageInformation(reviewPage);
		return PagedReviewInformation.of(pageInformation, reviewPage.getContent());
	}

	@Override
	public PagedReviewInformation loadMyAllReviews(Pageable pageable) {
		User authUser = authUtility.getAuthUser();
		Page<Review> reviewPage = reviewRepository.findByUser(authUser, pageable);
		PageInformation pageInformation = PageInformation.createNewPageInformation(reviewPage);
		return PagedReviewInformation.of(pageInformation, reviewPage.getContent());
	}

	@Override
	public PagedPhotoReviewInformation loadOnlyPhotoReviews(Pageable pageable, Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new ReviewInquiryFailException(INVALID_PRODUCT_MESSAGE));
		Page<Review> reviewPage = reviewRepository.findByProductAndReviewType(product, ReviewType.PHOTO, pageable);
		PageInformation pageInformation = PageInformation.createNewPageInformation(reviewPage);
		return PagedPhotoReviewInformation.of(pageInformation, reviewPage.getContent());
	}

	@Transactional
	@Override
	public void deleteReview(Long id) {
		Review review = reviewRepository.findById(id)
			.orElseThrow(() -> new ReviewDeleteFailException(new ReviewNotFoundException()));
		User authUser = authUtility.getAuthUser();
		checkAuthor(authUser, review.getUser());
		reviewRepository.delete(review);
	}

	@Transactional
	@Override
	public void updateReview(ReviewUpdateCommand command) {
		Review review = reviewRepository.findById(command.getId())
			.orElseThrow(() -> new ReviewUpdateFailException(new ReviewNotFoundException()));
		User authUser = authUtility.getAuthUser();
		checkAuthor(authUser, review.getUser());
		if (command.isPhotoReview()) {
			try {
				fileManager.delete(review.getReviewImage(), review);
				fileManager.store(command.getReviewImage(), review);
			} catch (FileIOException e) {
				throw new ReviewUpdateFailException(e.getMessage(), e);
			}
		}
		review.modifyReview(command.getReviewPoint(), command.getContents());
	}

	private void checkAuthor(User target, User author) {
		if (target != author)
			throw new AccessDeniedException("access denied");
	}

	@Override
	public void publishHelpPointIncreasingEvent(Long reviewId) {
		eventPublisher.publish(new HelpPointIncreaseEvent(reviewId));
	}

	@Transactional
	@Override
	public void increaseHelpPoint(Long reviewId, int increaseValue) {
		Review review = reviewRepository.findById(reviewId).orElseThrow(ReviewNotFoundException::new);
		review.increaseHelpPoint(increaseValue);
	}

}
