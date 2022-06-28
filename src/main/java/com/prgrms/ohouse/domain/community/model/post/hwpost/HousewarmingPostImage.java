package com.prgrms.ohouse.domain.community.model.post.hwpost;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.prgrms.ohouse.domain.common.file.StoredFile;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "hwpost_image")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class HousewarmingPostImage extends StoredFile {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "hwpost_id", nullable = false)
	private HousewarmingPost housewarmingPost;

	public HousewarmingPostImage(String fileName, String fileUrl, HousewarmingPost housewarmingPost) {
		super(fileName, fileUrl);
		this.housewarmingPost = housewarmingPost;
	}
}
