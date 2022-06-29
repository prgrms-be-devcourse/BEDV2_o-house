package com.prgrms.ohouse.domain.community.model.question;

public interface QuestionPostRepository {
	QuestionPost save(QuestionPost questionPost);

	void deleteById(Long id);
}
