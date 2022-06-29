package com.prgrms.ohouse.domain.community.application.command;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public final class QuestionPostRegisterCommand {

	private final String contents;
	//TODO: List 불변성 확보
	private final List<MultipartFile> multipartFiles;

	public QuestionPostRegisterCommand(String contents,
		List<MultipartFile> multipartFiles) {
		this.contents = contents;
		this.multipartFiles = multipartFiles;
	}
}
