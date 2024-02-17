package com.projectlyrics.onboarding.global.common;

public class ConstantUtil {

	public static final String PASSWORD_REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@\\$%])[A-Za-z\\d!@\\$%]*$";

	public static final String PASSWORD_REGEX_ERROR_MSG = "비밀번호는 영문 대소문자, 숫자, 특수문자(!, @, $, %)를 모두 포함해야 하며 공백 문자가 없어야 합니다.";
}
