package com.prgrms.ohouse.domain.commerce.application.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderViewCommand {
	Long userId;

	public OrderViewCommand(Long userId) {
		this.userId = userId;
	}
}
