package com.prgrms.ohouse.infrastructure.file;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.prgrms.ohouse.domain.common.file.FileUploader;
import com.prgrms.ohouse.domain.common.file.UploadFile;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@PropertySource("classpath:/application-${spring.profiles.active}.yml")
@Getter
public class LocalFileUploader implements FileUploader {
	@Value("${file.dir}")
	private String fileDir;
	@Value("${app.host}")
	private String host;

	@Override
	public UploadFile upload(MultipartFile multipartFile, String originName, String newName) throws IOException {
		if (multipartFile.isEmpty()) {
			log.info("file is null");
			return null;
		}
		multipartFile.transferTo(new File(fileDir + newName));
		log.info("file created successful at " + fileDir);
		return new UploadFile(newName, host + newName);
	}

	@Override
	public void deleteFile(String fileName) {
		File file = new File(fileDir + fileName);
		if (existByFileName(fileName)) {
			file.delete();
		}
	}

	@Override
	public boolean existByFileName(String fileName) {
		File file = new File(fileDir + fileName);
		return file.exists();
	}

	public void deleteAllFile() throws IOException {
		FileUtils.deleteDirectory(new File(fileDir));
		FileUtils.forceMkdir(new File(fileDir));
	}
}
