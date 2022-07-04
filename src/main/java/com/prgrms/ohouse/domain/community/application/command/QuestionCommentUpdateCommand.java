package com.prgrms.ohouse.domain.community.application.command;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public final class QuestionCommentUpdateCommand {

	private final Long commentId;
	private final String contents;
	private final Long postId;
	private final MultipartFile multipartFile;

	public QuestionCommentUpdateCommand(Long commentId, String contents, Long postId,
		MultipartFile multipartFile) {
		this.commentId = commentId;
		this.contents = contents;
		this.postId = postId;
		this.multipartFile = multipartFile;
	}
}
