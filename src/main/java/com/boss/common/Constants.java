package com.boss.common;

public interface Constants {
    /**
     * 通用常量
     */
    interface Common {
        String SUCCESS = "操作成功";
        String FAILURE = "操作失败";
        String LOGIN_SUCCESS = "登录成功";
        String LOGOUT_SUCCESS = "退出成功";
        String LOGIN_FAILED = "登录失败";
    }

    /**
     * 数据库相关常量
     */
    interface Database {
        String LOGIC_DELETE_FIELD = "deleted";
        Integer LOGIC_DELETE_VALUE = 1;
        Integer LOGIC_NOT_DELETE_VALUE = 0;
    }

    /**
     * 缓存相关常量
     */
    interface Cache {
        String TOKEN_PREFIX = "token:";
        String USER_PREFIX = "user:";
        Long TOKEN_EXPIRE = 7200L; // 2小时
    }

    /**
     * 安全相关常量
     */
    interface Security {
        String TOKEN_HEADER = "Authorization";
        String TOKEN_PREFIX = "Bearer ";
        String SECRET = "your-secret-key";
    }
} 