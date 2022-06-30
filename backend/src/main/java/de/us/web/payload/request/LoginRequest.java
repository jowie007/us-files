package de.us.web.payload.request;

import javax.validation.constraints.NotBlank;

// https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
