package com.prgrms.ohouse.web.commerce.results;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.commerce.application.command.ReviewUpdateCommand;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReviewUpdateRequest {
	private Long id;
	private int reviewPoint;
	private String contents;

	public ReviewUpdateRequest(Long id, int reviewPoint, String contents) {
		this.id = id;
		this.reviewPoint = reviewPoint;
		this.contents = contents;
	}

	public ReviewUpdateCommand toCommand(MultipartFile reviewImage) {
		return new ReviewUpdateCommand(id, reviewPoint, contents, reviewImage);
	}
}
