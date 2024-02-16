package com.projectlyrics.onboarding.domain.todo.entity;

import org.hibernate.annotations.DynamicInsert;

import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.global.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "todo")
public class Todo extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "todo_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "member_id", nullable = false)
	private Member member;

	@Column(nullable = false)
	private String title;

	@Column
	private String memo;

	@Column(nullable = false)
	private int orders;

	@Column(name = "is_finished", nullable = false, columnDefinition = "boolean default false")
	private boolean isFinished;

	@Column(name = "is_deleted", nullable = false, columnDefinition = "boolean default false")
	private boolean isDeleted;

	@Builder
	private Todo(Member member, String title, String memo, int orders) {
		this.member = member;
		this.title = title;
		this.memo = memo;
		this.orders = orders;
	}
}
