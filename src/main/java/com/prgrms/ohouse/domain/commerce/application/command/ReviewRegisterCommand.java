package com.prgrms.ohouse.domain.commerce.application.command;

import static com.google.common.base.Preconditions.*;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public class ReviewRegisterCommand {
	private final Long productId;
	private final Long userId;
	private final int reviewPoint;
	private final String contents;
	private MultipartFile reviewImage;

	public ReviewRegisterCommand(Long productId, Long userId, int reviewPoint, String contents,
		MultipartFile reviewImage) {
		checkArgument(reviewPoint > 0 && reviewPoint <= 5, "invalid review point range");
		checkArgument(contents.length() >= 20, "too short contents for review");
		this.productId = productId;
		this.userId = userId;
		this.reviewPoint = reviewPoint;
		this.contents = contents;
		if (reviewImage != null) {
			this.reviewImage = reviewImage;
		}
	}

	public boolean isPhotoReview() {
		return this.reviewImage != null;
	}
}
