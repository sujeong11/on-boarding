package com.projectlyrics.onboarding.domain.member.dto.request;

import static org.assertj.core.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

class UpdateNicknameRequestDtoTest {

	private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

	@Test
	void 사용자가_닉네임을_빈문자열로_변경한다면_validation_에러가_발생한다() {
		// given
		UpdateNicknameRequestDto updateNicknameRequestDto = createUpdateNicknameRequestDto("");

		// when
		Set<ConstraintViolation<UpdateNicknameRequestDto>> violation = validator.validate(updateNicknameRequestDto);

		// then
		violation.forEach(error -> {
			assertThat(error.getMessage()).isEqualTo("공백일 수 없습니다");
		});
	}

	private UpdateNicknameRequestDto createUpdateNicknameRequestDto(String nickname) {
		return new UpdateNicknameRequestDto(nickname);
	}
}