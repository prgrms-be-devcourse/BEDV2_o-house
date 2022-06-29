package com.prgrms.ohouse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.config.BootstrapMode;

@EnableJpaRepositories(bootstrapMode = BootstrapMode.DEFERRED)
@EnableJpaAuditing
@SpringBootApplication(
	exclude = {
		org.springframework.cloud.aws.autoconfigure.context.ContextInstanceDataAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextStackAutoConfiguration.class,
		org.springframework.cloud.aws.autoconfigure.context.ContextRegionProviderAutoConfiguration.class
	}
)
public class OhouseApplication {

	public static void main(String[] args) {
		SpringApplication.run(OhouseApplication.class, args);
	}

}