package com.prgrms.ohouse.domain.community.model.housewarming;

import java.text.MessageFormat;

import lombok.Getter;

@Getter
public class InvalidContentFormatException extends RuntimeException {
	private final int imageFileCount;
	private final int contentImageCount;

	public InvalidContentFormatException(int imageFileCount, int matchedSequenceCount) {
		super(
			MessageFormat.format(
				"첨부한 이미지 개수({0})와 게시물 컨텐츠에 배치될 이미지의 개수({1})가 일치하지 않습니다.",
				imageFileCount,
				matchedSequenceCount));
		this.imageFileCount = imageFileCount;
		this.contentImageCount = matchedSequenceCount;
	}
}
