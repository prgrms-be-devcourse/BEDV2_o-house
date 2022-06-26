package com.prgrms.ohouse.domain.commerce.application;

import org.springframework.data.domain.Pageable;

import com.prgrms.ohouse.domain.commerce.application.command.ProductViewMainPageCommand;
import com.prgrms.ohouse.domain.commerce.application.command.SliceCommand;

public interface ProductService {
	SliceCommand<ProductViewMainPageCommand> findMainPageOrderByCreatedAtDesc(Pageable pageable, String attribute);
}
