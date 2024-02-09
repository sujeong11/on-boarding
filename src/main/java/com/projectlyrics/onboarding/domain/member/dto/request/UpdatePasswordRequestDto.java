package com.projectlyrics.onboarding.domain.member.dto.request;

import com.projectlyrics.onboarding.global.common.ConstantUtil;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UpdatePasswordRequestDto(
	@NotNull
	@Pattern(regexp = ConstantUtil.PASSWORD_REGEX, message = ConstantUtil.PASSWORD_REGEX_ERROR_MSG)
	String password
) {
}
