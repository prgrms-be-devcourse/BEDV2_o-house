package com.prgrms.ohouse.domain.commerce.model.product;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.web.commerce.results.ProductViewMainPageResult;

public interface ProductRepository {
	Slice<ProductViewMainPageResult> findByIdAtDesc(Pageable pageable);

	Product save(Product product);

	Optional<Product> findById(Long id);
}

