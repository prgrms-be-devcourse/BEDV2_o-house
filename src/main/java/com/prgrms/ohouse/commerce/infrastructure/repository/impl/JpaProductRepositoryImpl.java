package com.prgrms.ohouse.commerce.infrastructure.repository.impl;

import static com.prgrms.ohouse.commerce.domain.model.product.QProduct.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;

import com.prgrms.ohouse.commerce.domain.application.command.ProductViewMainPageCommand;
import com.prgrms.ohouse.commerce.domain.model.product.Product;

import com.prgrms.ohouse.commerce.infrastructure.repository.custom.JpaProductRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaProductRepositoryImpl implements JpaProductRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<ProductViewMainPageCommand> findByIdAtDesc(Pageable pageable) {
		List<Product> products = queryFactory
			.selectFrom(product)
			.orderBy(product.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<ProductViewMainPageCommand> content = new ArrayList<>();
		for (Product product : products)
			content.add(new ProductViewMainPageCommand(product));
		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(content, pageable, hasNext);
	}
}
