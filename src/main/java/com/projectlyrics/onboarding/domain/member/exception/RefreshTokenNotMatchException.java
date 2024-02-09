package com.projectlyrics.onboarding.domain.member.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class RefreshTokenNotMatchException extends BusinessException {

	public RefreshTokenNotMatchException() {
		super(ErrorCode.MEMBER_REFRESH_TOKEN_NOT_MATCH);
	}
}
