package com.projectlyrics.onboarding.global.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.projectlyrics.onboarding.global.exception.dto.ErrorResponse;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleCustomException(BusinessException ex) {
		return ResponseEntity
			.status(ex.getHttpStatus())
			.body(new ErrorResponse(
				ex.getCode(),
				ex.getMessage()
			));
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(
				ErrorCode.CONSTRAINT_VIOLATION.getCode(),
				ErrorCode.CONSTRAINT_VIOLATION.getMessage()
			));
	}

	@Nullable
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
		MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request
	) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(
				ErrorCode.METHOD_ARGUMENT_NOT_VALID.getCode(),
				ErrorCode.METHOD_ARGUMENT_NOT_VALID.getMessage()
			));
	}

	@Nullable
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
		Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request
	) {
		return ResponseEntity
			.status(statusCode)
			.body(new ErrorResponse(
				ErrorCode.UNHANDLED.getCode(),
				ex.getMessage()
			));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(
				ErrorCode.UNHANDLED.getCode(),
				ex.getMessage()
			));
	}
}
