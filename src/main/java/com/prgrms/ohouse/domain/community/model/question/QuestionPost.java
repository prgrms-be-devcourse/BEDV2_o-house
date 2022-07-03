package com.prgrms.ohouse.domain.community.model.question;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.data.annotation.CreatedBy;

import com.prgrms.ohouse.domain.common.BaseTimeEntity;
import com.prgrms.ohouse.domain.user.model.UserAuditorAware;
import com.prgrms.ohouse.domain.common.file.ImageAttachable;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@EntityListeners(UserAuditorAware.class)
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionPost extends BaseTimeEntity implements ImageAttachable {

	public QuestionPost(String content) {
		this.content = content;
	}

	public QuestionPost(String content, List<QuestionPostImage> questionImages) {
		this.content = content;
		this.questionImages = questionImages;
	}

	public QuestionPost(Long id, String content,
		List<QuestionPostImage> questionImages) {
		this.id = id;
		this.content = content;
		this.questionImages = questionImages;
	}

	public QuestionPost(String content,
		List<QuestionPostImage> questionImages, User author) {
		this.content = content;
		this.questionImages = questionImages;
		this.author = author;
	}

	@Id
	@GeneratedValue
	private Long id;

	//TODO: 검증
	private String content;

	//TODO: 컬렉션 getter로 인한 불변성 붕괴 문제 수정
	@OneToMany(mappedBy = "questionPost", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QuestionPostImage> questionImages = new ArrayList<>();

	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private User author;

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		QuestionPostImage image = new QuestionPostImage(fileName, fileUrl, this);
		questionImages.add(image);
		return image;
	}

	@Override
	public void removeCurrentImage() {
		this.questionImages.clear();
	}

	public QuestionPost apply(String content) {
		this.content = content;
		return this;
	}
}