package com.projectlyrics.onboarding.global.common;

public class ConstantUtil {

	// 영문 대소문자, 숫자, 특수문자(!, @, $, %)를 모두 포함하고 공복을 포함하지 않는지 검사하는 정규식
	public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@\\$%])[A-Za-z\\d!@\\$%]*$";
}
