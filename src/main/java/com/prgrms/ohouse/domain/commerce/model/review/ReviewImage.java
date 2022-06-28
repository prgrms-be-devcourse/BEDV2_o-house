package com.prgrms.ohouse.domain.commerce.model.review;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.prgrms.ohouse.domain.common.file.StoredFile;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewImage extends StoredFile {

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "review_id", nullable = false)
	private Review review;

	public ReviewImage(String originalFileName, String url, Review review) {
		super(originalFileName, url);
		this.review = review;
	}
}
