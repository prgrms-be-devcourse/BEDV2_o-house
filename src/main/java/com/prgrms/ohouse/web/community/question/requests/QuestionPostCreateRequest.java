package com.prgrms.ohouse.web.community.question.requests;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class QuestionPostCreateRequest {

	public QuestionPostCreateRequest(String title, String content) {
		this.title = title;
		this.content = content;
	}

	@NotBlank
	@Size(max = 50)
	private String title;
	@NotBlank
	private String content;
}