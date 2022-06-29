package com.prgrms.ohouse.web.commerce.api;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.commerce.application.ProductService;
import com.prgrms.ohouse.web.commerce.results.SliceResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestProductController {
	private final ProductService productService;

	@GetMapping("/api/v0/products")
	public ResponseEntity<SliceResult> viewProductsByAttribute(@RequestParam String attribute,
		Pageable pageable) {
		return ResponseEntity.ok().body(productService.findMainPageOrderByCreatedAtDesc(pageable, attribute));
	}
}
