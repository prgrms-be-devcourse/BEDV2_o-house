package com.prgrms.ohouse.domain.commerce.application;

import java.util.List;

import com.prgrms.ohouse.domain.commerce.application.command.OrderAddCommand;
import com.prgrms.ohouse.domain.commerce.application.command.OrderViewCommand;
import com.prgrms.ohouse.web.commerce.results.OrderAddResult;
import com.prgrms.ohouse.web.commerce.results.OrderViewItemResult;

public interface OrderService {
	OrderAddResult addOrder(OrderAddCommand orderAddCommand);

	List<OrderViewItemResult> viewAllOrder(OrderViewCommand orderViewCommand);
}
