package com.prgrms.ohouse.domain.commerce.model.review;

import static com.google.common.base.Preconditions.*;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.common.ImageAttachable;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Review implements ImageAttachable {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	private User user;
	private int reviewPoint = 0;
	private String contents;
	private int helpPoint = 0;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "review", fetch = FetchType.LAZY)
	private ReviewImage reviewImage;
	private LocalDateTime createdAt;
	private ReviewType reviewType = ReviewType.NORMAL;

	public static Review createReview(Product product, User user, int reviewPoint, String contents) {
		Review instance = new Review();
		instance.setProduct(product);
		instance.setUser(user);
		instance.setReviewPoint(reviewPoint);
		instance.setContents(contents);
		instance.setCreatedAt(LocalDateTime.now());
		return instance;
	}

	public void assignReviewImage(ReviewImage reviewImage) {
		setReviewType(ReviewType.PHOTO);
		setReviewImage(reviewImage);
	}

	public void increaseHelpPoint(int value) {
		this.helpPoint += value;
	}

	private void validReviewPoint(int reviewPoint) {
		checkArgument(reviewPoint >= 1 && reviewPoint <= 5, "invalid review point range");
	}

	private void validContentLength(String contents) {
		checkArgument(contents.length() >= 20, "too short contents for review");
	}

	private void setReviewPoint(int reviewPoint) {
		validReviewPoint(reviewPoint);
		this.reviewPoint = reviewPoint;
	}

	private void setContents(String contents) {
		validContentLength(contents);
		this.contents = contents;
	}
}

