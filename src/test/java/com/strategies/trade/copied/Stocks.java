package com.strategies.trade.copied;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strategies.trade.api_test_beans.TradingviewClosePrice;
import com.strategies.trade.api_utils.ApiUrl;
import com.strategies.trade.api_utils.RestApiUtils;
import com.strategies.trade.utilities.ExcelUtils;
import org.apache.http.HttpResponse;
import org.apache.poi.ss.usermodel.*;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
public class Stocks {
    @Test
    public void getClosePrice() throws IOException {
        String excelPath = "/Users/hemasundarpenugonda/Documents/Stocks.xlsx";
        Workbook workBook = ExcelUtils.getWorkBook(excelPath);
        Sheet stock_data = ExcelUtils.getSheet(workBook, "Stock data");
//        String[][] sheetData = ExcelUtils.getSheetData(stock_data, 1);

        DataFormatter dataFormatter = new DataFormatter();
        String[][] sheetData = new String[stock_data.getPhysicalNumberOfRows()-1][ExcelUtils.getNumberOfColumns(stock_data)];

        for (Row row : stock_data) {
            //first 2(headerRows) rows are for headings
            if (row.getRowNum() < 1)
                continue;

            String[] currentRow = new String[ExcelUtils.getNumberOfColumns(stock_data)];
            for (Cell cell : row)
                currentRow[cell.getColumnIndex()] = dataFormatter.formatCellValue(cell);

            sheetData[row.getRowNum() - 1] = currentRow;
        }


        String collect = Arrays.stream(sheetData)
                .map(item -> "NSE:" + item[1])
                .collect(Collectors.joining("\",\""));
        collect = "\"" + collect + "\"";
        System.out.println(collect);

        HttpResponse httpResponse = RestApiUtils.sendingPostRequest(ApiUrl.TRADING_VIEW_SCAN_URL, "");
//        CustomResponse<LRLocationDetailsResponse> lrLocationDetailsResponseCustomResponse = new CustomResponse<>(response, LRLocationDetailsResponse.class);
        String x = RestApiUtils.getResponse(httpResponse);

        System.out.println(x);

        ObjectMapper mapper = new ObjectMapper();

        TradingviewClosePrice tradingviewClosePrice = mapper.readValue(x, TradingviewClosePrice.class);

        int lastRowNum = stock_data.getPhysicalNumberOfRows();

        for (int i = 1; i < lastRowNum; i++) {
            Row currentRow = stock_data.getRow(i);
            short lastCellNum = currentRow.getLastCellNum();
            for (int j = 0; j <= lastCellNum; j++) {
                String exchange = currentRow.getCell(0).getStringCellValue();
                String stockName = currentRow.getCell(1).getStringCellValue();
                int closePrice = Math.round(Float.parseFloat(tradingviewClosePrice.getClosePrice(exchange, stockName)));
                currentRow.getCell(3).setCellValue(closePrice);
            }
        }

        try {
            FileOutputStream out = new FileOutputStream(new File("/Users/hemasundarpenugonda/Documents/Stocks1.xlsx"));
            workBook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }


//        List<List<String>> collect1 = Arrays.stream(sheetData)
//                .map(item -> Arrays.asList(item))
//                .collect(Collectors.toList());
//
//        List<List<String>> collect2 = collect1.stream()
//                .map(item -> {
//                    ArrayList<String> strings = new ArrayList<>(item);
//                    strings.add(4, tradingviewClosePrice.getClosePrice(item.get(0), item.get(1)));
//                    return strings;
//                })
//                .collect(Collectors.toList());
//
//        ExcelUtils.writeSheet(stock_data, collect2);
//        ExcelUtils.writeExcel(workBook, "/Users/hemasundarpenugonda/Documents/Stocks1.xlsx");
    }


}
