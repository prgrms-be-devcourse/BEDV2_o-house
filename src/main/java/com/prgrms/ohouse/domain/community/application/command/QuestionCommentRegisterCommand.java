package com.prgrms.ohouse.domain.community.application.command;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public final class QuestionCommentRegisterCommand {

	private final String contents;
	private final Long postId;
	private final MultipartFile multipartFile;

	public QuestionCommentRegisterCommand(String contents, Long postId,
		MultipartFile multipartFile) {
		this.contents = contents;
		this.postId = postId;
		this.multipartFile = multipartFile;
	}
}
