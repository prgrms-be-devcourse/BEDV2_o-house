package com.prgrms.ohouse.domain.commerce.application;

import com.prgrms.ohouse.domain.commerce.application.command.CartCreateCommand;
import com.prgrms.ohouse.web.commerce.results.CartCreateResult;

public interface CartService {
	CartCreateResult insertCartItem(CartCreateCommand cartCreateCommand);
}
