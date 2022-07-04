package com.prgrms.ohouse.domain.community.model.question;

import static com.google.common.base.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

	public QuestionPost(String title, String contents) {
		isValidTitle(title);
		this.title = title;
		this.contents = contents;
	}

	public QuestionPost(String title, String contents, List<QuestionPostImage> questionImages, User author) {
		isValidTitle(title);
		this.title = title;
		this.contents = contents;
		this.questionImages = questionImages;
		this.author = author;
	}

	@Id
	@GeneratedValue
	private Long id;

	@Column(nullable = false, length = 50)
	private String title;
	//TODO: 검증
	private String contents;

	//TODO: 컬렉션 getter로 인한 불변성 붕괴 문제 수정
	@OneToMany(mappedBy = "questionPost", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<QuestionPostImage> questionImages = new ArrayList<>();

	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id", nullable = false)
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

	public QuestionPost apply(String title, String contents) {
		this.title = title;
		this.contents = contents;
		return this;
	}

	private void isValidTitle(String title) {
		checkArgument(title.length() >= 1 && title.length() < 30, "제목은 1 ~ 50자 범위여야 합니다.");
	}
}