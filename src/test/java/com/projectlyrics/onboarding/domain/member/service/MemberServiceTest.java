package com.projectlyrics.onboarding.domain.member.service;

import static org.assertj.core.api.BDDAssertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projectlyrics.onboarding.domain.member.dto.request.LoginRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.response.TokenResponseDto;
import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.LoginIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.LoginPasswordNotFoundException;
import com.projectlyrics.onboarding.domain.member.repository.MemberRepository;
import com.projectlyrics.onboarding.global.common.Role;
import com.projectlyrics.onboarding.global.security.JwtTokenProvider;
import com.projectlyrics.onboarding.util.MemberTestUtil;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService sut;

	@Mock
	private MemberRepository memberRepository;

	@Mock
	private JwtTokenProvider jwtTokenProvider;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Test
	void 사용자가_로그인에_성공한다면_액세스_토큰과_리프레시_토큰을_응답한다() {
		// given
		Member member = MemberTestUtil.createMember();
		LoginRequestDto loginRequestDto = createLoginRequestDto();
		String accessToken = "access-token";
		String refreshToken = "refresh-token";
		given(memberRepository.findByLoginId(anyString())).willReturn(Optional.of(member));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
		given(jwtTokenProvider.createAccessToken(anyLong(), any(Role.class))).willReturn(accessToken);
		given(jwtTokenProvider.createRefreshToken(anyLong(), any(Role.class))).willReturn(refreshToken);

		// when
		TokenResponseDto tokenResponseDto = sut.login(loginRequestDto);

		// then
		assertThat(member.getRefreshToken()).isNotNull();
		assertThat(tokenResponseDto.accessToken()).isEqualTo(accessToken);
		assertThat(tokenResponseDto.refreshToken()).isEqualTo(refreshToken);
	}

	@Test
	void 사용자가_잘못된_아이디로_로그인을_한다면_에러가_발생한다() {
		// given
		LoginRequestDto loginRequestDto = createLoginRequestDto();
		given(memberRepository.findByLoginId(anyString())).willReturn(Optional.empty());

		// when
		Throwable throwable = catchThrowable(() -> sut.login(loginRequestDto));

		// then
		assertThat(throwable).isInstanceOf(LoginIdNotFoundException.class);
	}

	@Test
	void 사용자가_잘못된_비밀번호로_로그인을_한다면_에러가_발생한다() {
		// given
		Member member = MemberTestUtil.createMember();
		LoginRequestDto loginRequestDto = createLoginRequestDto();
		given(memberRepository.findByLoginId(anyString())).willReturn(Optional.of(member));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(false);

		// when
		Throwable throwable = catchThrowable(() -> sut.login(loginRequestDto));

		// then
		assertThat(throwable).isInstanceOf(LoginPasswordNotFoundException.class);
	}

	private LoginRequestDto createLoginRequestDto() {
		return new LoginRequestDto("id", "aaAA1122!");
	}
}
