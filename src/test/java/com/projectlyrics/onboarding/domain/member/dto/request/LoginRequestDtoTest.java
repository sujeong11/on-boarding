package com.projectlyrics.onboarding.domain.member.dto.request;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class LoginRequestDtoTest {

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void 사용자가_아이디를_빈문자열로_로그인_시도를_한다면_validation_에러가_발생한다() {
		// given
		LoginRequestDto loginRequestDto = createLoginRequestDto("", "aaAA1122!");

		// when
		Set<ConstraintViolation<LoginRequestDto>> violation = validator.validate(loginRequestDto);

		// then
		violation.forEach(error -> {
			assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다");
		});
	}

	@Test
	void 사용자가_비밀번호를_빈문자열로_로그인_시도를_한다면_validation_에러가_발생한다() {
		// given
		LoginRequestDto loginRequestDto = createLoginRequestDto("id", "");

		// when
		Set<ConstraintViolation<LoginRequestDto>> violation = validator.validate(loginRequestDto);

		// then
		violation.forEach(error -> {
			assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다");
		});
	}

	private LoginRequestDto createLoginRequestDto(String loginId, String password) {
		return new LoginRequestDto(loginId, password);
	}
}