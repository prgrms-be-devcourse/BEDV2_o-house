package com.prgrms.ohouse.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.domain.commerce.model.product.Attribute;
import com.prgrms.ohouse.domain.commerce.model.product.AttributeRepository;

public interface JpaAttributeRepository extends JpaRepository<Attribute, Long>, AttributeRepository {
}
