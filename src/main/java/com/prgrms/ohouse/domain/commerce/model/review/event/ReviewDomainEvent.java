package com.prgrms.ohouse.domain.commerce.model.review.event;

import com.prgrms.ohouse.domain.common.event.DomainEvent;

import lombok.Getter;

@Getter
public abstract class ReviewDomainEvent extends DomainEvent {
	private Long reviewId;

	protected ReviewDomainEvent(Long reviewId) {
		this.reviewId = reviewId;
	}
}
