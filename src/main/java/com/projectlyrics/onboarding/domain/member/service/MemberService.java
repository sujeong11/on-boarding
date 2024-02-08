package com.projectlyrics.onboarding.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlyrics.onboarding.domain.member.dto.request.LoginRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.response.TokenResponseDto;
import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.MemberLoginIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.MemberPasswordNotFoundException;
import com.projectlyrics.onboarding.domain.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

	private final MemberRepository memberRepository;

	@Transactional
	public TokenResponseDto login(LoginRequestDto requestDto) {
		if (!memberRepository.existsByLoginId(requestDto.loginId())) {
			throw new MemberLoginIdNotFoundException();
		}

		Member member = memberRepository.findByLoginIdAndPassword(requestDto.loginId(), requestDto.password())
			.orElseThrow(MemberPasswordNotFoundException::new);

		TokenResponseDto tokenDto = createToken();

		member.updateRefreshToken(tokenDto.refreshToken());

		return tokenDto;
	}

	private TokenResponseDto createToken() {
		return TokenResponseDto.of("임시 Access Token", "임시 Refresh Token");
	}
}
