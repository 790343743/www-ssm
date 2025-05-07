package com.boss.aspect;

import com.boss.annotation.Cache;
import com.boss.utils.CacheKeyGenerator;
import com.boss.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * 缓存切面
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CacheAspect {

    private final RedisUtils redisUtils;

    @Around("@annotation(cache)")
    public Object around(ProceedingJoinPoint joinPoint, Cache cache) throws Throwable {
        if (!cache.useCache()) {
            return joinPoint.proceed();
        }

        // 生成缓存key
        String key = CacheKeyGenerator.generateKey(joinPoint, cache);

        // 获取缓存
        Object value = redisUtils.get(key);
        if (value != null) {
            return value;
        }

        // 执行方法
        value = joinPoint.proceed();

        // 设置缓存
        if (value != null) {
            redisUtils.set(key, value, cache.expire(), cache.timeUnit());
        }

        return value;
    }

    /**
     * 更新时删除缓存
     */
    public void deleteCache(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Cache cache = method.getAnnotation(Cache.class);
        if (cache != null && cache.deleteOnUpdate()) {
            String key = CacheKeyGenerator.generateKey(joinPoint, cache);
            redisUtils.del(key);
        }
    }

    /**
     * 删除时删除缓存
     */
    public void deleteCacheOnDelete(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Cache cache = method.getAnnotation(Cache.class);
        if (cache != null && cache.deleteOnDelete()) {
            String key = CacheKeyGenerator.generateKey(joinPoint, cache);
            redisUtils.del(key);
        }
    }
} 