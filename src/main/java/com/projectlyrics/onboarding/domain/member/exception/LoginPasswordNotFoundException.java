package com.projectlyrics.onboarding.domain.member.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class LoginPasswordNotFoundException extends BusinessException {

	public LoginPasswordNotFoundException() {
		super(ErrorCode.MEMBER_PASSWORD_NOT_FOUND);
	}
}
