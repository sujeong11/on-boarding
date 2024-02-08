package com.projectlyrics.onboarding.global.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 사용자 정의 에러 목록, 1200 ~
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode {

	UNHANDLED(1000, "알 수 없는 서버 에러가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR),

	// Validation
	METHOD_ARGUMENT_NOT_VALID(1100, "데이터 유효성 검사에 실패했습니다.", HttpStatus.BAD_REQUEST),
	CONSTRAINT_VIOLATION(1101, "데이터 유효성 검사에 실패했습니다.", HttpStatus.BAD_REQUEST),

	// Member
	MEMBER_ID_NOT_FOUND(1200, "해당 아이디(PK)를 가진 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	;

	private final Integer code;
	private final String message;
	private final HttpStatus httpStatus;
}
