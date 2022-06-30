package com.prgrms.ohouse.infrastructure.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.web.commerce.results.ProductViewMainPageResult;

public interface JpaProductRepositoryCustom {
	Slice<ProductViewMainPageResult> findByIdAtDesc(Pageable pageable);
}
