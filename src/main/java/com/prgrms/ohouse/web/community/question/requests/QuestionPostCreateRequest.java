package com.prgrms.ohouse.web.community.question.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public final class QuestionPostCreateRequest {

	public QuestionPostCreateRequest(String title, String content) {
		this.title = title;
		this.content = content;
	}

	private String title;
	private String content;
}