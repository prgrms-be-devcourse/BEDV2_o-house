package com.prgrms.ohouse.commerce.infrastructure.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.commerce.domain.application.command.ProductViewMainPageCommand;

public interface JpaProductRepositoryCustom {
	Slice<ProductViewMainPageCommand> findByIdAtDesc(Pageable pageable);
}
