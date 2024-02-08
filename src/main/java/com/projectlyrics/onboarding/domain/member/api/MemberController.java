package com.projectlyrics.onboarding.domain.member.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.projectlyrics.onboarding.domain.member.dto.request.LoginRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.request.LogoutRequestDto;
import com.projectlyrics.onboarding.domain.member.dto.response.TokenResponseDto;
import com.projectlyrics.onboarding.domain.member.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {

	private final MemberService memberService;

	@PostMapping("/login")
	public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginRequestDto requestDto) {
		return ResponseEntity
			.status(HttpStatus.OK)
			.body(memberService.login(requestDto));
	}

	@PostMapping("/logout")
	public ResponseEntity<Void> logout(@Valid @RequestBody LogoutRequestDto requestDto) {
		memberService.logout(requestDto);
		return ResponseEntity
			.status(HttpStatus.NO_CONTENT)
			.body(null);
	}
}
