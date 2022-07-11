package com.prgrms.ohouse.domain.commerce.model.review.event;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.prgrms.ohouse.domain.commerce.application.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelpPointIncreaseEventListener {
	private final ReviewService reviewService;

	@RabbitListener(queues = "#{eventQueue.name}", concurrency = "1")
	public void handleHelpPointIncreasingEvent(HelpPointIncreaseEvent event) {
		reviewService.increaseHelpPoint(event.getReviewId(), 1);
		log.info("increase help point to review id '" + event.getReviewId() + "' as " + 1);
	}

}
