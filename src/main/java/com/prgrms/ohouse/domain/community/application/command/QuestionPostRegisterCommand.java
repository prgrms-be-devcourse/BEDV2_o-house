package com.prgrms.ohouse.domain.community.application.command;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;

@Getter
public final class QuestionPostRegisterCommand {

	private final String title;
	private final String contents;
	//TODO: List 불변성 확보
	private final List<MultipartFile> multipartFiles;

	public QuestionPostRegisterCommand(String title, String contents,
		List<MultipartFile> multipartFiles) {
		this.title = title;
		this.contents = contents;
		this.multipartFiles = multipartFiles;
	}
}
