package com.projectlyrics.onboarding.domain.member.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.projectlyrics.onboarding.domain.member.dto.request.LoginRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.request.UpdateNicknameRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.request.UpdatePasswordRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.response.TokenResponseDto;
import com.projectlyrics.onboarding.domain.member.dto.response.UpdateNicknameResponseDto;
import com.projectlyrics.onboarding.domain.member.entity.Member;
import com.projectlyrics.onboarding.domain.member.exception.LoginIdNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.LoginPasswordNotChangeException;
import com.projectlyrics.onboarding.domain.member.exception.LoginPasswordNotFoundException;
import com.projectlyrics.onboarding.domain.member.exception.NicknameDuplicatedException;
import com.projectlyrics.onboarding.domain.member.exception.NicknameUpdateTimeException;
import com.projectlyrics.onboarding.domain.member.repository.MemberRepository;
import com.projectlyrics.onboarding.global.common.ConstantUtil;
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
		String accessToken = "access-token";
		String refreshToken = "refresh-token";
		Member member = MemberTestUtil.createMember();
		LoginRequestDto loginRequestDto = createLoginRequestDto();
		given(memberRepository.findByLoginId(anyString())).willReturn(Optional.of(member));
		given(passwordEncoder.matches(anyString(), anyString())).willReturn(true);
		given(jwtTokenProvider.createAccessToken(anyLong(), any(Role.class))).willReturn(accessToken);
		given(jwtTokenProvider.createRefreshToken(anyLong(), any(Role.class))).willReturn(refreshToken);

		// when
		TokenResponseDto tokenResponseDto = sut.login(loginRequestDto);

		// then
		then(memberRepository).should().findByLoginId(anyString());
		then(passwordEncoder).should().matches(
			argThat(password -> Pattern.matches(ConstantUtil.PASSWORD_REGEX, loginRequestDto.password())),
			anyString()
		);
		then(jwtTokenProvider).should().createAccessToken(anyLong(), any(Role.class));
		then(jwtTokenProvider).should().createRefreshToken(anyLong(), any(Role.class));
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
		then(memberRepository).should().findByLoginId(anyString());
		then(passwordEncoder).shouldHaveNoInteractions();
		then(jwtTokenProvider).shouldHaveNoInteractions();
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
		then(memberRepository).should().findByLoginId(anyString());
		then(passwordEncoder).should().matches(anyString(), anyString());
		then(jwtTokenProvider).shouldHaveNoInteractions();
		assertThat(throwable).isInstanceOf(LoginPasswordNotFoundException.class);
	}

	@Test
	void 로그아웃_요청_시_DB의_사용자_리프레시_토큰을_null로_업데이트를_한다() {
		// given
		Member member = MemberTestUtil.createLoginMember();
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

		// when
		sut.logout(anyLong());

		// then
		then(memberRepository).should().findById(anyLong());
		assertThat(member.getRefreshToken()).isNull();
	}

	@Test
	void 사용자의_nicknameUpdateAt_필드가_null이라면_닉네임을_변경한_적이_없으므로_닉네임_수정을_허용한다() {
		// given
		Member member = Mockito.spy(MemberTestUtil.createLoginMember());
		UpdateNicknameRequestDto expectUpdateNicknameDto = createUpdateNicknameRequestDto();
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
		given(memberRepository.existsByNickname(anyString())).willReturn(false);
		doReturn(null).when(member).getNicknameUpdateAt();

		// when
		UpdateNicknameResponseDto actualUpdateNicknameDto = sut.updateNickname(anyLong(), expectUpdateNicknameDto);

		// then
		then(memberRepository).should().findById(anyLong());
		then(memberRepository).should().existsByNickname(anyString());
		assertThat(actualUpdateNicknameDto.nickname()).isEqualTo(expectUpdateNicknameDto.nickname());
	}

	@Test
	void 닉네임_수정_시_다른_사용자가_사용_중인_닉네임으로_변경하려고_하면_에러가_발생한다() {
		// given
		Member member = MemberTestUtil.createLoginMember();
		UpdateNicknameRequestDto updateNicknameRequestDto = createUpdateNicknameRequestDto();
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
		given(memberRepository.existsByNickname(anyString())).willReturn(true);

		// when
		Throwable throwable = catchThrowable(() -> sut.updateNickname(anyLong(), updateNicknameRequestDto));

		// then
		then(memberRepository).should().findById(anyLong());
		then(memberRepository).should().existsByNickname(anyString());
		assertThat(throwable).isInstanceOf(NicknameDuplicatedException.class);
	}

	@Test
	void 닉네임을_수정한_지_30일이_지나지_않았을때_닉네임을_수정하려고_하면_에러가_발생한다() {
		// given
		Member member = Mockito.spy(MemberTestUtil.createLoginMember());
		UpdateNicknameRequestDto updateNicknameRequestDto = createUpdateNicknameRequestDto();
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));
		given(memberRepository.existsByNickname(anyString())).willReturn(false);
		doReturn(LocalDateTime.now().minusDays(29)).when(member).getNicknameUpdateAt();

		// when
		Throwable throwable = catchThrowable(() -> sut.updateNickname(anyLong(), updateNicknameRequestDto));

		// then
		then(memberRepository).should().findById(anyLong());
		then(memberRepository).should().existsByNickname(anyString());
		assertThat(throwable).isInstanceOf(NicknameUpdateTimeException.class);
	}

	@Test
	void 비밀번호_수정_시_입력받은_배밀번호를_DB에_암호화해_업데이트한다() {
		// given
		Member member = MemberTestUtil.createLoginMember();
		UpdatePasswordRequestDto updatePasswordRequestDto = createUpdatePasswordRequestDto("aaA123!");
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

		// when
		sut.updatePassword(anyLong(), updatePasswordRequestDto);

		// then
		then(memberRepository).should().findById(anyLong());
		then(passwordEncoder).should().encode(
			argThat(password -> Pattern.matches(ConstantUtil.PASSWORD_REGEX, updatePasswordRequestDto.password()))
		);
		assertThat(member.getPassword()).isNotEqualTo(updatePasswordRequestDto.password());
	}

	@Test
	void 변경_전_비밀번호와_변경하려고_하는_비밀번호가_동일하다면_에러가_발생한다() {
		// given
		Member member = MemberTestUtil.createLoginMember();
		UpdatePasswordRequestDto updatePasswordRequestDto = createUpdatePasswordRequestDto(member.getPassword());
		given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

		// when
		Throwable throwable = catchThrowable(() -> sut.updatePassword(anyLong(), updatePasswordRequestDto));

		// then
		then(memberRepository).should().findById(anyLong());
		then(passwordEncoder).shouldHaveNoInteractions();
		assertThat(throwable).isInstanceOf(LoginPasswordNotChangeException.class);
	}

	private LoginRequestDto createLoginRequestDto() {
		return new LoginRequestDto("id", "aaAA1122!");
	}

	private UpdateNicknameRequestDto createUpdateNicknameRequestDto() {
		return new UpdateNicknameRequestDto("nickname");
	}

	private UpdatePasswordRequestDto createUpdatePasswordRequestDto(String password) {
		return new UpdatePasswordRequestDto(password);
	}
}
