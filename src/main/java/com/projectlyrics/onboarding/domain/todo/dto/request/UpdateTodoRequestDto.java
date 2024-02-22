package com.projectlyrics.onboarding.domain.todo.dto.request;

public record UpdateTodoRequestDto(
	String title,
	String memo,
	Integer order,
	Boolean isRestore
) {
}
