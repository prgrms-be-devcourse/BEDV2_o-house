package com.prgrms.ohouse.infrastructure.file;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalFileStore implements FileStore {
	@Value("${file.dir}" )
	private String FILE_DIRECTORY;

	@Override
	public String save(MultipartFile multipartFile, String fileName) {
		try {
			String fileUrl = fullUrlOf(fileName);
			multipartFile.transferTo(new File(fileUrl));
			return fileUrl;
		} catch (IOException e) {
			throw new FileIOException(e.getMessage(), e);
		}
	}

	private String fullUrlOf(String fileName) {
		return FILE_DIRECTORY + fileName;
	}
}