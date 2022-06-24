package com.prgrms.ohouse.commerce.infrastructure.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.prgrms.ohouse.commerce.domain.model.product.Attribute;
import com.prgrms.ohouse.commerce.domain.model.product.AttributeRepository;

public interface JpaAttributeRepository extends JpaRepository<Attribute, Long>, AttributeRepository {
}
