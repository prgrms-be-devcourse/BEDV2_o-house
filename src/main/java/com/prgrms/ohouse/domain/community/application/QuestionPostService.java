package com.prgrms.ohouse.domain.community.application;

import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostUpdateCommand;

public interface QuestionPostService {
	Long createQuestionPost(QuestionPostRegisterCommand command);

	Long editQuestionPost(QuestionPostUpdateCommand command);

	void deletePost(Long id);
}