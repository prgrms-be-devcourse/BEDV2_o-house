package com.prgrms.ohouse.domain.common.file;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.prgrms.ohouse.infrastructure.file.LocalFileUploader;

@SpringBootTest(properties = "spring.profiles.active:test")
class LocalFileUploadTest {

	@Autowired
	private LocalFileUploader fileUploader;

	@AfterEach
	private void deleteAllFile() throws IOException {
		fileUploader.deleteAllFile();
	}

	@DisplayName("로컬서버 파일 업로드 테스트")
	@Test
	void testFileUpload() {
		MockMultipartFile file = new MockMultipartFile(
			"thumbnail",
			"test.png",
			"image/png",
			"<<png data>>".getBytes());

		assertDoesNotThrow(() -> {
			String path = fileUploader.save(file, "testfile");
			assertThat(path).isEqualTo("src/test/resources/static/testfile");
			assertTrue(fileUploader.existByFileUrl(path));
		});

	}

	@DisplayName("로컬서버 파일 삭제 테스트")
	@Test
	void testFileDelete() {
		MockMultipartFile file = new MockMultipartFile(
			"thumbnail",
			"test.png",
			"image/png",
			"<<png data>>".getBytes());
		String path = fileUploader.save(file, "testfile");

		fileUploader.delete(path);

		assertFalse(fileUploader.existByFileUrl(path));
	}
}