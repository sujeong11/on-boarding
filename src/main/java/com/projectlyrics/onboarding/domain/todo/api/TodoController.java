package com.projectlyrics.onboarding.domain.todo.api;

import java.util.Objects;

import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.projectlyrics.onboarding.domain.todo.constant.DeletionMethod;
import com.projectlyrics.onboarding.domain.todo.dto.TodoDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.CreateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.UpdateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.exception.DeletionMethodNotValidException;
import com.projectlyrics.onboarding.domain.todo.service.TodoService;
import com.projectlyrics.onboarding.global.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/todos")
@RestController
public class TodoController {

	private final TodoService todoService;

	@PostMapping
	public ResponseEntity<TodoDto> createTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@Valid @RequestBody CreateTodoRequestDto requestDto
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		return ResponseEntity
			.status(HttpStatus.CREATED)
			.body(todoService.createTodo(memberId, requestDto));
	}

	@GetMapping("/{todoId}")
	public ResponseEntity<TodoDto> getTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "todoId") Long todoId,
		@RequestParam(value = "status", required = false) String status
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		TodoDto todoDto = Objects.equals(status, "deleted")
			? todoService.getDeletedTodo(memberId, todoId)
			: todoService.getTodo(memberId, todoId);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoDto);
	}

	@GetMapping
	public ResponseEntity<Slice<TodoDto>> getTodoList(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(value = "status", required = false) String status,
		@RequestParam(value = "startTodoId") Long startTodoId,
		@RequestParam(value = "size") int size
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		Slice<TodoDto> todoDtoList = Objects.equals(status, "deleted")
			? todoService.getDeletedTodoList(memberId, startTodoId, size)
			: todoService.getTodoList(memberId, startTodoId, size);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoDtoList);
	}

	@PatchMapping("/{todoId}")
	public ResponseEntity<TodoDto> updateTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "todoId") Long todoId,
		@Valid @RequestBody UpdateTodoRequestDto requestDto
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoService.updateTodo(memberId, todoId, requestDto));
	}

	@DeleteMapping("/{todoId}")
	public ResponseEntity<Void> deleteTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "todoId") Long todoId,
		@RequestParam(value = "method") String method
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		switch (DeletionMethod.valueOfIgnoreCase(method)) {
			case SOFT -> todoService.softDeleteTodo(memberId, todoId);
			case HARD -> todoService.hardDeleteTodo(memberId, todoId);
			case null -> throw new DeletionMethodNotValidException();
		}

		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(null);
	}

	@PatchMapping("/restoration/{todoId}")
	public ResponseEntity<TodoDto> restoreTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "todoId") Long todoId
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoService.restoreTodo(memberId, todoId));
	}

	@PatchMapping("/{todoId}/reordering")
	public ResponseEntity<Slice<TodoDto>> reorderTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "todoId") Long todoId,
		@RequestParam("to") int to,
		@RequestParam(value = "startTodoId") Long startTodoId,
		@RequestParam(value = "size") int size
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());
		todoService.reorderTodo(memberId, todoId, to);

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoService.getTodoList(memberId, startTodoId, size));
	}
}
