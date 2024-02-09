package com.projectlyrics.onboarding.global.security.handler;

import java.io.IOException;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectlyrics.onboarding.global.exception.ErrorCode;
import com.projectlyrics.onboarding.global.exception.dto.ErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(
		HttpServletRequest request,
		HttpServletResponse response,
		AuthenticationException authException
	) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("utf-8");
		ErrorResponse errorResponse = new ErrorResponse(ErrorCode.UNAUTHORIZED.getCode(), authException.getMessage());
		new ObjectMapper().writeValue(response.getWriter(), errorResponse);
	}
}
