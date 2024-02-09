package com.projectlyrics.onboarding.domain.member.repository;

import java.util.Optional;

import org.springframework.data.repository.Repository;

import com.projectlyrics.onboarding.domain.member.entity.Member;

public interface MemberRepository extends Repository<Member, Long> {

	Optional<Member> findById(Long memberId);

	Optional<Member> findByLoginId(String loginId);

	boolean existsByNickname(String nickname);
}
