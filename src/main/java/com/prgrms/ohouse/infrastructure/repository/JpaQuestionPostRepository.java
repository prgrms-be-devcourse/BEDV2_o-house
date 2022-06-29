package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.community.model.question.QuestionPost;
import com.prgrms.ohouse.domain.community.model.question.QuestionPostRepository;

public interface JpaQuestionPostRepository extends JpaRepository<QuestionPost, Long>, QuestionPostRepository {
}
