package com.prgrms.ohouse.domain.commerce.model.order;

import java.time.LocalDate;

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

@Setter(AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", referencedColumnName = "id")
	private Order order;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private Product product;

	@NotNull
	private int quantity;

	@NotNull
	private int price;

	@NotNull
	private DeliveryType deliveryType;

	private LocalDate deliveryDate;

	public void setOrder(Order order) {
		order.getOrderItems().add(this);
		this.order = order;
	}

	private OrderItem(OrderItemBuilder builder) {
		setOrder(builder.order);
		setProduct(builder.product);
		setQuantity(builder.quantity);
		setPrice(builder.price);
		setDeliveryType(DeliveryType.DEPOSIT_WAIT);
		deliveryDate = null;
	}

	public static class OrderItemBuilder {
		private Order order;
		private Product product;
		private int quantity;
		private int price;

		public OrderItemBuilder price(int price) {
			if (price < 1) {
				throw new IllegalArgumentException("0보다 큰 수를 입력하세요");
			}
			this.price = price;
			return this;
		}

		public OrderItemBuilder order(Order order) {
			this.order = order;
			return this;
		}

		public OrderItemBuilder product(Product product) {
			this.product = product;
			return this;
		}

		public OrderItemBuilder quantity(int quantity) {
			this.quantity = quantity;
			return this;
		}

		public OrderItem build() {
			return new OrderItem(this);
		}
	}

	public static OrderItemBuilder builder() {
		return new OrderItemBuilder();
	}
}
