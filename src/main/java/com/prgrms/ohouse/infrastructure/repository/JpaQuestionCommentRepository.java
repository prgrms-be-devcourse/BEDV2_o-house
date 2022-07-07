package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.community.model.question.QuestionComment;
import com.prgrms.ohouse.domain.community.model.question.QuestionCommentRepository;

public interface JpaQuestionCommentRepository extends JpaRepository<QuestionComment, Long>, QuestionCommentRepository {
}
