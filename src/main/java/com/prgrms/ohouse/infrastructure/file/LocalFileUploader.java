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
	@Value("${app.host}")
	private String host;

	@Override
	public String save(MultipartFile multipartFile, String fileName) throws FileIOException {
		if (multipartFile.isEmpty()) {
			log.debug("file is null");
			return null;
		}
		try {
			multipartFile.transferTo(new File(fileDir + fileName));
		} catch (IOException e) {
			throw new FileIOException(e.getMessage(), e);
		}
		log.debug("file created successful at " + fileDir);
		return host + fileName;
	}


	@Override
	public void delete(String fileUrl) {
		File file = new File(fileUrl);
		if (existByFileName(fileUrl)) {
			file.delete();
		}
	}

	private boolean existByFileName(String fileName) {
		File file = new File(fileDir + fileName);
		return file.exists();
	}

	public void deleteAllFile() throws IOException {
		FileUtils.deleteDirectory(new File(fileDir));
		FileUtils.forceMkdir(new File(fileDir));
	}
}
