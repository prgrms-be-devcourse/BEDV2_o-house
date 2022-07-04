package com.prgrms.ohouse.web.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public final class QuestionPostCreateRequest {

	public QuestionPostCreateRequest(String title, String content) {
		this.title = title;
		this.content = content;
	}

	private String title;
	private String content;
}