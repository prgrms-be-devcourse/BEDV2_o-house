package com.prgrms.ohouse.domain.community.model.post.question;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.community.model.post.question.QuestionPost;

public interface QuestionPostRepository extends JpaRepository<QuestionPost, Long> {
}
