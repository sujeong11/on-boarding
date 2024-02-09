package com.projectlyrics.onboarding.domain.member.dto.response;

import com.projectlyrics.onboarding.domain.member.entity.Member;

public record UpdateNicknameResponseDto(
	Long memberId,
	String nickname
) {

	public static UpdateNicknameResponseDto from(Member member) {
		return new UpdateNicknameResponseDto(member.getId(), member.getNickname());
	}
}
