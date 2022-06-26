package com.prgrms.ohouse.domain.commerce.domain.application.impl;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.domain.application.ProductService;
import com.prgrms.ohouse.domain.commerce.domain.application.command.ProductViewMainPageCommand;
import com.prgrms.ohouse.domain.commerce.domain.application.command.SliceCommand;
import com.prgrms.ohouse.domain.commerce.domain.model.product.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepository;

	@Override
	public SliceCommand<ProductViewMainPageCommand> findMainPageOrderByCreatedAtDesc(Pageable pageable,
		String attribute) {
		return new SliceCommand<>(productRepository.findByIdAtDesc(pageable));
	}
}
