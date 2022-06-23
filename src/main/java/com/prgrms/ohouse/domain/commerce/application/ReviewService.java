package com.prgrms.ohouse.domain.commerce.application;

import com.prgrms.ohouse.domain.commerce.application.commands.ReviewRegisterCommand;

public interface ReviewService {

	Long registerReview(ReviewRegisterCommand command);
}
