package com.prgrms.ohouse.domain.community.application;

import com.prgrms.ohouse.domain.common.file.StoredFile;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ImageResult {
	private String originalFileName;
	private String fileUrl;

	public static ImageResult from(StoredFile image) {
		var result = new ImageResult();
		result.originalFileName = image.getOriginalFileName();
		result.fileUrl = image.getUrl();
		return result;
	}
}
