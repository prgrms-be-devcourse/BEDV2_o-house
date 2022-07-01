package com.prgrms.ohouse.web.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public final class QuestionPostCreateRequest {

	public QuestionPostCreateRequest(String content) {
		this.content = content;
	}

	private String content;
}