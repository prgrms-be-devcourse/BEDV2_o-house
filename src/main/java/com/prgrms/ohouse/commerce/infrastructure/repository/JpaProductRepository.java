package com.prgrms.ohouse.commerce.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.commerce.domain.model.product.Product;
import com.prgrms.ohouse.commerce.domain.model.product.ProductRepository;
import com.prgrms.ohouse.commerce.infrastructure.repository.custom.JpaProductRepositoryCustom;

public interface JpaProductRepository
	extends JpaRepository<Product, Long>, ProductRepository, JpaProductRepositoryCustom {

}
