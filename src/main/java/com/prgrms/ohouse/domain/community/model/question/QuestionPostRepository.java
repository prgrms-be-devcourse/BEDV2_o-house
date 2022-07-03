package com.prgrms.ohouse.domain.community.model.question;

import java.util.Optional;

public interface QuestionPostRepository {
	QuestionPost save(QuestionPost questionPost);

	void deleteById(Long id);

	Optional<QuestionPost> findById(Long id);
}