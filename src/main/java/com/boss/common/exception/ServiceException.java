package com.boss.common.exception;

import lombok.Getter;

@Getter
public class ServiceException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;
    private String message;

    public ServiceException(String message) {
        this.message = message;
        this.code = 500;
    }

    public ServiceException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }
} 