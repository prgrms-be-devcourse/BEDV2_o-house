package com.prgrms.ohouse.domain.community.model.post.question;

import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.community.model.post.question.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.model.post.question.command.QuestionPostUpdateCommand;

public interface QuestionPostService {
	@Transactional
	QuestionPost createQuestionPost(QuestionPostRegisterCommand command);

	@Transactional
	void deletePost(Long id);
}