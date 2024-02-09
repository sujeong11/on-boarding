package com.projectlyrics.onboarding.global.security.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class TokenNotValidException extends BusinessException {

	public TokenNotValidException() {
		super(ErrorCode.TOKEN_NOT_VALID);
	}
}
