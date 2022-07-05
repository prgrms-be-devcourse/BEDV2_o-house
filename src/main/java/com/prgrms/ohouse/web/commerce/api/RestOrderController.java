package com.prgrms.ohouse.web.commerce.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.commerce.application.OrderService;
import com.prgrms.ohouse.domain.commerce.application.command.OrderAddCommand;
import com.prgrms.ohouse.web.commerce.requests.OrderAddRequest;
import com.prgrms.ohouse.web.commerce.results.OrderAddResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestOrderController {
	private final OrderService orderService;

	@PostMapping("/api/v0/order")
	public ResponseEntity<OrderAddResult> addNewOrder(@RequestBody OrderAddRequest orderAddRequest) {
		OrderAddCommand orderAddCommand = new OrderAddCommand(orderAddRequest);
		return ResponseEntity.ok().body(orderService.addOrder(orderAddCommand));
	}
}
