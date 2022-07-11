package com.prgrms.ohouse.domain.common.event;

public interface DomainEventPublisher {
	<T extends DomainEvent> void publish(T event);
}
