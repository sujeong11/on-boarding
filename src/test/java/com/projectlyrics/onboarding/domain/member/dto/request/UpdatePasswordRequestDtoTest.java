package com.projectlyrics.onboarding.domain.member.dto.request;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.projectlyrics.onboarding.global.common.ConstantUtil;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class UpdatePasswordRequestDtoTest {

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void 사용자가_비밀번호를_null로_변경하려고_한다면_validation_에러가_발생한다() {
		// given
		UpdatePasswordRequestDto updatePasswordRequestDto = createUpdatePasswordRequestDto(null);

		// when
		Set<ConstraintViolation<UpdatePasswordRequestDto>> violation = validator.validate(updatePasswordRequestDto);

		// then
		violation.forEach(error -> {
			assertThat(error.getMessage()).isEqualTo("널이어서는 안됩니다");
		});
	}

	@ParameterizedTest
	@ValueSource(strings = {"aA1", "aA!", "a1!", "A1!", "aA 1!"})
	void 사용자가_비밀번호를_정규식을_통과하지_못할_값으로_변경하려고_한다면_validation_에러가_발생한다(String password) {
		// given
		UpdatePasswordRequestDto updatePasswordRequestDto = createUpdatePasswordRequestDto(password);

		// when
		Set<ConstraintViolation<UpdatePasswordRequestDto>> violation = validator.validate(updatePasswordRequestDto);

		// then
		violation.forEach(error -> {
			assertThat(error.getMessage()).isEqualTo(ConstantUtil.PASSWORD_REGEX_ERROR_MSG);
		});
	}

	private UpdatePasswordRequestDto createUpdatePasswordRequestDto(String password) {
		return new UpdatePasswordRequestDto(password);
	}
}