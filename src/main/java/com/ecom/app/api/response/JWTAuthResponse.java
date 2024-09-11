package com.ecom.app.api.response;


import com.ecom.app.dto.UserDTO;

import lombok.Data;

@Data
public class JWTAuthResponse {
	private String token;
	
	private UserDTO user;
}