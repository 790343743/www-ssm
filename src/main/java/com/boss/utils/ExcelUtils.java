package com.boss.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ExcelUtils {
    
    /**
     * 导出Excel
     *
     * @param response HttpServletResponse
     * @param data 数据List
     * @param fileName 文件名
     * @param sheetName sheet名
     * @param clazz 实体类
     */
    public static <T> void exportExcel(HttpServletResponse response, List<T> data, String fileName,
                                     String sheetName, Class<T> clazz) throws IOException {
        // 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

        // 设置样式
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        HorizontalCellStyleStrategy styleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);

        // 导出
        EasyExcel.write(response.getOutputStream(), clazz)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet(sheetName)
                .registerWriteHandler(styleStrategy)
                .doWrite(data);
    }

    /**
     * 导入Excel
     *
     * @param file 上传的文件
     * @param clazz 实体类
     * @return 数据List
     */
    public static <T> List<T> importExcel(MultipartFile file, Class<T> clazz) throws IOException {
        return EasyExcel.read(file.getInputStream())
                .head(clazz)
                .sheet()
                .doReadSync();
    }
} 