package com.projectlyrics.onboarding.domain.member.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class UsedPasswordUseException extends BusinessException {

	public UsedPasswordUseException() {
		super(ErrorCode.MEMBER_USED_PASSWORD_USE);
	}
}