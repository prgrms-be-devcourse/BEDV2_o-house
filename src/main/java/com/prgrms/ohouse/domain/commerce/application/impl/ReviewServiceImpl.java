package com.prgrms.ohouse.domain.commerce.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.application.ReviewService;
import com.prgrms.ohouse.domain.commerce.application.command.ReviewRegisterCommand;
import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.domain.commerce.model.review.Review;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewImage;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRegisterFailException;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
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
	private final ProductRepository productRepository;
	private final ReviewRepository reviewRepository;
	private final UserRepository userRepository;
	private final FileManager fileManager;

	@Transactional
	@Override
	public Long registerReview(ReviewRegisterCommand command) {
		Product product = productRepository.findById(command.getProductId())
			.orElseThrow(() -> new ReviewRegisterFailException("invalid product id"));
		User user = userRepository.findById(command.getUserId())
			.orElseThrow(() -> new ReviewRegisterFailException("invalid user id"));

		Review review = Review.createReview(product, user, command.getReviewPoint(), command.getContents());
		review = reviewRepository.save(review);
		fileManager.store(command.getReviewImage(), review);
		return reviewRepository.save(review).getId();
	}
}
