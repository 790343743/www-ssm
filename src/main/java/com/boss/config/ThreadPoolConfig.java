package com.boss.config;

import com.boss.config.properties.ThreadPoolProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class ThreadPoolConfig {

    private final ThreadPoolProperties threadPoolProperties;

    @Bean("commonExecutor")
    public Executor commonExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 从配置文件中读取参数
        executor.setCorePoolSize(threadPoolProperties.getCommon().getCoreSize());
        executor.setMaxPoolSize(threadPoolProperties.getCommon().getMaxSize());
        executor.setQueueCapacity(threadPoolProperties.getCommon().getQueueCapacity());
        executor.setThreadNamePrefix(threadPoolProperties.getCommon().getNamePrefix());
        executor.setKeepAliveSeconds(threadPoolProperties.getCommon().getKeepAlive());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }

    @Bean("asyncExecutor")
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // 从配置文件中读取参数
        executor.setCorePoolSize(threadPoolProperties.getAsync().getCoreSize());
        executor.setMaxPoolSize(threadPoolProperties.getAsync().getMaxSize());
        executor.setQueueCapacity(threadPoolProperties.getAsync().getQueueCapacity());
        executor.setThreadNamePrefix(threadPoolProperties.getAsync().getNamePrefix());
        executor.setKeepAliveSeconds(threadPoolProperties.getAsync().getKeepAlive());
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        return executor;
    }
} 