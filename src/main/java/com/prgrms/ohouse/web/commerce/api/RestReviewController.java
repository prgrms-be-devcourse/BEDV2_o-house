package com.prgrms.ohouse.web.commerce.api;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.commerce.application.ReviewService;
import com.prgrms.ohouse.domain.commerce.model.review.ReviewType;
import com.prgrms.ohouse.web.commerce.results.ReviewCreateResult;
import com.prgrms.ohouse.web.requests.ReviewCreateRequest;

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

	@PostMapping(value = "/api/v0/reviews", consumes = {MediaType.APPLICATION_JSON_VALUE,
		MediaType.MULTIPART_FORM_DATA_VALUE})
	public ResponseEntity<ReviewCreateResult> createNewReview(
		@RequestPart(value = "request") ReviewCreateRequest request,
		@RequestPart(value = "review-image", required = false) MultipartFile image) {
		Long newReviewId = reviewService.registerReview(request.toCommand(image));
		URI location = URI.create("http://localhost:8080/api/v0/reviews/" + newReviewId);
		return ResponseEntity.created(location).body(ReviewCreateResult.ok(newReviewId));
	}

	@ExceptionHandler(Exception.class)
	public void logging(Exception e) {
		log.error(e.getMessage(), e);
	}

	//TODO: 입력받은 리뷰 id가 정상적인지 확인 후 예외처리
	// @DeleteMapping("/api/v0/reviews/{id}")
	// public ResponseEntity<String> deleteReview(@PathVariable Long id) {
	// 	reviewService.deleteReview();
	// 	return ResponseEntity.ok().body("delete successful");
	// }

}
