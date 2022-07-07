package com.prgrms.ohouse.domain.community.model.question;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.prgrms.ohouse.domain.common.file.StoredFile;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionCommentImage extends StoredFile {

	public QuestionCommentImage(String originalFileName, String url, QuestionComment questionComment) {
		super(originalFileName, url);
		this.questionComment = questionComment;
	}

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "comment_id", nullable = false)
	private QuestionComment questionComment;
}
