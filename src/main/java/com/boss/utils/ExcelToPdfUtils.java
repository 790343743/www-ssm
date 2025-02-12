package com.boss.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class ExcelToPdfUtils {

    private static final String FONT = "STSong-Light";
    private static final String FONT_ENCODING = "UniGB-UCS2-H";

    /**
     * Excel转PDF
     *
     * @param file Excel文件
     * @param response HTTP响应
     * @param fileName 输出的PDF文件名
     */
    public static void convertToPdf(MultipartFile file, HttpServletResponse response, String fileName) 
            throws IOException, DocumentException {
        // 设置响应头
        response.setContentType("application/pdf");
        response.setCharacterEncoding("utf-8");
        fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8 + "").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".pdf");

        // 读取Excel
        Workbook workbook = new XSSFWorkbook(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);

        // 创建PDF文档
        Document document = new Document(PageSize.A4.rotate());
        PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // 设置中文字体
        BaseFont baseFont = BaseFont.createFont(FONT, FONT_ENCODING, BaseFont.NOT_EMBEDDED);
        com.itextpdf.text.Font font = new com.itextpdf.text.Font(baseFont, 10, com.itextpdf.text.Font.NORMAL);

        // 获取Excel的行数和列数
        int rows = sheet.getPhysicalNumberOfRows();
        int cols = 0;
        if (rows > 0) {
            Row row = sheet.getRow(0);
            cols = row.getPhysicalNumberOfCells();
        }

        // 创建PDF表格
        PdfPTable table = new PdfPTable(cols);
        table.setWidthPercentage(100);

        // 转换数据
        for (int i = 0; i < rows; i++) {
            Row row = sheet.getRow(i);
            if (row != null) {
                for (int j = 0; j < cols; j++) {
                    Cell cell = row.getCell(j);
                    String value = getCellValue(cell);
                    PdfPCell pdfCell = new PdfPCell(new Phrase(value, font));
                    pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    pdfCell.setPadding(5);
                    table.addCell(pdfCell);
                }
            }
        }

        document.add(table);
        document.close();
        workbook.close();
    }

    /**
     * 获取单元格的值
     */
    private static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        String value;
        switch (cell.getCellType()) {
            case STRING:
                value = cell.getStringCellValue();
                break;
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    value = cell.getDateCellValue().toString();
                } else {
                    value = String.valueOf((long) cell.getNumericCellValue());
                }
                break;
            case BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                value = cell.getCellFormula();
                break;
            default:
                value = "";
        }
        return value;
    }
} 