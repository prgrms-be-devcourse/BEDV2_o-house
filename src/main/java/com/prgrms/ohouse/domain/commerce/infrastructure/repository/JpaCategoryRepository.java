package com.prgrms.ohouse.domain.commerce.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.domain.model.product.Category;
import com.prgrms.ohouse.domain.commerce.domain.model.product.CategoryRepository;

public interface JpaCategoryRepository extends JpaRepository<Category, Long>, CategoryRepository {
}
