package com.projectlyrics.onboarding.global.security.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class TokenExpiredException extends BusinessException {

	public TokenExpiredException() {
		super(ErrorCode.TOKEN_EXPIRED);
	}
}
