package com.prgrms.ohouse.commerce.domain.application;

import org.springframework.data.domain.Pageable;

import com.prgrms.ohouse.commerce.domain.application.command.ProductViewMainPageCommand;
import com.prgrms.ohouse.commerce.domain.application.command.SliceCommand;

public interface ProductService {
	SliceCommand<ProductViewMainPageCommand> findMainPageOrderByCreatedAtDesc(Pageable pageable, String attribute);
}
