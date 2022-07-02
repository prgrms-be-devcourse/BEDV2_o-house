package com.prgrms.ohouse.domain.community.application;

import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostUpdateCommand;

public interface QuestionPostService {
	@Transactional
	Long createQuestionPost(QuestionPostRegisterCommand command);

	@Transactional
	Long editQuestionPost(QuestionPostUpdateCommand command);

	@Transactional
	void deletePost(Long id);
}