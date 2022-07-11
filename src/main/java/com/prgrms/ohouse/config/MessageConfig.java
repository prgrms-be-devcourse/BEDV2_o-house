package com.prgrms.ohouse.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {
	@Bean
	public FanoutExchange domainEventsExchange() {
		return new FanoutExchange("ta.domain.events", true, false);
	}

	@Bean
	public Queue eventQueue() {
		return new Queue("ta.event", true);
	}

	@Bean
	public Binding binding(FanoutExchange exchange, Queue queue) {
		return BindingBuilder.bind(eventQueue()).to(exchange);
	}
}
