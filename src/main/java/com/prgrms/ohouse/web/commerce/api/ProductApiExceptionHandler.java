package com.prgrms.ohouse.web.commerce.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.prgrms.ohouse.domain.commerce.model.product.ProductException;
import com.prgrms.ohouse.web.commerce.results.ErrorResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class ProductApiExceptionHandler {
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
}
