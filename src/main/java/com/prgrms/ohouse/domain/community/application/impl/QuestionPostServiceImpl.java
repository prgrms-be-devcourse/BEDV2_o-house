package com.prgrms.ohouse.domain.community.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.community.application.QuestionPostService;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionPostServiceImpl implements QuestionPostService {

	//TODO: 리포지터리를 바로 참조하는 것은 헥사고날 아키텍처와 맞지 않아보임
	private final QuestionPostRepository questionPostRepository;
	private final FileManager fileManager;

	@Transactional
	@Override
	public Long createQuestionPost(QuestionPostRegisterCommand command) {
		QuestionPost savedPost = questionPostRepository.save(new QuestionPost(command.getContents()));
		fileManager.store(command.getMultipartFiles(), savedPost);
		return savedPost.getId();
	}

	@Transactional
	@Override
	public void deletePost(Long id) {
		questionPostRepository.deleteById(id);
	}
}