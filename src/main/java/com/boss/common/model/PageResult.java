package com.boss.common.model;

import lombok.Data;

import java.util.List;

@Data
public class PageResult<T> {
    /**
     * 总记录数
     */
    private Long total;
    
    /**
     * 当前页数据
     */
    private List<T> records;
    
    /**
     * 当前页
     */
    private Integer current;
    
    /**
     * 每页条数
     */
    private Integer size;
    
    public static <T> PageResult<T> build(List<T> records, Long total, Integer current, Integer size) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(records);
        result.setTotal(total);
        result.setCurrent(current);
        result.setSize(size);
        return result;
    }
} 