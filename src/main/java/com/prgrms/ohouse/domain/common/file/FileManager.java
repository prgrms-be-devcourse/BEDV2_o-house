package com.prgrms.ohouse.domain.common.file;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface FileManager {
	<T extends ImageAttachable> List<StoredFile> store(List<MultipartFile> multipartFiles, T attached);

	<T extends ImageAttachable> StoredFile store(MultipartFile multipartFile, T attached);

	<T extends StoredFile> void delete(List<T> files);

	<T extends StoredFile> void delete(T file);
}
