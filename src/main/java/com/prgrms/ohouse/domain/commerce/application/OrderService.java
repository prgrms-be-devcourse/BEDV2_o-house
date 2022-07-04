package com.prgrms.ohouse.domain.commerce.application;

import com.prgrms.ohouse.domain.commerce.application.command.OrderAddCommand;
import com.prgrms.ohouse.web.commerce.results.OrderAddResult;

public interface OrderService {
	OrderAddResult addOrder(OrderAddCommand orderAddCommand);
}
