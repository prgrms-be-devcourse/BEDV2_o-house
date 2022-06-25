package com.prgrms.ohouse.infrastructure.file;

import org.springframework.stereotype.Component;

import com.prgrms.ohouse.domain.common.ImageAttachable;
import com.prgrms.ohouse.domain.common.file.QuestionPostImage;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.community.model.post.question.QuestionPost;
import com.prgrms.ohouse.infrastructure.repository.FileRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class FileRepositoryDelegator {

	private final FileRepository fileRepository;

	public <T extends ImageAttachable> StoredFile save(String fileName, String fileUrl, T attached) {
		return fileRepository.save(StoredFileFactory.storedFileOf(fileName, fileUrl, attached));
	}

	private static class StoredFileFactory {

		public static <T extends ImageAttachable> StoredFile storedFileOf(String fileName, String fileUrl, T attached) {
			if (attached instanceof QuestionPost) {
				return new QuestionPostImage(fileName, fileUrl, (QuestionPost)attached);
			}
			//TODO: 예외 정의
			throw new IllegalArgumentException("이미지 첨부 대상이 된 클래스가 부적합합니다.");
		}
	}
}