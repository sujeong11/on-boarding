package com.projectlyrics.onboarding.domain.todo.dto.request;

public record UpdateTodoRequestDto(
	String title,
	String memo,
	int order,
	Boolean isRestore
) {
}
