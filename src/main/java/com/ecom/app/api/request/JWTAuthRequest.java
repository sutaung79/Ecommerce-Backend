package com.ecom.app.api.request;


import lombok.Data;

@Data
public class JWTAuthRequest {
	private String username; // email
	private String password;
}
