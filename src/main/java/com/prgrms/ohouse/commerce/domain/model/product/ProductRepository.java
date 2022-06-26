package com.prgrms.ohouse.commerce.domain.model.product;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.commerce.domain.application.command.ProductViewMainPageCommand;

public interface ProductRepository{
	Slice<ProductViewMainPageCommand> findByIdAtDesc(Pageable pageable);
	Product save(Product product);
}
