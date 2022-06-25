package com.prgrms.ohouse.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import com.prgrms.ohouse.domain.common.security.JwtAuthenticationFilter;
import com.prgrms.ohouse.domain.common.security.JwtTokenProvider;
import com.prgrms.ohouse.domain.common.security.TokenProvideService;

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final TokenProvideService tokenProvideService;
	private final JwtTokenProvider jwtTokenProvider;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(tokenProvideService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()
			.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and()
			.authorizeRequests()
			.anyRequest().permitAll()
			.and()
			.addFilterAfter(new JwtAuthenticationFilter(jwtTokenProvider), SecurityContextPersistenceFilter.class)
		;
	}
}
