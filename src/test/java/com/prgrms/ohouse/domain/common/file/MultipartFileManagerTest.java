package com.prgrms.ohouse.domain.common.file;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

class MultipartFileManagerTest {

	static final FileStore FILE_STORE_MOCK = mock(FileStore.class);
	static final FileRepository FILE_REPOSITORY_MOCK = mock(FileRepository.class);
	static final MultipartFile MULTIPART_FILE_MOCK = mock(MultipartFile.class);
	static final ImageAttachable IMAGE_ATTACHABLE_MOCK = mock(ImageAttachable.class);

	FileManager fileManager = new MultipartFileManager(FILE_STORE_MOCK, FILE_REPOSITORY_MOCK);

	@BeforeEach
	void resetMock() {
		reset(FILE_STORE_MOCK, FILE_REPOSITORY_MOCK, MULTIPART_FILE_MOCK, IMAGE_ATTACHABLE_MOCK);
	}

	@Test
	@DisplayName("파일이 비어 있을 경우 파일 저장 로직이 실행되지 않음")
	void dontStoreIfFileIsEmpty() {
		when(MULTIPART_FILE_MOCK.getOriginalFilename()).thenReturn("filename.png");
		when(MULTIPART_FILE_MOCK.isEmpty()).thenReturn(true);
		fileManager.store(MULTIPART_FILE_MOCK, IMAGE_ATTACHABLE_MOCK);
		verify(FILE_STORE_MOCK, never()).save(any(), anyString());
		verify(FILE_REPOSITORY_MOCK, never()).save(any());
	}

	@Test
	@DisplayName("파일이 null일 경우 파일 저장 로직이 실행되지 않음")
	void dontStoreIfFileIsNull() {
		when(MULTIPART_FILE_MOCK.getOriginalFilename()).thenReturn("filename.png");
		MultipartFile nullFile = null;
		fileManager.store(nullFile, IMAGE_ATTACHABLE_MOCK);
		verify(FILE_STORE_MOCK, never()).save(any(), anyString());
		verify(FILE_REPOSITORY_MOCK, never()).save(any());
	}

	@Test
	@DisplayName("부적절한 확장자의 경우 저장을 지원하지 않음")
	void dontStoreIfFileHasIllegalExtension() {
		when(MULTIPART_FILE_MOCK.getOriginalFilename()).thenReturn("filename.trash");
		assertThrows(IllegalFileExtensionException.class,
			() -> fileManager.store(MULTIPART_FILE_MOCK, IMAGE_ATTACHABLE_MOCK));

		verify(FILE_STORE_MOCK, never()).save(any(), anyString());
		verify(FILE_REPOSITORY_MOCK, never()).save(any());
	}
}