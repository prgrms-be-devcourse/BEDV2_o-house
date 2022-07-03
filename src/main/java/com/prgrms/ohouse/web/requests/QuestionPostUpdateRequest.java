package com.prgrms.ohouse.web.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public final class QuestionPostUpdateRequest {

	public QuestionPostUpdateRequest(Long id, String content) {
		this.id = id;
		this.content = content;
	}

	private Long id;
	private String content;
}