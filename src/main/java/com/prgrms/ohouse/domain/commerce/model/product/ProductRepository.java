package com.prgrms.ohouse.domain.commerce.model.product;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.domain.commerce.application.command.ProductViewMainPageCommand;

public interface ProductRepository {
	Slice<ProductViewMainPageCommand> findByIdAtDesc(Pageable pageable);

	Product save(Product product);

	Optional<Product> findById(Long id);
}
