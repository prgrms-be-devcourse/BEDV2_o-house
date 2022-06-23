package com.prgrms.ohouse.domain.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import com.prgrms.ohouse.domain.user.model.User;

@Slf4j
@Component
public class JwtTokenProvider {

	private final TokenProvideService tokenProvideService;
	private final String secretKey;
	private final String headerName;
	private static final long EXPIRY_MILLI_SECONDS = 60 * 60 * 1000L;

	public JwtTokenProvider(TokenProvideService tokenProvideService,
		@Value("${jwt.secretKey}") String secretKey,
		@Value("${jwt.headerName}") String headerName) {
		this.tokenProvideService = tokenProvideService;
		this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
		this.headerName = headerName;
	}

	public String createToken(String email, List<GrantedAuthority> authorities) {
		Claims claims = Jwts.claims().setSubject(email);
		claims.put("roles", authorities);
		Date now = new Date();
		return Jwts.builder()
			.setClaims(claims)
			.setIssuedAt(now)
			.setExpiration(new Date(now.getTime() + EXPIRY_MILLI_SECONDS))
			.signWith(SignatureAlgorithm.HS256, secretKey)
			.compact();
	}

	public Authentication getAuthentication(String token) {
		User user = (User)tokenProvideService.loadUserByUsername(parseToken(token));
		JwtAuthenticationToken authenticatedToken = new JwtAuthenticationToken(
			new JwtAuthentication(token, user.getUsername()),
			null, user.getAuthorities());
		authenticatedToken.setDetails(user);
		return authenticatedToken;
	}

	public String parseToken(String token) {
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	public String resolveToken(HttpServletRequest request) {
		return request.getHeader(headerName);
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			log.warn("잘못된 토큰입니다.", e);
		}
		return false;
	}
}
