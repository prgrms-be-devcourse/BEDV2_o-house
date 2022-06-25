package com.prgrms.ohouse.domain.community.model.post.question;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.community.model.post.question.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.infrastructure.repository.QuestionPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionPostServiceImpl implements QuestionPostService {

	//TODO: 리포지터리를 바로 참조하는 것은 헥사고날 아키텍처와 맞지 않아보임
	private final QuestionPostRepository questionRepository;
	private final FileManager fileManager;

	@Transactional
	@Override
	public QuestionPost createQuestionPost(QuestionPostRegisterCommand command) {
		QuestionPost savedPost = questionRepository.save(new QuestionPost(command.getContents()));
		List<StoredFile> questionImages = fileManager.store(command.getMultipartFiles(), savedPost);
		//TODO: 연관관계 설정방식을 재고
		savedPost.setImages(questionImages);
		return savedPost;
	}
}