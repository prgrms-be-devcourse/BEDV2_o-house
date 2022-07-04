package com.prgrms.ohouse.web.requests;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public final class QuestionPostUpdateRequest {

	public QuestionPostUpdateRequest(Long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	private Long id;
	private String title;
	private String content;
}