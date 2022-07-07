package com.prgrms.ohouse.domain.commerce.model.review.event;

public class HelpPointIncreaseEvent extends ReviewDomainEvent {
	public HelpPointIncreaseEvent(Long reviewId) {
		super(reviewId);
	}
}
