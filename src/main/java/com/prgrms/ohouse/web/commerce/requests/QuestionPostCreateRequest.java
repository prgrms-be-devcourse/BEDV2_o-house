package com.prgrms.ohouse.web.commerce.requests;

import lombok.Getter;

@Getter
public final class QuestionPostCreateRequest {

	public QuestionPostCreateRequest(String content) {
		this.content = content;
	}

	private  String content;
}