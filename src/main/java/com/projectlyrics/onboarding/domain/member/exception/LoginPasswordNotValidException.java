package com.projectlyrics.onboarding.domain.member.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class LoginPasswordNotValidException extends BusinessException {

	public LoginPasswordNotValidException() {
		super(ErrorCode.MEMBER_PASSWORD_NOT_VALID);
	}
}
