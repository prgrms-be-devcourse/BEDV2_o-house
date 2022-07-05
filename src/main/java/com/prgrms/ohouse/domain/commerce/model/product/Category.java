package com.prgrms.ohouse.domain.commerce.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.prgrms.ohouse.domain.commerce.model.product.enums.FourthCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.RootCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.SecondCategory;
import com.prgrms.ohouse.domain.commerce.model.product.enums.ThirdCategory;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter(AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private RootCategory rootCategory;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private SecondCategory secondCategory;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ThirdCategory thirdCategory;

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FourthCategory fourthCategory;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
	List<Product> products = new ArrayList<>();

	public static Category of(RootCategory rootCategory, SecondCategory secondCategory, ThirdCategory thirdCategory,
		FourthCategory fourthCategory) {
		Category instance = new Category();
		instance.setRootCategory(rootCategory);
		instance.setSecondCategory(secondCategory);
		instance.setThirdCategory(thirdCategory);
		instance.setFourthCategory(fourthCategory);
		return instance;
	}
}
