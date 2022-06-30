package com.prgrms.ohouse.domain.commerce.model.cart;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import com.prgrms.ohouse.domain.commerce.model.product.Product;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter(AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "cart_id", referencedColumnName = "id")
	private Cart cart;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	@NotNull
	private int productCount;

	public static CartItem of(Cart cart, Product product) {
		CartItem instance = new CartItem();
		instance.setCart(cart);
		instance.setProduct(product);
		instance.setProductCount(1);
		return instance;
	}

	private void setCart(Cart cart) {
		cart.getCartItems().add(this);
		this.cart = cart;
	}
}
