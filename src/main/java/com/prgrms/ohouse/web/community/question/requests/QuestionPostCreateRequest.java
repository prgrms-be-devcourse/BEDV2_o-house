package com.prgrms.ohouse.web.community.question.requests;

import lombok.Getter;

@Getter
public final class QuestionPostCreateRequest {

	private String title;
	private String content;
}