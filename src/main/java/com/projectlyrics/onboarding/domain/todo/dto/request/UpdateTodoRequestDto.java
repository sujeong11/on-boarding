package com.projectlyrics.onboarding.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record UpdateTodoRequestDto(
	@NotBlank String title,
	String memo
) {
}
