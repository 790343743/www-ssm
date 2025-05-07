package com.boss.utils;

import com.boss.annotation.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * 缓存key生成器
 */
public class CacheKeyGenerator {

    private static final ExpressionParser PARSER = new SpelExpressionParser();

    /**
     * 生成缓存key
     */
    public static String generateKey(ProceedingJoinPoint joinPoint, Cache cache) {
        StringBuilder key = new StringBuilder();
        
        // 添加前缀
        if (!StringUtils.isEmpty(cache.prefix())) {
            key.append(cache.prefix()).append(":");
        }
        
        // 添加类名
        key.append(joinPoint.getTarget().getClass().getSimpleName()).append(":");
        
        // 添加方法名
        key.append(joinPoint.getSignature().getName()).append(":");
        
        // 添加参数
        if (!StringUtils.isEmpty(cache.key())) {
            key.append(parseKey(cache.key(), joinPoint));
        } else {
            key.append(generateKeyByArgs(joinPoint));
        }
        
        return key.toString();
    }

    /**
     * 解析SpEL表达式
     */
    private static String parseKey(String key, ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object[] args = joinPoint.getArgs();
        Parameter[] parameters = method.getParameters();
        
        EvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameters.length; i++) {
            context.setVariable(parameters[i].getName(), args[i]);
        }
        
        Expression expression = PARSER.parseExpression(key);
        return expression.getValue(context, String.class);
    }

    /**
     * 根据参数生成key
     */
    private static String generateKeyByArgs(ProceedingJoinPoint joinPoint) {
        StringBuilder key = new StringBuilder();
        Object[] args = joinPoint.getArgs();
        for (Object arg : args) {
            if (arg != null) {
                key.append(arg.toString()).append(":");
            }
        }
        return key.toString();
    }
} 