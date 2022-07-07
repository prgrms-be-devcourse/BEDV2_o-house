package com.prgrms.ohouse.web.commerce.api;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.commerce.application.ReviewService;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewDeleteFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewInquiryFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewRegisterFailException;
import com.prgrms.ohouse.domain.commerce.model.review.exception.ReviewUpdateFailException;
import com.prgrms.ohouse.web.commerce.requests.ReviewCreateRequest;
import com.prgrms.ohouse.web.commerce.requests.ReviewUpdateRequest;
import com.prgrms.ohouse.web.commerce.results.ErrorResult;
import com.prgrms.ohouse.web.commerce.results.ReviewCreateResult;
import com.prgrms.ohouse.web.commerce.results.ReviewDeleteResult;
import com.prgrms.ohouse.web.commerce.results.ReviewUpdateResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestReviewController {
	private final ReviewService reviewService;

	@GetMapping("/api/v0/reviews/{productId}")
	public ResponseEntity getAllReview(
		@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
		@PathVariable Long productId,
		@RequestParam(required = false) ReviewType reviewType) {
		if (reviewType == ReviewType.PHOTO) {
			return ResponseEntity.ok(reviewService.loadOnlyPhotoReviews(pageable, productId));
		} else {
			return ResponseEntity.ok(reviewService.loadAllProductReviews(pageable, productId));
		}
	}

	@PutMapping("/api/v0/reviews/{reviewId}/helpPoint")
	public ResponseEntity<String> increaseHelpPoint(@PathVariable Long reviewId) {
		reviewService.publishHelpPointIncreasingEvent(reviewId);
		return ResponseEntity.ok().body("request accepted successful");
	}

	@PostMapping(value = "/api/v0/reviews", consumes = {MediaType.APPLICATION_JSON_VALUE,
		MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ReviewCreateResult> createNewReview(
		@RequestPart(value = "request") ReviewCreateRequest request,
		@RequestPart(value = "review-image", required = false) MultipartFile image) {
		Long newReviewId = reviewService.registerReview(request.toCommand(image));
		URI location = URI.create("http://localhost:8080/api/v0/reviews/" + newReviewId);
		return ResponseEntity.created(location).body(ReviewCreateResult.ok(newReviewId));
	}

	@PostMapping("/api/v0/reviews/{reviewId}")
	public ResponseEntity<ReviewUpdateResult> updateReview(
		@RequestPart(value = "request") ReviewUpdateRequest request,
		@RequestPart(value = "review-image", required = false) MultipartFile image,
		@PathVariable Long reviewId) {
		reviewService.updateReview(request.toCommand(image));
		return ResponseEntity.ok(ReviewUpdateResult.ok(reviewId));
	}

	@DeleteMapping("/api/v0/reviews/{id}")
	public ResponseEntity<ReviewDeleteResult> deleteReview(@PathVariable Long id) {
		reviewService.deleteReview(id);
		return ResponseEntity.ok().body(ReviewDeleteResult.ok(id));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResult> handleUncaughtException(Exception e) {
		log.error(e.getMessage(), e);
		ErrorResult body = ErrorResult.of(HttpStatus.INTERNAL_SERVER_ERROR, "server error");
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
	}

	@ExceptionHandler(ReviewRegisterFailException.class)
	public ResponseEntity<ErrorResult> handleRegisterFailException(ReviewRegisterFailException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.of(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(ReviewUpdateFailException.class)
	public ResponseEntity<ErrorResult> handleUpdateFailException(ReviewUpdateFailException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.of(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(ReviewInquiryFailException.class)
	public ResponseEntity<ErrorResult> handleInquiryFailException(ReviewInquiryFailException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.of(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

	@ExceptionHandler(ReviewDeleteFailException.class)
	public ResponseEntity<ErrorResult> handleDeleteFailException(ReviewDeleteFailException e) {
		log.warn(e.getMessage(), e);
		ErrorResult body = ErrorResult.of(HttpStatus.BAD_REQUEST, e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
	}

}
