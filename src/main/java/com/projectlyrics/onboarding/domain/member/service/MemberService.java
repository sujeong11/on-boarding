package com.projectlyrics.onboarding.domain.member.service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.projectlyrics.onboarding.domain.member.dto.request.LoginRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.request.UpdateNicknameRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.request.UpdatePasswordRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.response.TokenResponseDto;
import com.projectlyrics.onboarding.domain.member.dto.response.UpdateNicknameResponseDto;
import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.LoginIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.LoginPasswordNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.MemberIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.NicknameDuplicatedException;
import com.projectlyrics.onboarding.domain.member.exception.NicknameUpdateTimeException;
import com.projectlyrics.onboarding.domain.member.exception.UsedPasswordUseException;
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

	@Transactional
	public UpdateNicknameResponseDto updateNickname(Long memberId, UpdateNicknameRequestDto requestDto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(MemberIdNotFoundException::new);

		if (memberRepository.existsByNickname(requestDto.nickname())) {
			throw new NicknameDuplicatedException();
		}

		boolean isUpdatable = member.getNicknameUpdateAt() == null
			? true : Duration.between(member.getNicknameUpdateAt(), LocalDateTime.now()).toDays() > 30;

		if (!isUpdatable) {
			throw new NicknameUpdateTimeException();
		}

		member.updateNickname(requestDto.nickname());

		return UpdateNicknameResponseDto.from(member);
	}

	@Transactional
	public void updatePassword(Long memberId, UpdatePasswordRequestDto requestDto) {
		Member member = memberRepository.findById(memberId)
			.orElseThrow(MemberIdNotFoundException::new);

		List<String> usedPasswordList = member.getUsedPasswordList();

		for (String usedPassword : usedPasswordList) {
			if (passwordEncoder.matches(requestDto.password(), usedPassword)) {
				throw new UsedPasswordUseException();
			}
		}

		member.updatePassword(
			member.getPassword(),
			passwordEncoder.encode(requestDto.password())
		);
	}

	private TokenResponseDto createToken() {
		String accessToken = jwtTokenProvider.createAccessToken(1L, Role.USER);
		String refreshToken = jwtTokenProvider.createRefreshToken(1L, Role.USER);
		return TokenResponseDto.of(accessToken, refreshToken);
	}
}
