package com.prgrms.ohouse.domain.common.security;

public class JwtAuthentication {

	public final String token;

	public final String email;

	public JwtAuthentication(String token, String email) {

		this.token = token;
		this.email = email;
	}

	@Override
	public String toString() {
		return new StringBuilder()
			.append("JwtAuthentication[token=" + token)
			.append(",email=" + email)
			.append("]")
			.toString();
	}
}
