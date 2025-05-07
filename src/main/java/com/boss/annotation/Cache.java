package com.boss.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 缓存注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Cache {
    /**
     * 缓存key前缀
     */
    String prefix() default "";

    /**
     * 缓存key，支持SpEL表达式
     */
    String key() default "";

    /**
     * 缓存过期时间
     */
    long expire() default 30;

    /**
     * 缓存过期时间单位
     */
    TimeUnit timeUnit() default TimeUnit.MINUTES;

    /**
     * 是否使用缓存
     */
    boolean useCache() default true;

    /**
     * 是否在更新时删除缓存
     */
    boolean deleteOnUpdate() default true;

    /**
     * 是否在删除时删除缓存
     */
    boolean deleteOnDelete() default true;
} 