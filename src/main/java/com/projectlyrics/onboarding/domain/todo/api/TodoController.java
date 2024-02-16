package com.projectlyrics.onboarding.domain.todo.api;

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

import com.projectlyrics.onboarding.domain.todo.dto.TodoDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.CreateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.dto.request.UpdateTodoRequestDto;
import com.projectlyrics.onboarding.domain.todo.service.TodoService;
import com.projectlyrics.onboarding.global.security.CustomUserDetails;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/todo")
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
		@PathVariable(value = "todoId") Long todoId
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoService.getTodo(memberId, todoId));
	}

	@GetMapping
	public ResponseEntity<Slice<TodoDto>> getTodoList(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(value = "startTodoId") Long startTodoId,
		@RequestParam(value = "size") int size
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoService.getTodoList(memberId, startTodoId, size));
	}

	@GetMapping("/deleted/{todoId}")
	public ResponseEntity<TodoDto> getDeletedTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "todoId") Long todoId
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoService.getDeletedTodo(memberId, todoId));
	}

	@GetMapping("/deleted")
	public ResponseEntity<Slice<TodoDto>> getDeletedTodoList(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@RequestParam(value = "startTodoId") Long startTodoId,
		@RequestParam(value = "size") int size
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());

		return ResponseEntity
			.status(HttpStatus.OK)
			.body(todoService.getDeletedTodoList(memberId, startTodoId, size));
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

	@DeleteMapping("/soft/{todoId}")
	public ResponseEntity<Void> softDeleteTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "todoId") Long todoId
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());
		todoService.softDeleteTodo(memberId, todoId);

		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(null);
	}

	@DeleteMapping("/hard/{todoId}")
	public ResponseEntity<Void> hardDeleteTodo(
		@AuthenticationPrincipal CustomUserDetails userDetails,
		@PathVariable(value = "todoId") Long todoId
	) {
		Long memberId = Long.valueOf(userDetails.getMemberId());
		todoService.hardDeleteTodo(memberId, todoId);

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
}
