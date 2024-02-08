package com.projectlyrics.onboarding.domain.member.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class MemberIdNotFoundException extends BusinessException {

	public MemberIdNotFoundException() {
		super(ErrorCode.MEMBER_ID_NOT_FOUND);
	}
}
