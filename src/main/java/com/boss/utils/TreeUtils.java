package com.boss.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 树形结构工具类
 */
public class TreeUtils {

    /**
     * 构建树形结构
     *
     * @param list 源数据集合
     * @param id 主键字段名
     * @param parentId 父级字段名
     * @param children 子级集合字段名
     * @return 树形结构数据
     */
    public static <T> List<T> buildTree(List<T> list, String id, String parentId, String children) {
        if (list == null || list.isEmpty()) {
            return new ArrayList<>();
        }

        // 按父ID分组
        Map<Object, List<T>> parentMap = list.stream()
                .collect(Collectors.groupingBy(item -> getFieldValue(item, parentId)));

        // 设置子节点
        list.forEach(item -> {
            Object itemId = getFieldValue(item, id);
            List<T> childrenList = parentMap.get(itemId);
            if (childrenList != null) {
                setFieldValue(item, children, childrenList);
            }
        });

        // 返回顶层节点
        return list.stream()
                .filter(item -> {
                    Object pid = getFieldValue(item, parentId);
                    return pid == null || "0".equals(pid.toString()) || "".equals(pid.toString());
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取字段值
     */
    private static <T> Object getFieldValue(T item, String fieldName) {
        try {
            return item.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)).invoke(item);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 设置字段值
     */
    private static <T> void setFieldValue(T item, String fieldName, Object value) {
        try {
            item.getClass().getMethod("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), value.getClass()).invoke(item, value);
        } catch (Exception e) {
            // 忽略异常
        }
    }
} 