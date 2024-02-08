package com.projectlyrics.onboarding.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI openAPI(@Value("${springdoc.version}") String appVersion) {
		return new OpenAPI()
			.info(new Info()
				.title("Lyrics API Docs")
				.description("Lyrics API 명세서")
				.contact(new Contact().name("sujeong").email("qalzm7351@gmail.com"))
				.version(appVersion));
	}
}
