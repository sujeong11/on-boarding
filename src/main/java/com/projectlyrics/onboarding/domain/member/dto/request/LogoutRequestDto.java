package com.projectlyrics.onboarding.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;

public record LogoutRequestDto(
	@NonNull Long memberId,
	@NotBlank String refreshToken
	) {
}
