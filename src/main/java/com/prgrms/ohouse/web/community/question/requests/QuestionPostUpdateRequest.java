package com.prgrms.ohouse.web.community.question.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class QuestionPostUpdateRequest {

	@NotBlank
	@Size(max = 50)
	private String title;
	@NotBlank
	private String content;
}