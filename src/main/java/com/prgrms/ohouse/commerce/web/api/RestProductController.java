package com.prgrms.ohouse.commerce.web.api;

import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.commerce.domain.application.ProductService;
import com.prgrms.ohouse.commerce.domain.application.command.ProductViewMainPageCommand;
import com.prgrms.ohouse.commerce.domain.application.command.SliceCommand;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestProductController {
	private final ProductService productService;

	@GetMapping("/api/v0/products/{attribute}")
	public SliceCommand<ProductViewMainPageCommand> viewProductsByAttribute(@RequestParam String attribute,
		Pageable pageable) {
		return productService.findMainPageOrderByCreatedAtDesc(pageable, attribute);
	}
}
