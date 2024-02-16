package com.projectlyrics.onboarding.domain.member.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.projectlyrics.onboarding.global.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "member_id")
	private Long id;

	@Column(nullable = false)
	private String loginId;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false, unique = true)
	private String nickname;

	@Column
	private String refreshToken;

	@Column
	private LocalDateTime nicknameUpdateAt;

	@Column
	@ElementCollection(fetch = FetchType.LAZY)
	private List<String> usedPasswordList;

	@Builder
	private Member(String loginId, String password, String nickname, String refreshToken) {
		this.loginId = loginId;
		this.password = password;
		this.nickname = nickname;
		this.refreshToken = refreshToken;
		this.usedPasswordList = new ArrayList<>();
	}

	public void updateRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void deleteRefreshToken() {
		this.refreshToken = null;
	}

	public void updateNickname(String nickname) {
		this.nickname = nickname;
		this.nicknameUpdateAt = LocalDateTime.now();
	}

	public void updatePassword(String currentPassword, String newPassword) {
		usedPasswordList.add(currentPassword);
		this.password = newPassword;
	}
}
