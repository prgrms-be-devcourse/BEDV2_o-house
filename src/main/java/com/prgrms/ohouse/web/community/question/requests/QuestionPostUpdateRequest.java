package com.prgrms.ohouse.web.community.question.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public final class QuestionPostUpdateRequest {

	private String title;
	private String content;
}