package com.boss.config.enums;

public enum DynamicTableTreadLocal {
    INSTANCE;
    
    private final ThreadLocal<String> tableNameThreadLocal = new ThreadLocal<>();
    
    public void setTableName(String tableName) {
        tableNameThreadLocal.set(tableName);
    }
    
    public String getTableName() {
        return tableNameThreadLocal.get();
    }
    
    public void remove() {
        tableNameThreadLocal.remove();
    }
} 