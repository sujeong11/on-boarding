package com.projectlyrics.onboarding.domain.todo.exception;

import com.projectlyrics.onboarding.global.exception.BusinessException;
import com.projectlyrics.onboarding.global.exception.ErrorCode;

public class DeletionMethodNotValidException extends BusinessException {

	public DeletionMethodNotValidException() {
		super(ErrorCode.DELETION_METHOD_NOT_VALID);
	}
}
