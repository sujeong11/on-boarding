package com.projectlyrics.onboarding.domain.todo.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.todo.entity.Todo;

public interface TodoRepository extends Repository<Todo, Long> {

	int countByMember(Member member);

	Todo save(Todo todo);

	Optional<Todo> findByIdAndIsDeletedIsFalse(Long todoId);

	@Query("SELECT t FROM Todo t WHERE t.id > :startTodoId AND t.member.id = :memberId AND t.isDeleted = FALSE")
	Slice<Todo> findNotDeletedTodoAll(@Param("memberId") Long memberId, @Param("startTodoId") Long startTodoId, Pageable pageable);

	Optional<Todo> findByIdAndIsDeletedIsTrue(Long todoId);

	@Query("SELECT t FROM Todo t WHERE t.id > :startTodoId AND t.member.id = :memberId AND t.isDeleted = TRUE")
	Slice<Todo> findDeletedTodoAll(@Param("memberId") Long memberId, @Param("startTodoId") Long startTodoId, Pageable pageable);

	void deleteById(Long todoId);
}
