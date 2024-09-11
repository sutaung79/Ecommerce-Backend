package com.ecom.app.api.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginCredentials {
	
	
	@Email
	@Column(unique = true, nullable = false)
	@NotBlank(message = "Email cannot be empty.")
	private String email;

	@NotBlank(message = "Password cannot be empty.")
	private String password;
}
