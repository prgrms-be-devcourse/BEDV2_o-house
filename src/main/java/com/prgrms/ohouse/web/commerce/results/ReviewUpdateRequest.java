package com.prgrms.ohouse.web.commerce.results;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.commerce.application.command.ReviewUpdateCommand;

import lombok.Data;

@Data
public class ReviewUpdateRequest {
	private final Long id;
	private final int reviewPoint;
	private final String contents;
	private MultipartFile reviewImage;

	public ReviewUpdateCommand toCommand(MultipartFile reviewImage) {
		return new ReviewUpdateCommand(id, reviewPoint, contents, reviewImage);
	}
}
