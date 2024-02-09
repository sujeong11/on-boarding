package com.projectlyrics.onboarding.domain.member.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlyrics.onboarding.domain.member.dto.request.LoginRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.response.TokenResponseDto;
import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.LoginIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.LoginPasswordNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.MemberIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.repository.MemberRepository;
import com.projectlyrics.onboarding.global.common.Role;
import com.projectlyrics.onboarding.global.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberService {

	private final MemberRepository memberRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public TokenResponseDto login(LoginRequestDto requestDto) {
		Member member = memberRepository.findByLoginId(requestDto.loginId())
			.orElseThrow(LoginIdNotFoundException::new);

		if (!passwordEncoder.matches(requestDto.password(), member.getPassword())) {
			throw new LoginPasswordNotFoundException();
		}

		TokenResponseDto tokenDto = createToken();

		member.updateRefreshToken(tokenDto.refreshToken());

		return tokenDto;
	}

	@Transactional
	public void logout(Long memberId) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(MemberIdNotFoundException::new);

		member.deleteRefreshToken();
	}

	private TokenResponseDto createToken() {
		String accessToken = jwtTokenProvider.createAccessToken(1L, Role.USER);
		String refreshToken = jwtTokenProvider.createRefreshToken(1L, Role.USER);
		return TokenResponseDto.of(accessToken, refreshToken);
	}
}
