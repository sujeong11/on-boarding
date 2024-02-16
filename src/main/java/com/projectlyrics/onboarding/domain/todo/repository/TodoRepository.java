package com.projectlyrics.onboarding.domain.todo.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.todo.entity.Todo;

public interface TodoRepository extends Repository<Todo, Long> {

	int countByMember(Member member);

	Todo save(Todo todo);

	Optional<Todo> findByIdAndIsDeletedIsFalse(Long todoId);
}
