package com.strategies.trade.utilities;


import org.apache.commons.text.StringEscapeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
//TODO: Refactor
public class CsvUtils {

    public static int getNumberOfRows(String filePath) throws IOException {
        return Long.valueOf(Files.lines(Paths.get(filePath)).count()).intValue();
    }

    public static int getNumberOfColumns(String filePath) throws IOException {

        int size = Files.lines(Paths.get(filePath))
                .limit(1)
                .map(line -> Arrays.asList(line.split(",")))
                .collect(Collectors.toList()).get(0).size();

        return size;
    }

    public static List<List<String>> getSheetData(String filePath) throws IOException {
        List<List<String>> data = Files.lines(Paths.get(filePath))
                .filter(item -> !item.isEmpty())
                .map(line -> Arrays.asList(line.split(",")))
                .collect(Collectors.toList());

        return data;
    }

    public static void appendSheet(String filePath, List<List<String>> sheetData) throws IOException {
        writeCsv(filePath, sheetData);
    }

    public static void writeCsv(String filePath, List<List<String>> sheetData) throws IOException {

        List<String> collect = sheetData.stream()
                .map(row -> row.stream()
                        .map(cell -> cell.replace(",", " "))
                        .map(StringEscapeUtils::escapeCsv)
                        .collect(Collectors.joining(","))
                )
                .collect(Collectors.toList());
        JavaUtils.writeToFile(filePath, collect);

    }
}
