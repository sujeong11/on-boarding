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
	MEMBER_LOGIN_ID_NOT_FOUND(1200, "해당 아이디를 가진 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	MEMBER_PASSWORD_NOT_FOUND(1201, "해당 비밀번호를 가진 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	MEMBER_ID_NOT_FOUND(1202, "해당 아이디(PK)를 가진 회원을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	MEMBER_REFRESH_TOKEN_NOT_MATCH(1203, "회원의 리프레시 토큰과 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
	MEMBER_NICKNAME_DUPLICATED(1204, "수정하려는 닉네임은 이미 사용 중인 닉네임입니다.", HttpStatus.BAD_REQUEST),
	MEMBER_NICKNAME_UPDATE_NO_TIME(1205, "닉네임은 30일마다 1번만 수정할 수 있습니다.", HttpStatus.BAD_REQUEST),
	MEMBER_USED_PASSWORD_USE(1206, "이전에 사용한 비밀번호는 다시 사용할 수 없습니다.", HttpStatus.BAD_REQUEST),

	// Auth
	TOKEN_EXPIRED(1300, "유효기간이 만료된 토큰입니다.", HttpStatus.BAD_REQUEST),
	TOKEN_NOT_VALID(1301, "유효하지 않은 토큰입니다.", HttpStatus.BAD_REQUEST),
	ACCESS_DENIED(1302, "권한이 없어 접근이 거부되었습니다.", HttpStatus.FORBIDDEN),
	UNAUTHORIZED(1303, "인증과정에서 문제가 발생했습니다.", HttpStatus.UNAUTHORIZED),

	// Todo
	TODO_ID_NOT_FOUND(1400, "해당 아이디(PK)를 가진 할 일을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	TODO_MEMBER_ID_NOT_MATCH(1401, "해당 사용자가 작성한 할 일이 아닙니다.", HttpStatus.BAD_REQUEST),
	;

	private final Integer code;
	private final String message;
	private final HttpStatus httpStatus;
}
