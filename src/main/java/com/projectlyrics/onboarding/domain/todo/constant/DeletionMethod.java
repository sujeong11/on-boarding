package com.projectlyrics.onboarding.domain.todo.constant;

public enum DeletionMethod {
	SOFT, HARD;

	public static DeletionMethod valueOfIgnoreCase(String method) {
		if (method.equalsIgnoreCase("soft")) {
			return SOFT;
		} else if (method.equalsIgnoreCase("hard")) {
			return HARD;
		}
		return null;
	}
}
