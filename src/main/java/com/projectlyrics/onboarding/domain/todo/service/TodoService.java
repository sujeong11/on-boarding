package com.projectlyrics.onboarding.domain.todo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.MemberIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.repository.MemberRepository;
import com.projectlyrics.onboarding.domain.todo.dto.TodoDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.CreateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.UpdateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.entity.Todo;
import com.projectlyrics.onboarding.domain.todo.exception.TodoIdNotFoundException;
import com.projectlyrics.onboarding.domain.todo.exception.TodoMemberNotMatchException;
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

	public TodoDto getTodo(Long memberId, Long todoId) {
		if (!memberRepository.existsById(memberId)) {
			throw new MemberIdNotFoundException();
		}

		Todo todo = todoRepository.findByIdAndIsDeletedIsFalse(todoId)
			.orElseThrow(TodoIdNotFoundException::new);

		if (todo.getMember().getId() != memberId) {
			throw new TodoMemberNotMatchException();
		}

		return TodoDto.from(todo);
	}

	public Slice<TodoDto> getTodoList(Long memberId, Long startTodoId, int size) {
		if (!memberRepository.existsById(memberId)) {
			throw new MemberIdNotFoundException();
		}

		Pageable pageable = PageRequest.of(0, size);

		Slice<Todo> todoList = todoRepository.findNotDeletedTodoAll(startTodoId, pageable);

		List<TodoDto> todoDtoList = todoList.stream()
			.map(TodoDto::from)
			.collect(Collectors.toUnmodifiableList());

		return new SliceImpl<>(todoDtoList, pageable, todoList.hasNext());
	}

	@Transactional
	public TodoDto updateTodo(Long memberId, Long todoId, UpdateTodoRequestDto requestDto) {
		if (!memberRepository.existsById(memberId)) {
			throw new MemberIdNotFoundException();
		}

		Todo todo = todoRepository.findByIdAndIsDeletedIsFalse(todoId)
			.orElseThrow(TodoIdNotFoundException::new);

		if (todo.getMember().getId() != memberId) {
			throw new TodoMemberNotMatchException();
		}

		todo.updateTodo(requestDto.title(), requestDto.memo());

		return TodoDto.from(todo);
	}

	@Transactional
	public void deleteTodo(Long memberId, Long todoId) {
		if (!memberRepository.existsById(memberId)) {
			throw new MemberIdNotFoundException();
		}

		Todo todo = todoRepository.findByIdAndIsDeletedIsFalse(todoId)
			.orElseThrow(TodoIdNotFoundException::new);

		if (todo.getMember().getId() != memberId) {
			throw new TodoMemberNotMatchException();
		}

		todo.deleteTodo();
	}

	@Transactional
	public TodoDto restoreTodo(Long memberId, Long todoId) {
		if (!memberRepository.existsById(memberId)) {
			throw new MemberIdNotFoundException();
		}

		Todo todo = todoRepository.findByIdAndIsDeletedIsTrue(todoId)
			.orElseThrow(TodoIdNotFoundException::new);

		if (todo.getMember().getId() != memberId) {
			throw new TodoMemberNotMatchException();
		}

		todo.restoreTodo();

		return TodoDto.from(todo);
	}
}
