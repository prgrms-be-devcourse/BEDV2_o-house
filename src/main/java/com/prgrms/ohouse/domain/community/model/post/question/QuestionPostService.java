package com.prgrms.ohouse.domain.community.model.post.question;

import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.community.model.post.question.command.QuestionPostRegisterCommand;

public interface QuestionPostService {
	@Transactional
	QuestionPost createQuestionPost(QuestionPostRegisterCommand command);

	@Transactional
	void deletePost(Long id);
}