package com.boss.common.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /**
     * 当前页码
     */
    private Integer pageNum = 1;
    
    /**
     * 每页数量
     */
    private Integer pageSize = 10;
    
    /**
     * 排序字段
     */
    private String orderBy;
    
    /**
     * 排序方式：asc/desc
     */
    private String orderType;
} 