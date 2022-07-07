package com.prgrms.ohouse.domain.community.application;

import com.prgrms.ohouse.domain.community.application.command.QuestionCommentRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionCommentUpdateCommand;

public interface QuestionCommentService {
	Long createQuestionComment(QuestionCommentRegisterCommand command);

	Long editQuestionComment(QuestionCommentUpdateCommand command, Long userId);

	void deleteComment(Long commentId, Long userId);
}