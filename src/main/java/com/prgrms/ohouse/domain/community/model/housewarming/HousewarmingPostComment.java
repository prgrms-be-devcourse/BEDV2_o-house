package com.prgrms.ohouse.domain.community.model.housewarming;

import static com.google.common.base.Preconditions.*;

import javax.annotation.Nonnull;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedBy;

import com.prgrms.ohouse.domain.common.BaseTimeEntity;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hwpost_comment")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class HousewarmingPostComment extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(nullable = false)
	private String comment;

	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private User author;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private HousewarmingPost hwPost;

	public HousewarmingPostComment(@Nonnull String comment, @Nonnull HousewarmingPost hwPost) {
		updateContent(comment);
		checkNotNull(hwPost);
		this.hwPost = hwPost;
	}

	public void updateContent(String comment) {
		checkArgument(comment.length() > 0, "댓글은 최소 1 이상의 길이를 가져야 합니다.");
		this.comment = comment;
	}

}
