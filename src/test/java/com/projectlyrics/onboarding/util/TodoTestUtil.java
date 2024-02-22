package com.projectlyrics.onboarding.util;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.todo.entity.Todo;

public class TodoTestUtil {

	public static Todo createTodo(Member member, int orders) {
		return Todo.builder()
			.member(member)
			.title("title")
			.memo("memo")
			.orders(orders)
			.build();
	}
}
