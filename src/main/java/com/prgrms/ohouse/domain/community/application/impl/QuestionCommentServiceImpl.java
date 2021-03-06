package com.prgrms.ohouse.domain.community.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.community.application.QuestionCommentService;
import com.prgrms.ohouse.domain.community.application.UnauthorizedContentAccessException;
import com.prgrms.ohouse.domain.community.application.command.QuestionCommentRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionCommentUpdateCommand;
import com.prgrms.ohouse.domain.community.model.question.QuestionComment;
import com.prgrms.ohouse.domain.community.model.question.QuestionCommentRepository;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionCommentServiceImpl implements QuestionCommentService {

	private final QuestionPostRepository postRepository;
	private final QuestionCommentRepository commentRepository;
	private final FileManager fileManager;

	@Transactional
	@Override
	public Long createQuestionComment(QuestionCommentRegisterCommand command) {
		QuestionPost post = postRepository.findById(command.getPostId()).orElseThrow();
		QuestionComment savedComment = commentRepository.save(new QuestionComment(command.getContents(), post));
		fileManager.store(command.getMultipartFile(), savedComment);
		return savedComment.getId();
	}

	@Transactional
	@Override
	public Long editQuestionComment(QuestionCommentUpdateCommand command, Long userId) {
		QuestionComment comment = getAuthorized(command.getCommentId(), userId);
		fileManager.delete(comment.getCommentImage(), comment);
		fileManager.store(command.getMultipartFile(), comment);

		comment.apply(command.getContents());
		return comment.getId();
	}

	@Transactional
	@Override
	public void deleteComment(Long commentId, Long userId) {
		QuestionComment comment = getAuthorized(commentId, userId);
		fileManager.delete(comment.getCommentImage(), comment);
		commentRepository.deleteById(commentId);
	}

	private QuestionComment getAuthorized(Long commentId, Long userId) {
		QuestionComment comment = commentRepository.findById(commentId).orElseThrow();
		if (comment.getAuthor().getId() != userId) {
			throw new UnauthorizedContentAccessException(commentId, userId);
		}
		return comment;
	}
}