package com.prgrms.ohouse.infrastructure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.prgrms.ohouse.domain.commerce.model.review.ReviewRepository;
import com.prgrms.ohouse.domain.commerce.model.review.dummy.ProductRepository;
import com.prgrms.ohouse.domain.commerce.model.review.dummy.Products;
import com.prgrms.ohouse.domain.commerce.model.review.dummy.User;
import com.prgrms.ohouse.domain.commerce.model.review.dummy.UserRepository;

@Component
@Transactional
public class TestDataProvider {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ReviewRepository reviewRepository;

	public Long insertUser(){
		return userRepository.save(new User()).getId();
	}

	public Long insertProduct(){
		return productRepository.save(new Products()).getId();
	}

	public void deleteAllData(){
		userRepository.deleteAll();
		productRepository.deleteAll();
	}
}
