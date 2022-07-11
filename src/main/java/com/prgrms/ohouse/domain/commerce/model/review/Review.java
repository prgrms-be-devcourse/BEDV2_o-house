package com.prgrms.ohouse.domain.commerce.model.review;

import static com.google.common.base.Preconditions.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.springframework.data.annotation.CreatedBy;

import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.common.file.ImageAttachable;
import com.prgrms.ohouse.domain.common.file.StoredFile;
import com.prgrms.ohouse.domain.user.model.BaseEntity;
import com.prgrms.ohouse.domain.user.model.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter(AccessLevel.PRIVATE)
@Getter
public class Review extends BaseEntity implements ImageAttachable {
	@Id
	@GeneratedValue
	private Long id;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id")
	private Product product;
	@CreatedBy
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	private int reviewPoint = 0;
	private String contents;
	private int helpPoint = 0;
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "review", fetch = FetchType.LAZY)
	private ReviewImage reviewImage;
	private ReviewType reviewType = ReviewType.NORMAL;

	public static Review createReview(Product product, User user, int reviewPoint, String contents) {
		Review instance = new Review();
		instance.setProduct(product);
		instance.setReviewPoint(reviewPoint);
		instance.setContents(contents);
		instance.setUser(user);
		return instance;
	}

	public void increaseHelpPoint(int value) {
		this.helpPoint += value;
	}

	public void modifyReview(int reviewPoint, String contents) {
		setReviewPoint(reviewPoint);
		setContents(contents);
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

	@Override
	public StoredFile attach(String fileName, String fileUrl) {
		ReviewImage image = new ReviewImage(fileName, fileUrl, this);
		setReviewType(ReviewType.PHOTO);
		this.reviewImage = image;
		return image;
	}

	@Override
	public void removeCurrentImage() {
		setReviewType(ReviewType.NORMAL);
	}

}