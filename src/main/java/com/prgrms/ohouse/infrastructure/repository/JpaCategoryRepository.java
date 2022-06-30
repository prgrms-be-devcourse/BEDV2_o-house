package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.product.Category;
import com.prgrms.ohouse.domain.commerce.model.product.CategoryRepository;

public interface JpaCategoryRepository extends JpaRepository<Category, Long>, CategoryRepository {
}
