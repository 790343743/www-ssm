package com.boss.common.enums;

import lombok.Getter;

@Getter
public enum ResultCode {
    SUCCESS(200, "操作成功"),
    FAILED(500, "操作失败"),
    VALIDATE_FAILED(404, "参数检验失败"),
    UNAUTHORIZED(401, "暂未登录或token已经过期"),
    FORBIDDEN(403, "没有相关权限"),
    
    // 业务异常
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_LOGIN_FAILED(1002, "用户名或密码错误"),
    USER_ACCOUNT_FORBIDDEN(1003, "用户账号已被禁用"),
    USER_NOT_LOGIN(1004, "用户未登录"),
    USER_NO_PERMISSION(1005, "用户无相关权限"),
    
    // 系统异常
    SYSTEM_ERROR(2001, "系统异常"),
    NETWORK_ERROR(2002, "网络异常"),
    DATABASE_ERROR(2003, "数据库异常"),
    
    // 参数异常
    PARAM_IS_INVALID(3001, "参数无效"),
    PARAM_IS_BLANK(3002, "参数为空"),
    PARAM_TYPE_ERROR(3003, "参数类型错误"),
    PARAM_NOT_COMPLETE(3004, "参数缺失");

    private final Integer code;
    private final String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 