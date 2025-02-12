package com.boss.aspect;

import com.boss.annotation.DataSource;
import com.baomidou.dynamic.datasource.toolkit.DynamicDataSourceContextHolder;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Aspect
@Order(1)
@Slf4j
@Component
public class DataSourceAspect {

    @Pointcut("@annotation(com.boss.annotation.DataSource)"
            + "|| @within(com.boss.annotation.DataSource)")
    public void dsPointCut() {
    }

    @Around("dsPointCut()")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        DataSource dataSource = getDataSource(point);

        if (Objects.nonNull(dataSource)) {
            String dsKey = dataSource.value();
            DynamicDataSourceContextHolder.push(dsKey);
            log.info("切换数据源到：{}", dsKey);
        }

        try {
            return point.proceed();
        } finally {
            // 销毁数据源
            DynamicDataSourceContextHolder.poll();
        }
    }

    private DataSource getDataSource(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        DataSource dataSource = AnnotationUtils.findAnnotation(signature.getMethod(), DataSource.class);
        if (Objects.nonNull(dataSource)) {
            return dataSource;
        }

        return AnnotationUtils.findAnnotation(signature.getDeclaringType(), DataSource.class);
    }
} 