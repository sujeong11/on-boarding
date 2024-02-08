package com.projectlyrics.onboarding.domain.member.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class MemberLoginIdNotFoundException extends BusinessException {

	public MemberLoginIdNotFoundException() {
		super(ErrorCode.MEMBER_LOGIN_ID_NOT_FOUND);
	}
}
