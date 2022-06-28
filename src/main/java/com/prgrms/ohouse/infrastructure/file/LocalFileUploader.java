package com.prgrms.ohouse.infrastructure.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.file.FileIOException;
import com.prgrms.ohouse.domain.common.file.FileStore;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@PropertySource("classpath:/application-test.yml")
@Getter
@Profile("test")
public class LocalFileUploader implements FileStore {
	@Value("${file.dir}")
	private String fileDir;

	@Override
	public String save(MultipartFile multipartFile, String fileName) throws FileIOException {
		if (multipartFile.isEmpty()) {
			log.debug("file is null");
			return null;
		}
		try {
			String fileUrl = fileDir + fileName;
			multipartFile.transferTo(new File(fileUrl));
			log.debug("file created successful at " + fileDir);
			return fileUrl;
		} catch (IOException e) {
			throw new FileIOException(e.getMessage(), e);
		}
	}

	@Override
	public void delete(String fileUrl) {
		File file = new File(fileUrl);
		if (existByFileUrl(fileUrl)) {
			file.delete();
		}
	}

	public boolean existByFileUrl(String fileUrl) {
		File file = new File(fileUrl);
		return file.exists();
	}

	public void deleteAllFile() throws IOException {
		FileUtils.deleteDirectory(new File(fileDir));
		FileUtils.forceMkdir(new File(fileDir));
	}
}
