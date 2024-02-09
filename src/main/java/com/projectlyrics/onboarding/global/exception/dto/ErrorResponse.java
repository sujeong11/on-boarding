package com.projectlyrics.onboarding.global.exception.dto;

public record ErrorResponse(
	Integer code,
	String message
) {
}
