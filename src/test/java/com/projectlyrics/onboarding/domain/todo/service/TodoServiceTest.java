package com.projectlyrics.onboarding.domain.todo.service;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.repository.MemberRepository;
import com.projectlyrics.onboarding.domain.todo.entity.Todo;
import com.projectlyrics.onboarding.domain.todo.repository.TodoRepository;
import com.projectlyrics.onboarding.util.MemberTestUtil;
import com.projectlyrics.onboarding.util.TodoTestUtil;

@ActiveProfiles("test")
@SpringBootTest
class TodoServiceTest {

	@Autowired
	private TodoService sut;

	@Autowired
	private TodoRepository todoRepository;

	@Autowired
	private MemberRepository memberRepository;

	private Member member;

	private Todo todo, todo2, todo3, todo4, todo5;

	@BeforeEach
	void setUp() {
		member = memberRepository.save(MemberTestUtil.createLoginMember());
		todo = todoRepository.save(TodoTestUtil.createTodo(member, 1));
		todo2 = todoRepository.save(TodoTestUtil.createTodo(member, 2));
		todo3 = todoRepository.save(TodoTestUtil.createTodo(member, 3));
		todo4 = todoRepository.save(TodoTestUtil.createTodo(member, 4));
		todo5 = todoRepository.save(TodoTestUtil.createTodo(member, 5));
	}

	@Test
	void todo를_원래_순서보다_뒤로_이동시킨다면_원래_위치_다음부터_이동시킬_위치까지의_위치_값을_모두_1씩_더해준다() {
		// given
		int to = 3;
		Long targetTodoId = todo.getId();

		// when
		sut.reorderTodo(member.getId(), targetTodoId, to);

		// then
		assertThat(findTodoOrder(targetTodoId)).isEqualTo(to);
		assertThat(findTodoOrder(todo2.getId())).isEqualTo(1);
		assertThat(findTodoOrder(todo3.getId())).isEqualTo(2);
		assertThat(findTodoOrder(todo4.getId())).isEqualTo(4);
		assertThat(findTodoOrder(todo5.getId())).isEqualTo(5);
	}

	@Test
	void todo를_원래_순서보다_앞으로_이동시킨다면_원래_위치부터_이동시킬_위치_이전까지의_위치_값을_모두_1씩_빼준다() {
		// given
		int to = 2;
		Long targetTodoId = todo4.getId();

		// when
		sut.reorderTodo(member.getId(), targetTodoId, to);

		// then
		assertThat(findTodoOrder(todo.getId())).isEqualTo(1);
		assertThat(findTodoOrder(todo2.getId())).isEqualTo(3);
		assertThat(findTodoOrder(todo3.getId())).isEqualTo(4);
		assertThat(findTodoOrder(targetTodoId)).isEqualTo(to);
		assertThat(findTodoOrder(todo5.getId())).isEqualTo(5);
	}

	private int findTodoOrder(Long todoId) {
		return todoRepository.findByIdAndIsDeletedIsFalse(todoId).get().getOrders();
	}
}