package com.boss.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@TableName("sys_user")
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
    
    private String username;
    
    private String password;
    
    private String email;
    
    private String phone;
    
    private Integer status;
} 