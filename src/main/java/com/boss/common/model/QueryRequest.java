package com.boss.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class QueryRequest extends PageRequest {
    
    /**
     * 查询关键字
     */
    private String keyword;
    
    /**
     * 开始时间
     */
    private String beginTime;
    
    /**
     * 结束时间
     */
    private String endTime;
    
    /**
     * 状态（0正常 1停用）
     */
    private String status;
    
    /**
     * 删除标志（0代表存在 1代表删除）
     */
    private String delFlag;
} 