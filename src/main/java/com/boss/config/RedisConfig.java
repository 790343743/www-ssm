package com.boss.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import org.springframework.data.redis.connection.RedisNode;
import java.util.stream.Collectors;

@Configuration
public class RedisConfig {
    
    @Autowired
    private RedisProperties redisProperties;

    @Bean
    @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "standalone", matchIfMissing = true)
    public RedisConnectionFactory standaloneRedisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(redisProperties.getHost());
        config.setPort(redisProperties.getPort());
        config.setDatabase(redisProperties.getDatabase());
        if (redisProperties.getPassword() != null) {
            config.setPassword(redisProperties.getPassword());
        }
        return new LettuceConnectionFactory(config);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "sentinel")
    public RedisConnectionFactory sentinelRedisConnectionFactory() {
        RedisSentinelConfiguration config = new RedisSentinelConfiguration();
        config.setMaster(redisProperties.getSentinel().getMaster());
        config.setSentinels(redisProperties.getSentinel().getNodes().stream()
                .map(node -> new RedisNode(node.split(":")[0], Integer.parseInt(node.split(":")[1])))
                .collect(Collectors.toList()));
        if (redisProperties.getPassword() != null) {
            config.setPassword(redisProperties.getPassword());
        }
        return new LettuceConnectionFactory(config);
    }

    @Bean
    @ConditionalOnProperty(name = "spring.redis.mode", havingValue = "cluster")
    public RedisConnectionFactory clusterRedisConnectionFactory() {
        RedisClusterConfiguration config = new RedisClusterConfiguration(
                redisProperties.getCluster().getNodes());
        if (redisProperties.getPassword() != null) {
            config.setPassword(redisProperties.getPassword());
        }
        return new LettuceConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // 使用Jackson2JsonRedisSerializer来序列化和反序列化redis的value值
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
        serializer.setObjectMapper(mapper);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        // key采用String的序列化方式
        template.setKeySerializer(stringRedisSerializer);
        // hash的key也采用String的序列化方式
        template.setHashKeySerializer(stringRedisSerializer);
        // value序列化方式采用jackson
        template.setValueSerializer(serializer);
        // hash的value序列化方式采用jackson
        template.setHashValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }
} 