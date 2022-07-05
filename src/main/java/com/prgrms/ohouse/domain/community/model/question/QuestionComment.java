package com.prgrms.ohouse.domain.community.model.question;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedBy;

import com.prgrms.ohouse.domain.common.BaseTimeEntity;
import com.prgrms.ohouse.domain.common.file.ImageAttachable;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.user.model.User;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(UserAuditorAware.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionComment extends BaseTimeEntity implements ImageAttachable {

	public QuestionComment(String content, QuestionPost questionPost) {
		this.contents = content;
		this.questionPost = questionPost;
	}

	@Id
	@GeneratedValue
	private Long id;

	//TODO: 검증
	@Column(nullable = false)
	private String contents;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false, updatable = false)
	private QuestionPost questionPost;

	@OneToOne(mappedBy = "questionComment", cascade = CascadeType.ALL, orphanRemoval = true)
	private QuestionCommentImage commentImage;

	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false, updatable = false)
	private User author;

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		QuestionCommentImage image = new QuestionCommentImage(fileName, fileUrl, this);
		commentImage = image;
		return image;
	}

	@Override
	public void removeCurrentImage() {
		this.commentImage = null;
	}

	public void apply(String content) {
		this.contents = content;
	}
}
