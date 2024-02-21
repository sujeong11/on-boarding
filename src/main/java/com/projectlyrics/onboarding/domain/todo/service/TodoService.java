package com.projectlyrics.onboarding.domain.todo.service;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.MemberIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.repository.MemberRepository;
import com.projectlyrics.onboarding.domain.todo.dto.TodoDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.CreateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.UpdateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.entity.Todo;
import com.projectlyrics.onboarding.domain.todo.exception.TodoIdNotFoundException;
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
		checkMemberExists(memberId);
		Todo todo = findTodoById(todoId, memberId);
		return TodoDto.from(todo);
	}

	public Slice<TodoDto> getTodoList(Long memberId, Long startTodoId, int size) {
		checkMemberExists(memberId);

		Pageable pageable = PageRequest.of(0, size, Sort.by("orders"));

		Slice<Todo> todoList = todoRepository.findNotDeletedTodoAll(memberId, startTodoId, pageable);

		List<TodoDto> todoDtoList = todoList.stream()
			.map(TodoDto::from)
			.toList();

		return new SliceImpl<>(todoDtoList, pageable, todoList.hasNext());
	}

	public TodoDto getDeletedTodo(Long memberId, Long todoId) {
		checkMemberExists(memberId);
		Todo todo = findDeletedTodoById(todoId, memberId);
		return TodoDto.from(todo);
	}

	public Slice<TodoDto> getDeletedTodoList(Long memberId, Long startTodoId, int size) {
		checkMemberExists(memberId);

		Pageable pageable = PageRequest.of(0, size, Sort.by("orders"));

		Slice<Todo> todoList = todoRepository.findDeletedTodoAll(memberId, startTodoId, pageable);

		List<TodoDto> todoDtoList = todoList.stream()
			.map(TodoDto::from)
			.toList();

		return new SliceImpl<>(todoDtoList, pageable, todoList.hasNext());
	}

	@Transactional
	public TodoDto updateTodo(Long memberId, Long todoId, UpdateTodoRequestDto requestDto) {
		checkMemberExists(memberId);

		Todo todo = isTodoRestore(requestDto.isRestore())
			? restoreTodo(todoId, memberId) : findTodoById(todoId, memberId);

		updateTitle(todo, requestDto.title());
		updateMemo(todo, requestDto.memo());
		updateOrders(memberId, todo, requestDto.order());

		return TodoDto.from(todo);
	}

	@Transactional
	public void softDeleteTodo(Long memberId, Long todoId) {
		checkMemberExists(memberId);
		Todo todo = findTodoById(todoId, memberId);

		todo.deleteTodo();
	}

	@Transactional
	public void hardDeleteTodo(Long memberId, Long todoId) {
		checkMemberExists(memberId);
		Todo todo = findDeletedTodoById(todoId, memberId);

		todoRepository.delete(todo);
	}

	private void checkMemberExists(Long memberId) {
		if (!memberRepository.existsById(memberId)) {
			throw new MemberIdNotFoundException();
		}
	}

	private Todo findTodoById(Long todoId, Long memberId) {
		return todoRepository.findByIdAndIsDeletedIsFalse(todoId, memberId)
			.orElseThrow(TodoIdNotFoundException::new);
	}

	private Todo findDeletedTodoById(Long todoId, Long memberId) {
		return todoRepository.findByIdAndIsDeletedIsTrue(todoId, memberId)
			.orElseThrow(TodoIdNotFoundException::new);
	}

	private void updateTitle(Todo todo, String newTitle) {
		if (StringUtils.hasText(newTitle)) todo.updateTitle(newTitle);
	}

	private void updateMemo(Todo todo, String newMemo) {
		if (StringUtils.hasText(newMemo)) todo.updateMemo(newMemo);
	}

	private boolean validateTodoOrders(int currentOrder, int newOrder) {
		return !(currentOrder == newOrder) && newOrder > 0;
	}

	private void updateOrders(Long memberId, Todo todo, int newOrder) {
		if (!Objects.isNull(newOrder) && validateTodoOrders(todo.getOrders(), newOrder)) {
			updateEachOrder(memberId, newOrder, todo);
		}
	}

	private void updateEachOrder(Long memberId, int to, Todo todo) {
		if (todo.getOrders() > to) {
			todoRepository.incrementOrdersByRange(memberId, to, todo.getOrders() - 1);
		} else {
			todoRepository.decreaseOrdersByRange(memberId, todo.getOrders() + 1, to);
		}
		todo.updateOrder(to);
	}

	private boolean isTodoRestore(Boolean isRestore) {
		return !Objects.isNull(isRestore) && isRestore;
	}

	private Todo restoreTodo(Long todoId, Long memberId) {
		Todo todo = findDeletedTodoById(todoId, memberId);
		todo.restoreTodo();
		return todo;
	}
}
