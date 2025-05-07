package com.boss.utils;

import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.concurrent.TimeUnit;

/**
 * 数据字典工具类
 */
@Component
public class DictUtils {

    private final RedisTemplate<String, Object> redisTemplate;
    private static final String DICT_KEY_PREFIX = "dict:";
    private static final long DICT_EXPIRE_TIME = 24 * 60 * 60; // 24小时

    public DictUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 获取字典值
     */
    public String getDictValue(String dictType, String dictCode) {
        String key = DICT_KEY_PREFIX + dictType;
        Map<Object, Object> dictMap = (Map<Object, Object>) redisTemplate.opsForValue().get(key);
        if (dictMap != null) {
            DictItem dictItem = (DictItem) dictMap.get(dictCode);
            return dictItem != null ? dictItem.getValue() : null;
        }
        return null;
    }

    /**
     * 获取字典标签
     */
    public String getDictLabel(String dictType, String dictCode) {
        String key = DICT_KEY_PREFIX + dictType;
        Map<Object, Object> dictMap = (Map<Object, Object>) redisTemplate.opsForValue().get(key);
        if (dictMap != null) {
            DictItem dictItem = (DictItem) dictMap.get(dictCode);
            return dictItem != null ? dictItem.getLabel() : null;
        }
        return null;
    }

    /**
     * 获取字典列表
     */
    public List<DictItem> getDictList(String dictType) {
        String key = DICT_KEY_PREFIX + dictType;
        Map<Object, Object> dictMap = (Map<Object, Object>) redisTemplate.opsForValue().get(key);
        if (dictMap != null) {
            return new ArrayList<>(dictMap.values().stream()
                    .map(item -> (DictItem) item)
                    .collect(Collectors.toList()));
        }
        return new ArrayList<>();
    }

    /**
     * 设置字典
     */
    public void setDict(String dictType, Map<String, DictItem> dictMap) {
        String key = DICT_KEY_PREFIX + dictType;
        redisTemplate.opsForValue().set(key, dictMap, DICT_EXPIRE_TIME, TimeUnit.SECONDS);
    }

    /**
     * 删除字典
     */
    public void deleteDict(String dictType) {
        String key = DICT_KEY_PREFIX + dictType;
        redisTemplate.delete(key);
    }

    /**
     * 字典项
     */
    @Data
    public static class DictItem {
        private String code;
        private String value;
        private String label;
        private Integer sort;
        private String remark;
    }
} 