package com.projectlyrics.onboarding.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.projectlyrics.onboarding.domain.member.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {

	boolean existsByLoginId(String loginId);

	Optional<Member> findByLoginIdAndPassword(String loginId, String password);
}
