package com.prgrms.ohouse.domain.common.file;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import com.prgrms.ohouse.infrastructure.file.LocalFileUploader;

@SpringBootTest(properties = "spring.profiles.active:test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocalFileUploadTest {

	@Autowired
	private LocalFileUploader fileUploader;

	@AfterAll
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

		Assertions.assertDoesNotThrow(() -> {
			String path = fileUploader.save(file, "testfile");
			assertThat(path).isEqualTo("http://localhost:8080/testfile");
		});
	}
}