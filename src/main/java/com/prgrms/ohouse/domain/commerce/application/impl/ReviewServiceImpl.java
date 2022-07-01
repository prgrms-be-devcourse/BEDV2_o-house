package com.prgrms.ohouse.domain.commerce.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewDeleteFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewInquiryFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewRegisterFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewUpdateFailException;
import com.prgrms.ohouse.domain.common.file.FileIOException;
import com.prgrms.ohouse.domain.common.file.FileManager;
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

	//TODO: 로그인 된 사용자만 이용할 수 있도록 검증
	@Transactional
	@Override
	public Long registerReview(ReviewRegisterCommand command) {
		Product product = productRepository.findById(command.getProductId())
			.orElseThrow(() -> new ReviewRegisterFailException(INVALID_PRODUCT_MESSAGE));
		User user = userRepository.findById(command.getUserId())
			.orElseThrow(() -> new ReviewRegisterFailException("invalid user id"));
		Review review = Review.createReview(product, user, command.getReviewPoint(), command.getContents());
		review = reviewRepository.save(review);
		if (command.isPhotoReview()) {
			try {
				fileManager.store(command.getReviewImage(), review);
			} catch (FileIOException e) {
				throw new ReviewRegisterFailException(e.getMessage(), e);
			}
		}
		return review.getId();
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
	public PagedPhotoReviewInformation loadOnlyPhotoReviews(Pageable pageable, Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new ReviewInquiryFailException(INVALID_PRODUCT_MESSAGE));
		Page<Review> reviewPage = reviewRepository.findByProductAndReviewType(product, ReviewType.PHOTO, pageable);
		PageInformation pageInformation = PageInformation.createNewPageInformation(reviewPage);
		return PagedPhotoReviewInformation.of(pageInformation, reviewPage.getContent());
	}

	//TODO: 리뷰 작성자만 지울 수 있도록 검증
	@Transactional
	@Override
	public void deleteReview(Long id) {
		Review review = reviewRepository.findById(id)
			.orElseThrow(() -> new ReviewDeleteFailException("invalid review id"));
		reviewRepository.delete(review);
	}

	@Override
	public void updateReview(ReviewUpdateCommand command) {
		Review review = reviewRepository.findById(command.getId())
			.orElseThrow(() -> new ReviewUpdateFailException("invalid review id"));
		if (command.isPhotoReview()) {
			try {
				fileManager.delete();
				fileManager.store(command.getReviewImage(), review);
			} catch (FileIOException e) {
				throw new ReviewUpdateFailException(e.getMessage(), e);
			}
		}
		review.modifyReview(command.getReviewPoint(), command.getContents());
	}
}
