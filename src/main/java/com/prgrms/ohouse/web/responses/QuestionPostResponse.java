package com.prgrms.ohouse.web.responses;

import java.util.List;

import com.prgrms.ohouse.domain.community.model.post.question.QuestionPostImage;
import com.prgrms.ohouse.domain.community.model.post.question.QuestionPost;

import lombok.Getter;

@Getter
public final class QuestionPostResponse {

	public QuestionPostResponse(Long id, String content,
		List<QuestionPostImage> questionImages) {
		this.id = id;
		this.content = content;
		this.questionImages = questionImages;
	}

	private final Long id;
	private final String content;
	private final List<QuestionPostImage> questionImages;

	public static QuestionPostResponse of(QuestionPost questionPost) {
		return new QuestionPostResponse(questionPost.getId(), questionPost.getContent(), questionPost.getQuestionImages());
	}
}