package com.projectlyrics.onboarding.domain.todo.dto;

import com.projectlyrics.onboarding.domain.todo.entity.Todo;

public record TodoDto(
	Long id,
	String title,
	String memo,
	long order,
	boolean isFinished
) {

	public static TodoDto from(Todo todo) {
		return new TodoDto(
			todo.getId(),
			todo.getTitle(),
			todo.getMemo(),
			todo.getOrders(),
			todo.isFinished()
		);
	}
}
