package com.projectlyrics.onboarding.domain.todo.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class TodoIdNotFoundException extends BusinessException {

	public TodoIdNotFoundException() {
		super(ErrorCode.TODO_ID_NOT_FOUND);
	}
}
