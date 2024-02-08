package com.projectlyrics.onboarding.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

	private final Integer code;
	private final String message;
	private final HttpStatus httpStatus;

	public BusinessException(ErrorCode errorCode) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage();
		this.httpStatus = errorCode.getHttpStatus();
	}

	public BusinessException(ErrorCode errorCode, String optionalMessage) {
		this.code = errorCode.getCode();
		this.message = errorCode.getMessage() + " " + optionalMessage;
		this.httpStatus = errorCode.getHttpStatus();
	}
}
