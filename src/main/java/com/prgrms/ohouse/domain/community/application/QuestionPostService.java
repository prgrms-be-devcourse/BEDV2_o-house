package com.prgrms.ohouse.domain.community.application;

import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;

public interface QuestionPostService {
	@Transactional
	Long createQuestionPost(QuestionPostRegisterCommand command);

	@Transactional
	void deletePost(Long id);
}