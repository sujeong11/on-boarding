package com.projectlyrics.onboarding.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateNicknameRequestDto(@NotBlank String nickname) {
}
