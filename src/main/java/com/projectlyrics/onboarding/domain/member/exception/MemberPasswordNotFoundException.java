package com.projectlyrics.onboarding.domain.member.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class MemberPasswordNotFoundException extends BusinessException {

	public MemberPasswordNotFoundException() {
		super(ErrorCode.MEMBER_PASSWORD_NOT_FOUND);
	}
}
