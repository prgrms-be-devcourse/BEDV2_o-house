package com.prgrms.ohouse.domain.community.application.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.common.file.FileManager;
import com.prgrms.ohouse.domain.community.application.QuestionPostService;
import com.prgrms.ohouse.domain.community.application.UnauthorizedContentAccessException;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostRegisterCommand;
import com.prgrms.ohouse.domain.community.application.command.QuestionPostUpdateCommand;
import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuestionPostServiceImpl implements QuestionPostService {

	private final QuestionPostRepository postRepository;
	private final FileManager fileManager;

	@Transactional
	@Override
	public Long createQuestionPost(QuestionPostRegisterCommand command) {
		QuestionPost savedPost = postRepository.save(new QuestionPost(command.getTitle(), command.getContents()));
		fileManager.store(command.getMultipartFiles(), savedPost);
		return savedPost.getId();
	}

	@Transactional
	@Override
	public Long editQuestionPost(QuestionPostUpdateCommand command, Long userId) {
		QuestionPost post = getAuthorized(command.getId(), userId);
		fileManager.delete(post.getQuestionImages(), post);
		fileManager.store(command.getMultipartFiles(), post);

		post.apply(command.getTitle(), command.getContents());
		return post.getId();
	}

	@Transactional
	@Override
	public void deletePost(Long postId, Long userId) {
		QuestionPost post = getAuthorized(postId, userId);
		fileManager.delete(post.getQuestionImages(), post);
		postRepository.deleteById(postId);
	}

	private QuestionPost getAuthorized(Long postId, Long userId) {
		QuestionPost post = postRepository.findById(postId).orElseThrow();
		if (post.getAuthor().getId() != userId) {
			throw new UnauthorizedContentAccessException(postId, userId);
		}
		return post;
	}
}