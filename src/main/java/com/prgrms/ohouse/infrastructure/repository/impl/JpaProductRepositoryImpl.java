package com.prgrms.ohouse.infrastructure.repository.impl;

import static com.prgrms.ohouse.domain.commerce.model.product.QProduct.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Repository;
import com.prgrms.ohouse.web.commerce.results.ProductViewMainPageResult;
import com.prgrms.ohouse.domain.commerce.model.product.Product;

import com.prgrms.ohouse.infrastructure.repository.custom.JpaProductRepositoryCustom;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class JpaProductRepositoryImpl implements JpaProductRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	@Override
	public Slice<ProductViewMainPageResult> findByIdAtDesc(Pageable pageable) {
		List<Product> products = queryFactory
			.selectFrom(product)
			.orderBy(product.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize() + 1)
			.fetch();

		List<ProductViewMainPageResult> content = new ArrayList<>();
		for (Product product : products)
			content.add(new ProductViewMainPageResult(product));
		boolean hasNext = false;
		if (content.size() > pageable.getPageSize()) {
			content.remove(pageable.getPageSize());
			hasNext = true;
		}
		return new SliceImpl<>(content, pageable, hasNext);
	}
}