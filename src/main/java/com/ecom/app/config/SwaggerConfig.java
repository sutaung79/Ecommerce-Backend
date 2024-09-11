package com.ecom.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI().info(new Info().title("E-Commerce TakeHome Test")
			.description("This API exposes endpoints to manage Ecommerce application.")
			.version("v1.0.0"));
	}
	


}
