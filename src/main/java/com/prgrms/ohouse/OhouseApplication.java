package com.prgrms.ohouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class OhouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(OhouseApplication.class, args);
	}

}
