package com.prgrms.ohouse.web.api;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.commerce.application.ReviewService;
import com.prgrms.ohouse.web.requests.ReviewCreateRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestReviewController {
	private final ReviewService reviewService;

	// @GetMapping("/api/v0/reviews/{productId}")
	// public String getAllReview(Pageable pageable, @PathVariable Long productId){
	// 	reviewService.
	//
	// }

	@PostMapping("/api/v0/reviews")
	public ResponseEntity<String> createNewReview(@RequestBody ReviewCreateRequest request) {
		Long newReviewId = reviewService.registerReview(request.toCommand());
		URI location = URI.create("http://localhost:8080/api/v1/reviews/" + newReviewId);
		return ResponseEntity.created(location).body("new review created successful");
	}
}
