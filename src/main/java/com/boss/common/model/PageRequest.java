package com.boss.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PageRequest extends BaseRequest {
    
    /**
     * 是否需要分页
     */
    private Boolean needPaging = true;
    
    /**
     * 是否查询总记录数
     */
    private Boolean searchCount = true;
    
    /**
     * 是否优化 COUNT SQL
     */
    private Boolean optimizeCountSql = true;
    
    /**
     * 当前页参数名称
     */
    private String pageParamName = "pageNum";
    
    /**
     * 每页数量参数名称
     */
    private String sizeParamName = "pageSize";
    
    /**
     * 排序参数名称
     */
    private String orderByParamName = "orderBy";
} 