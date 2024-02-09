package com.projectlyrics.onboarding.domain.member.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdatePasswordRequestDto(@NotBlank String password) {
}
