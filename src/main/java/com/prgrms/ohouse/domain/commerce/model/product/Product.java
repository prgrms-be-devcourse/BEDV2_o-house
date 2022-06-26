package com.prgrms.ohouse.domain.commerce.model.product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.ohouse.domain.commerce.model.product.enums.PriceRange;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter(AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "공백 사용 불가능합니다.")
	@Column(length = 50, nullable = false)
	private String name;

	@NotNull
	@Column(nullable = false)
	private int price;

	@NotBlank(message = "공백 사용 불가능합니다.")
	@Column(length = 1000, nullable = false)
	private String contents;

	@NotNull
	@Column(nullable = false)
	private int viewCount;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "category_id", referencedColumnName = "id")
	private Category category;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "attribute_id", referencedColumnName = "id")
	private Attribute attribute;

	public static Product of(String name, int price, String contents,
		Category category, Attribute attribute) {
		Product instance = new Product();
		instance.setName(name);
		instance.setPrice(price);
		instance.setContents(contents);
		instance.setViewCount(0);
		instance.setCategory(category);
		instance.setAttribute(attribute);
		return instance;
	}

	private void validPriceRange(int price) {
		if (!PriceRange.ZERO.validRange(price) || !PriceRange.MAX.validRange(price)) {
			throw new ProductException("invalid price");
		}
	}

	private void setPrice(int price) {
		validPriceRange(price);
		this.price = price;
	}
}
