package com.projectlyrics.onboarding.domain.member.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class NicknameUpdateTimeException extends BusinessException {

	public NicknameUpdateTimeException() {
		super(ErrorCode.MEMBER_NICKNAME_UPDATE_NO_TIME);
	}
}
