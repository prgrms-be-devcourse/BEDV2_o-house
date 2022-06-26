package com.prgrms.ohouse.commerce.domain.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.prgrms.ohouse.commerce.domain.model.product.enums.FourthCategory;
import com.prgrms.ohouse.commerce.domain.model.product.enums.RootCategory;
import com.prgrms.ohouse.commerce.domain.model.product.enums.SecondCategory;
import com.prgrms.ohouse.commerce.domain.model.product.enums.ThirdCategory;

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
	@Column(nullable = false)
	private RootCategory rootCategory;

	@NotNull
	@Column(nullable = false)
	private SecondCategory secondCategory;

	@NotNull
	@Column(nullable = false)
	private ThirdCategory thirdCategory;

	@NotNull
	@Column(nullable = false)
	private FourthCategory fourthCategory;

	@OneToMany(mappedBy = "category")
	List<Product> products = new ArrayList<>();

	public static Category of(RootCategory rootCategory, SecondCategory secondCategory, ThirdCategory thirdCategory, FourthCategory fourthCategory) {
		Category instance = new Category();
		instance.setRootCategory(rootCategory);
		instance.setSecondCategory(secondCategory);
		instance.setThirdCategory(thirdCategory);
		instance.setFourthCategory(fourthCategory);
		return instance;
	}
}
