package com.prgrms.ohouse.web.commerce.api;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.commerce.application.ProductService;
import com.prgrms.ohouse.domain.commerce.model.product.ProductException;
import com.prgrms.ohouse.web.commerce.results.ErrorResult;
import com.prgrms.ohouse.web.commerce.results.SliceResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestProductController {
	private final ProductService productService;

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResult> handleIllegalArgument(IllegalArgumentException e) {
		log.error(e.getMessage(), e);
		ErrorResult body = ErrorResult.of(HttpStatus.BAD_REQUEST, "INVALID_INPUT_VALUE");
		return ResponseEntity.badRequest().body(body);
	}

	@ExceptionHandler(ProductException.class)
	public ResponseEntity<ErrorResult> handleProduct(ProductException e) {
		log.error(e.getMessage(), e);
		ErrorResult body = ErrorResult.of(HttpStatus.BAD_REQUEST, "INVALID_PRODUCT_VALUE");
		return ResponseEntity.badRequest().body(body);
	}

	@GetMapping("/api/v0/products")
	public ResponseEntity<SliceResult> viewProductsByAttribute(@RequestParam String attribute,
		Pageable pageable) {
		return ResponseEntity.ok().body(productService.findMainPageOrderByCreatedAtDesc(pageable, attribute));
	}
}
