package com.projectlyrics.onboarding.domain.todo.repository;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.todo.entity.Todo;

public interface TodoRepository extends Repository<Todo, Long> {

	int countByMember(Member member);

	Todo save(Todo todo);

	@Query("SELECT t FROM Todo t "
		+ "WHERE t.id = :todoId "
		+ "AND t.member.id = :memberId "
		+ "AND t.isDeleted = FALSE")
	Optional<Todo> findByIdAndIsDeletedIsFalse(@Param("todoId") Long todoId, @Param("memberId") Long memberId);

	@Query("SELECT t FROM Todo t "
		+ "WHERE t.id > :startTodoId "
		+ "AND t.member.id = :memberId "
		+ "AND t.isDeleted = FALSE")
	Slice<Todo> findNotDeletedTodoAll(@Param("memberId") Long memberId, @Param("startTodoId") Long startTodoId, Pageable pageable);

	@Query("SELECT t FROM Todo t "
		+ "WHERE t.id = :todoId "
		+ "AND t.member.id = :memberId "
		+ "AND t.isDeleted = TRUE")
	Optional<Todo> findByIdAndIsDeletedIsTrue(@Param("todoId") Long todoId, @Param("memberId") Long memberId);

	@Query("SELECT t FROM Todo t "
		+ "WHERE t.id > :startTodoId "
		+ "AND t.member.id = :memberId "
		+ "AND t.isDeleted = TRUE")
	Slice<Todo> findDeletedTodoAll(@Param("memberId") Long memberId, @Param("startTodoId") Long startTodoId, Pageable pageable);

	void delete(Todo todo);

	@Modifying
	@Query("UPDATE Todo t "
		+ "SET t.orders = t.orders + 1 "
		+ "WHERE t.member.id = :memberId "
		+ "AND t.orders BETWEEN :start AND :end")
	int incrementOrdersByRange(@Param("memberId") Long memberId, @Param("start") int start, @Param("end") int end);

	@Modifying
	@Query("UPDATE Todo t "
		+ "SET t.orders = t.orders - 1 "
		+ "WHERE t.member.id = :memberId "
		+ "AND t.orders BETWEEN :start AND :end")
	int decreaseOrdersByRange(@Param("memberId") Long memberId, @Param("start") int start, @Param("end") int end);
}
