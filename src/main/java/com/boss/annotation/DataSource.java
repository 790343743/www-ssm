package com.boss.annotation;

import com.boss.enums.DataSourceType;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    String value() default DataSourceType.MASTER;
} 