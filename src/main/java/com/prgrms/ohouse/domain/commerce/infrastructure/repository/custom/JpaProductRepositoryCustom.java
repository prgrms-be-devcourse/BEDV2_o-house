package com.prgrms.ohouse.domain.commerce.infrastructure.repository.custom;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.prgrms.ohouse.domain.commerce.domain.application.command.ProductViewMainPageCommand;

public interface JpaProductRepositoryCustom {
	Slice<ProductViewMainPageCommand> findByIdAtDesc(Pageable pageable);
}
