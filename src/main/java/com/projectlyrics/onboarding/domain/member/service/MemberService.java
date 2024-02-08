package com.projectlyrics.onboarding.domain.member.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlyrics.onboarding.domain.member.dto.request.LoginRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.request.LogoutRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.response.TokenResponseDto;
import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.LoginIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.LoginPasswordNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.MemberIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.RefreshTokenNotMatchException;
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
			throw new LoginIdNotFoundException();
		}

		Member member = memberRepository.findByLoginIdAndPassword(requestDto.loginId(), requestDto.password())
			.orElseThrow(LoginPasswordNotFoundException::new);

		TokenResponseDto tokenDto = createToken();

		member.updateRefreshToken(tokenDto.refreshToken());

		return tokenDto;
	}

	@Transactional
	public void logout(LogoutRequestDto requestDto) {
		Member member = memberRepository.findById(requestDto.memberId())
			.orElseThrow(MemberIdNotFoundException::new);

		if (!member.getRefreshToken().equals(requestDto.refreshToken())) {
			throw new RefreshTokenNotMatchException();
		}

		member.deleteRefreshToken();
	}

	private TokenResponseDto createToken() {
		return TokenResponseDto.of("임시 Access Token", "임시 Refresh Token");
	}
}
