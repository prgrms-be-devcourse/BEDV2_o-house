package com.prgrms.ohouse.domain.commerce.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.product.Product;
import com.prgrms.ohouse.domain.commerce.model.product.ProductRepository;
import com.prgrms.ohouse.domain.commerce.infrastructure.repository.custom.JpaProductRepositoryCustom;

public interface JpaProductRepository
	extends JpaRepository<Product, Long>, ProductRepository, JpaProductRepositoryCustom {

}
