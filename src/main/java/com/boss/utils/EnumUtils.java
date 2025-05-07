package com.boss.utils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 枚举工具类
 */
public class EnumUtils {

    /**
     * 获取枚举列表
     */
    public static <T extends Enum<T>> List<T> getEnumList(Class<T> enumClass) {
        return Arrays.asList(enumClass.getEnumConstants());
    }

    /**
     * 获取枚举Map
     */
    public static <T extends Enum<T>, R> Map<R, T> getEnumMap(Class<T> enumClass, java.util.function.Function<T, R> keyMapper) {
        return Arrays.stream(enumClass.getEnumConstants())
                .collect(Collectors.toMap(keyMapper, e -> e));
    }

    /**
     * 根据code获取枚举
     */
    public static <T extends Enum<T>> T getEnumByCode(Class<T> enumClass, String code) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> {
                    try {
                        return code.equals(e.getClass().getMethod("getCode").invoke(e));
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * 根据value获取枚举
     */
    public static <T extends Enum<T>> T getEnumByValue(Class<T> enumClass, String value) {
        return Arrays.stream(enumClass.getEnumConstants())
                .filter(e -> {
                    try {
                        return value.equals(e.getClass().getMethod("getValue").invoke(e));
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .findFirst()
                .orElse(null);
    }

    /**
     * 获取枚举描述
     */
    public static <T extends Enum<T>> String getEnumDesc(T enumConstant) {
        try {
            return (String) enumConstant.getClass().getMethod("getDesc").invoke(enumConstant);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取枚举值
     */
    public static <T extends Enum<T>> String getEnumValue(T enumConstant) {
        try {
            return (String) enumConstant.getClass().getMethod("getValue").invoke(enumConstant);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取枚举编码
     */
    public static <T extends Enum<T>> String getEnumCode(T enumConstant) {
        try {
            return (String) enumConstant.getClass().getMethod("getCode").invoke(enumConstant);
        } catch (Exception e) {
            return null;
        }
    }
} 