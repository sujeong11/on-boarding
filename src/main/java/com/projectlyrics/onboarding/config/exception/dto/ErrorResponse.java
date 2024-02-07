package com.projectlyrics.onboarding.config.exception.dto;

public record ErrorResponse(
	Integer code,
	String message
) {
}
