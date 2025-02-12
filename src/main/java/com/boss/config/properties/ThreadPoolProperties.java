package com.boss.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "thread-pool")
public class ThreadPoolProperties {
    
    private Pool common;
    private Pool async;
    
    @Data
    public static class Pool {
        private int coreSize;
        private int maxSize;
        private int queueCapacity;
        private int keepAlive;
        private String namePrefix;
    }
} 