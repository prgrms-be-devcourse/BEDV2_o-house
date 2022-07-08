package com.prgrms.ohouse.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.prgrms.ohouse.domain.common.security.AccessDeniedHandlerImpl;
import com.prgrms.ohouse.domain.common.security.JwtAuthenticationFilter;
import com.prgrms.ohouse.domain.common.security.JwtTokenProvider;
import com.prgrms.ohouse.domain.common.security.TokenProvideService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
			.antMatchers("/h2-console/**").permitAll()
			.antMatchers("/api/v0/login", "/api/v0/signup").permitAll()
			.antMatchers(HttpMethod.GET, "/api/v0/products/**","/api/v0/hwpost/**", "/api/v0/reviews/**").permitAll()
			.antMatchers(HttpMethod.PUT, "/api/v0/reviews/**").permitAll()
			.anyRequest().hasRole("USER")
			.and()
			.addFilterAt(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
			.exceptionHandling().accessDeniedHandler(accessDeniedHandler())
		;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/h2-console/**");
	}

	@Bean
	public AccessDeniedHandler accessDeniedHandler(){
		return new AccessDeniedHandlerImpl();
	}
}
