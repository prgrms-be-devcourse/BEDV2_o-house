package com.prgrms.ohouse.domain.community.application;

import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostUpdateCommand;

public interface QuestionPostService {
	Long createQuestionPost(QuestionPostRegisterCommand command);

	Long editQuestionPost(QuestionPostUpdateCommand command, Long userId);

	void deletePost(Long postId, Long userId);
}