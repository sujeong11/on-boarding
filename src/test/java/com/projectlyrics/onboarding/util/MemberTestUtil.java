package com.projectlyrics.onboarding.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.projectlyrics.onboarding.domain.member.entity.Member;

public class MemberTestUtil {

	public static Member createMember() {
		return Member.builder()
			.loginId("id")
			.password(encodePassword())
			.nickname("sujeong")
			.build();
	}

	public static Member createLoginMember() {
		return Member.builder()
			.loginId("id")
			.password(encodePassword())
			.nickname("sujeong")
			.refreshToken("임시 Refresh Token")
			.build();
	}

	private static String encodePassword() {
		return new BCryptPasswordEncoder().encode("aaAA1122!");
	}
}
