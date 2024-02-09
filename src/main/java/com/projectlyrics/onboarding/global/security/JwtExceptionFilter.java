package com.projectlyrics.onboarding.global.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projectlyrics.onboarding.global.exception.ErrorCode;
import com.projectlyrics.onboarding.global.exception.dto.ErrorResponse;
import com.projectlyrics.onboarding.global.security.exception.TokenExpiredException;
import com.projectlyrics.onboarding.global.security.exception.TokenNotValidException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtExceptionFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain
	) throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (TokenExpiredException ex) {
			setErrorResponse(response, ErrorCode.TOKEN_EXPIRED);
		} catch (TokenNotValidException ex) {
			setErrorResponse(response, ErrorCode.TOKEN_NOT_VALID);
		}
	}

	private void setErrorResponse(HttpServletResponse response, ErrorCode errorCode) throws IOException {
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setCharacterEncoding("utf-8");
		ErrorResponse errorResponse = new ErrorResponse(errorCode.getCode(), errorCode.getMessage());
		new ObjectMapper().writeValue(response.getWriter(), errorResponse);
	}
}
