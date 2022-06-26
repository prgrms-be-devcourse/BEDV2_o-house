package com.prgrms.ohouse.domain.common.file;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.ImageAttachable;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class MultipartFileManager implements FileManager {

	private final FileStore fileStore;
	private final FileRepositoryDelegator fileRepositoryDelegator;

	private static final String[] SUPPORTING_EXTENSIONS = {
		"png", "jpg"
	};

	@Override
	public <T extends ImageAttachable> List<StoredFile> store(List<MultipartFile> multipartFiles, T attached) {
		//TODO: 파일 최대 첨부 가능 수에 기반하여 ArrayList의 크기 설정하기
		List<StoredFile> storedFiles = new ArrayList<>();
		for (MultipartFile multipartFile : multipartFiles) {
			storedFiles.add(store(multipartFile, attached));
		}
		return storedFiles;
	}

	@Override
	public <T extends ImageAttachable> StoredFile store(MultipartFile multipartFile, T attached) {
		if (multipartFile.isEmpty()) {
			return null;
		}

		String originalFilename = multipartFile.getOriginalFilename();
		String storedFileName = generateFileNameOf(originalFilename);

		String fileUrl = fileStore.save(multipartFile, storedFileName);
		StoredFile savedFile = fileRepositoryDelegator.save(originalFilename, fileUrl, attached);

		return savedFile;
	}

	private String generateFileNameOf(String originalFilename) {
		String uuid = UUID.randomUUID().toString();
		String extension = extractExtensionFrom(originalFilename);
		return uuid + "." + extension;
	}

	private String extractExtensionFrom(String originalFilename) {
		int location = originalFilename.lastIndexOf(".");
		String extension = originalFilename.substring(location + 1);
		if (isSupportedExtension(extension)) {
			return extension;
		}
		throw new IllegalFileExtensionException(extension);
	}

	private boolean isSupportedExtension(String extension) {
		for (String imageExtension : SUPPORTING_EXTENSIONS) {
			if (extension.equalsIgnoreCase(imageExtension)) {
				return true;
			}
		}
		return false;
	}
}