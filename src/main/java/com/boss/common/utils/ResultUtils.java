package com.boss.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.boss.common.model.PageResult;
import com.boss.common.model.Result;
import java.util.List;

public class ResultUtils {
    /**
     * 返回数据
     */
    public static <T> Result<T> toResult(T data) {
        return Result.success(data);
    }

    /**
     * 返回布尔结果
     */
    public static <T> Result<T> toResult(boolean result) {
        return result ? Result.success() : Result.failed();
    }

    /**
     * 返回分页数据
     */
    public static <T> Result<PageResult<T>> toPageResult(IPage<T> page) {
        PageResult<T> result = new PageResult<>();
        result.setRecords(page.getRecords());
        result.setTotal(page.getTotal());
        result.setCurrent((int) page.getCurrent());
        result.setSize((int) page.getSize());
        return Result.success(result);
    }

    /**
     * 返回列表数据
     */
    public static <T> Result<List<T>> toListResult(List<T> list) {
        return Result.success(list);
    }
} 