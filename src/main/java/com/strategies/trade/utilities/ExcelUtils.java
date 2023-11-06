package com.strategies.trade.utilities;

import com.strategies.trade.test_data_beans.Exchange;
import com.strategies.trade.test_data_beans.FilePaths;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */
//TODO: Refactor
public class ExcelUtils {

    public static Workbook getWorkBook(String path) {
        Workbook workBook = null;
        try {
            workBook = new XSSFWorkbook(path);
//            workBook = new XSSFWorkbook(new FileInputStream(path));
//            workBook = WorkbookFactory.create(new File(path));
        } catch (FileNotFoundException e) {
            return new XSSFWorkbook();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return workBook;
    }

    public static Sheet getSheet(Workbook workBook, String sheetName) {
        for (Sheet sheet : workBook) {
            if (sheet.getSheetName().equalsIgnoreCase(sheetName))
                return sheet;
        }
        return workBook.createSheet(sheetName);
    }

    public static int getNumberOfRows(Sheet sheet) {
        return sheet.getLastRowNum();
    }

    public static int getNumberOfColumns(Sheet sheet) {
        return sheet.getRow(0).getLastCellNum();
    }

    public static List<List<String>> getSheetData(Sheet sheet) {
        DataFormatter dataFormatter = new DataFormatter();
        List<List<String>> data = new ArrayList<>();

        for (Row row : sheet) {
            List<String> currentRow = new ArrayList<>();
            for (Cell cell : row)
                currentRow.add(dataFormatter.formatCellValue(cell));
            data.add(currentRow);
        }
        return data;
    }

    public static Sheet writeSheet(Sheet sheet, List<List<String>> sheetData) {
       return writeSheet(sheet, sheetData, 0);
    }

    public static Sheet appendSheet(Sheet sheet, List<List<String>> sheetData) {
       return writeSheet(sheet, sheetData, sheet.getLastRowNum()+1);
    }

    private static Sheet writeSheet(Sheet sheet, List<List<String>> sheetData, int currentLastRowNumber) {

        // Normal Cell Style
        CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
        Font font = sheet.getWorkbook().createFont();
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);
        cellStyle.setWrapText(true);

        // Header Cell Style
        CellStyle headerCellStyle = sheet.getWorkbook().createCellStyle();
        Font headerFont = sheet.getWorkbook().createFont();
        headerFont.setBold(true);
        headerCellStyle.setFont(font);

        int rowCount = currentLastRowNumber;

        for (List<String> currentRowData : sheetData) {
            Row row = sheet.createRow(rowCount++);

            int columnCount = 0;

            for (String field : currentRowData) {
                Cell cell = row.createCell(columnCount++);

                if (rowCount == 0)
                    cell.setCellStyle(headerCellStyle);
                else
                    cell.setCellStyle(cellStyle);

                cell.setCellValue(field);
            }
//            row.setHeight((short)-1);
        }

        //AutoAdjust Row height
        int totalNumberOfRows = sheet.getPhysicalNumberOfRows();
        for (int i = 0; i < totalNumberOfRows; i++) {
            sheet.getRow(i).setHeight((short) -1);
        }

        // Auto adjust column width of the sheet
        short numberOfColumns = sheet.getRow(sheet.getFirstRowNum()).getLastCellNum();
        for (int i = 1; i <= numberOfColumns; i++) {
            sheet.autoSizeColumn(i);
        }

        //Colouring alternate rows (like table view)
        SheetConditionalFormatting sheetCF = sheet.getSheetConditionalFormatting();

        // Condition 1: Formula Is   =A2=A1   (White Font)
        ConditionalFormattingRule rule1 = sheetCF.createConditionalFormattingRule("MOD(ROW(),2)");
        PatternFormatting fill1 = rule1.createPatternFormatting();
        fill1.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
        fill1.setFillPattern(PatternFormatting.SOLID_FOREGROUND);

        CellRangeAddress[] regions = {
                new CellRangeAddress(sheet.getFirstRowNum(), sheet.getLastRowNum(), sheet.getRow(sheet.getFirstRowNum()).getFirstCellNum(), sheet.getRow(sheet.getFirstRowNum()).getLastCellNum())
//                CellRangeAddress.valueOf("A1:Z100")
        };

        sheetCF.addConditionalFormatting(regions, rule1);


        return sheet;
    }

    public static void writeExcel(Workbook workbook, String path) {

        try {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File(path));
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static List<String> getAllSheetNames(Workbook workbook) {
        List<String> res = new ArrayList<>();
        for (Sheet sheet : workbook) {
            res.add(sheet.getSheetName());
        }
        return res;
    }

    //        @Test
    public void test() throws IOException, InvalidFormatException {
        Workbook workBook = ExcelUtils.getWorkBook(Exchange.NSE.getDataFolderPath() + FilePaths.HISTORICAL_DATA_FOLDER + "HistoricalData.xlsx");
        Sheet sheet = ExcelUtils.getSheet(workBook, "Test1");
        List<List<String>> collect = new ArrayList<>();
        collect.add(Arrays.asList("Test1", "Test2"));
        collect.add(Arrays.asList("Test3", "Test4"));
        Sheet rows = ExcelUtils.writeSheet(sheet, collect);
        ExcelUtils.writeExcel(workBook, Exchange.NSE.getDataFolderPath() + FilePaths.HISTORICAL_DATA_FOLDER + "HistoricalData.xlsx");

    }
}
