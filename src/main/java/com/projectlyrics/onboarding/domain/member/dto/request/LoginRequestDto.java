package com.projectlyrics.onboarding.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
	@NotBlank String loginId,
	@NotBlank String password
) {
}
