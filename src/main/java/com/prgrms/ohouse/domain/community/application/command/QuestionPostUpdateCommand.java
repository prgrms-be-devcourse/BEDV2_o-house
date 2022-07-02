package com.prgrms.ohouse.domain.community.application.command;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostImage;

import lombok.Getter;

@Getter
public final class QuestionPostUpdateCommand {

	private final Long id;
	private final String contents;
	private final List<MultipartFile> multipartFiles;

	public QuestionPostUpdateCommand(Long id, String contents,
		List<MultipartFile> multipartFiles) {
		this.id = id;
		this.contents = contents;
		this.multipartFiles = multipartFiles;
	}
}
