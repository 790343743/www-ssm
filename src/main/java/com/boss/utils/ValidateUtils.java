package com.boss.utils;

import java.util.regex.Pattern;

public class ValidateUtils {
    /**
     * 手机号正则
     */
    private static final String MOBILE_REGEX = "^1[3-9]\\d{9}$";
    
    /**
     * 邮箱正则
     */
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    
    /**
     * 身份证正则
     */
    private static final String ID_CARD_REGEX = "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";

    public static boolean isMobile(String mobile) {
        return Pattern.matches(MOBILE_REGEX, mobile);
    }

    public static boolean isEmail(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isIdCard(String idCard) {
        return Pattern.matches(ID_CARD_REGEX, idCard);
    }
} 