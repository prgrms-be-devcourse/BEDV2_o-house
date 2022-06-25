package com.prgrms.ohouse.commerce.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.commerce.domain.model.product.Category;
import com.prgrms.ohouse.commerce.domain.model.product.CategoryRepository;

public interface JpaCategoryRepository extends JpaRepository<Category, Long>, CategoryRepository {
}
