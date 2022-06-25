package com.prgrms.ohouse.domain.common.file;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.ImageAttachable;

public interface FileManager {
	/**
	 *
	 * @param multipartFiles
	 * @param attached
	 * @param <T>
	 * @return
	 */
	<T extends ImageAttachable> List<StoredFile> store(List<MultipartFile> multipartFiles, T attached);

	<T extends ImageAttachable> StoredFile store(MultipartFile multipartFile, T attached);
}
