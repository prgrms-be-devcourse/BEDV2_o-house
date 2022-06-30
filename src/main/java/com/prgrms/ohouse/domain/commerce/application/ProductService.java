package com.prgrms.ohouse.domain.commerce.application;

import org.springframework.data.domain.Pageable;

import com.prgrms.ohouse.web.commerce.results.ProductViewMainPageResult;
import com.prgrms.ohouse.web.commerce.results.SliceResult;

public interface ProductService {
	SliceResult<ProductViewMainPageResult> findMainPageOrderByCreatedAtDesc(Pageable pageable, String attribute);
}
