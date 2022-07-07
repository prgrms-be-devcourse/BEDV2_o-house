package com.prgrms.ohouse.domain.community.model.question;

import java.util.Optional;

public interface QuestionCommentRepository {
	QuestionComment save(QuestionComment questionComment);

	Optional<QuestionComment> findById(Long commentId);

	void deleteById(Long id);
}
