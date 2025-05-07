package com.boss.utils;

import java.util.regex.Pattern;

/**
 * 数据脱敏工具类
 */
public class DesensitizationUtils {

    /**
     * 手机号脱敏
     */
    public static String desensitizePhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            return phone;
        }
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 邮箱脱敏
     */
    public static String desensitizeEmail(String email) {
        if (StringUtils.isEmpty(email)) {
            return email;
        }
        return email.replaceAll("(\\w{3})\\w+(@\\w+)", "$1***$2");
    }

    /**
     * 身份证号脱敏
     */
    public static String desensitizeIdCard(String idCard) {
        if (StringUtils.isEmpty(idCard)) {
            return idCard;
        }
        return idCard.replaceAll("(\\d{4})\\d{10}(\\w{4})", "$1**********$2");
    }

    /**
     * 银行卡号脱敏
     */
    public static String desensitizeBankCard(String bankCard) {
        if (StringUtils.isEmpty(bankCard)) {
            return bankCard;
        }
        return bankCard.replaceAll("(\\d{4})\\d+(\\d{4})", "$1****$2");
    }

    /**
     * 姓名脱敏
     */
    public static String desensitizeName(String name) {
        if (StringUtils.isEmpty(name)) {
            return name;
        }
        if (name.length() <= 1) {
            return name;
        }
        return name.substring(0, 1) + "**";
    }

    /**
     * 地址脱敏
     */
    public static String desensitizeAddress(String address) {
        if (StringUtils.isEmpty(address)) {
            return address;
        }
        if (address.length() <= 4) {
            return address;
        }
        return address.substring(0, 4) + "****";
    }

    /**
     * 自定义脱敏
     *
     * @param str 原始字符串
     * @param start 开始保留长度
     * @param end 结束保留长度
     * @param mask 掩码字符
     * @return 脱敏后的字符串
     */
    public static String desensitize(String str, int start, int end, char mask) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }
        if (start < 0 || end < 0 || start + end >= str.length()) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        sb.append(str.substring(0, start));
        for (int i = 0; i < str.length() - start - end; i++) {
            sb.append(mask);
        }
        sb.append(str.substring(str.length() - end));
        return sb.toString();
    }
} 