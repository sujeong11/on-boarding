package com.projectlyrics.onboarding.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.projectlyrics.onboarding.domain.member.entity.Member;

public class MemberTestUtil {

	public static Member createMember() {
		return Member.builder()
			.loginId("id")
			.password(encodePassword())
			.nickname(createNickname())
			.build();
	}

	public static Member createLoginMember() {
		return Member.builder()
			.loginId("id")
			.password(encodePassword())
			.nickname(createNickname())
			.refreshToken("임시 Refresh Token")
			.build();
	}

	private static String encodePassword() {
		return new BCryptPasswordEncoder().encode("aaAA1122!");
	}

	private static String createNickname() {
		// member의 nickname 필드는 unique 속성이 걸려있으므로 닉네임에 난수를 추가해 닉네임이 중복되지 않도록 함
		return "sujeong" + Math.random();
	}
}
