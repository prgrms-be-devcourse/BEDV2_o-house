package com.prgrms.ohouse.domain.common.file;

import lombok.Getter;

@Getter
public class UploadFile {
	private final String uploadFileName;
	private final String uploadFileUrl;

	public UploadFile(String uploadFileName, String uploadFileUrl) {
		this.uploadFileName = uploadFileName;
		this.uploadFileUrl = uploadFileUrl;
	}
}
