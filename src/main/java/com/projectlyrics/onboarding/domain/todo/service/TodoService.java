package com.projectlyrics.onboarding.domain.todo.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.MemberIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.repository.MemberRepository;
import com.projectlyrics.onboarding.domain.todo.dto.TodoDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.CreateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.entity.Todo;
import com.projectlyrics.onboarding.domain.todo.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class TodoService {

	private final TodoRepository todoRepository;
	private final MemberRepository memberRepository;

	@Transactional
	public TodoDto createTodo(Long memberId, CreateTodoRequestDto requestDto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(MemberIdNotFoundException::new);

		int todoCount = todoRepository.countByMember(member);

		Todo todo = Todo.builder()
			.member(member)
			.title(requestDto.title())
			.memo(requestDto.memo())
			.orders(todoCount + 1)
			.build();

		Todo savedTodo = todoRepository.save(todo);

		return TodoDto.from(savedTodo);
	}
}
