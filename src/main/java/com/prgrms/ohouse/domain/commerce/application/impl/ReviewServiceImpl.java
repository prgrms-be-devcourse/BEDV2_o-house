package com.prgrms.ohouse.domain.commerce.application.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.commerce.application.ReviewService;
import com.prgrms.ohouse.domain.commerce.application.command.ReviewRegisterCommand;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.domain.commerce.model.review.PageInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedPhotoReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.PagedReviewInformation;
import com.prgrms.ohouse.domain.commerce.model.review.Review;

import com.prgrms.ohouse.domain.commerce.model.review.ReviewInquiryFailException;

import com.prgrms.ohouse.domain.commerce.model.review.ReviewRegisterFailException;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;
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
			saveReviewImage(command.getReviewImage(), review);
		}
		return review.getId();
	}

	private void saveReviewImage(MultipartFile reviewImageFile, Review review) {
		try {
			fileManager.store(reviewImageFile, review);
		} catch (FileIOException e) {
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
	public PagedPhotoReviewInformation loadOnlyPhotoReviews(Pageable pageable, Long productId) {
		Product product = productRepository.findById(productId)
			.orElseThrow(() -> new ReviewInquiryFailException(INVALID_PRODUCT_MESSAGE));
		Page<Review> reviewPage = reviewRepository.findByProductAndReviewType(product, ReviewType.PHOTO, pageable);
		PageInformation pageInformation = PageInformation.createNewPageInformation(reviewPage);
		return PagedPhotoReviewInformation.of(pageInformation, reviewPage.getContent());
	}
}
