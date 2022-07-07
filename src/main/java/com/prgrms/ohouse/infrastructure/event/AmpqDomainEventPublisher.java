package com.prgrms.ohouse.infrastructure.event;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.prgrms.ohouse.domain.common.event.DomainEvent;
import com.prgrms.ohouse.domain.common.event.DomainEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class AmpqDomainEventPublisher implements DomainEventPublisher {
	private final RabbitTemplate rabbitTemplate;
	private final FanoutExchange exchange;

	@Override
	public <T extends DomainEvent> void publish(T event) {
		log.debug("publishing domain event");
		try {
			rabbitTemplate.convertAndSend(exchange.getName(), "", event);

		} catch (AmqpException e) {
			log.error(e.getMessage(), e);
		}
	}

}
