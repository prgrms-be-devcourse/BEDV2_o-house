package com.prgrms.ohouse.web.commerce.api;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.prgrms.ohouse.domain.commerce.application.OrderService;
import com.prgrms.ohouse.domain.commerce.application.command.OrderAddCommand;
import com.prgrms.ohouse.domain.commerce.application.command.OrderViewCommand;
import com.prgrms.ohouse.domain.common.security.AuthUtility;
import com.prgrms.ohouse.web.commerce.requests.OrderAddRequest;
import com.prgrms.ohouse.web.commerce.results.OrderAddResult;
import com.prgrms.ohouse.web.commerce.results.OrderViewItemResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class RestOrderController {
	private final OrderService orderService;
	private final AuthUtility authUtility;

	@PostMapping("/api/v0/order")
	public ResponseEntity<OrderAddResult> addNewOrder(@RequestBody OrderAddRequest orderAddRequest) {
		OrderAddCommand orderAddCommand = new OrderAddCommand(orderAddRequest);
		return ResponseEntity.ok().body(orderService.addOrder(orderAddCommand));
	}

	@GetMapping("/api/v0/order")
	public ResponseEntity<List<OrderViewItemResult>> viewAllOrder() {
		OrderViewCommand orderViewCommand = new OrderViewCommand(authUtility.getAuthUser().getId());
		return ResponseEntity.ok().body(orderService.viewAllOrder(orderViewCommand));
	}
}
