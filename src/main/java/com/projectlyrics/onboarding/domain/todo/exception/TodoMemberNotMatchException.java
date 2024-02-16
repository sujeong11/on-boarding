package com.projectlyrics.onboarding.domain.todo.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class TodoMemberNotMatchException extends BusinessException {

	public TodoMemberNotMatchException() {
		super(ErrorCode.TODO_MEMBER_ID_NOT_MATCH);
	}
}
