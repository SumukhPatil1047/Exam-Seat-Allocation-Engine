package com.example.examengine.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI examEngineAPI() {
		return new OpenAPI().info(new Info().title("Exam Seat Allocation Engine API")
				.description("API documentation for Exam Seat Allocation System").version("1.0.0"));
	}
}
