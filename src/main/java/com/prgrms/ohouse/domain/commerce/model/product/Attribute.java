package com.prgrms.ohouse.domain.commerce.model.product;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.prgrms.ohouse.domain.commerce.model.product.enums.Color;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Shipping;
import com.prgrms.ohouse.domain.commerce.model.product.enums.Size;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter(AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attribute {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(nullable = false)
	private Color color;

	@NotNull
	@Column(nullable = false)
	private Size size;

	@NotBlank(message = "공백 사용 불가능합니다.")
	@Column(nullable = false)
	private String brand;

	@NotNull
	@Column(nullable = false)
	private Shipping shipping;

	@OneToMany(mappedBy = "attribute", cascade = CascadeType.ALL)
	private List<Product> products = new ArrayList<>();

	public static Attribute of(Color color, Size size, String brand, Shipping shipping) {
		Attribute instance = new Attribute();
		instance.setColor(color);
		instance.setSize(size);
		instance.setBrand(brand);
		instance.setShipping(shipping);
		return instance;
	}
}
