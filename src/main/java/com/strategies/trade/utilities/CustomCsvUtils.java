package com.strategies.trade.utilities;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
public class CustomCsvUtils {

    public static void writeToCSV(Path path, List<List<String>> csvContent) throws IOException {
        String csvContentAsString = csvContent.stream()
                .map(item -> String.join(", ", item))
                .collect(Collectors.joining("\n"));

        Files.write(path, csvContentAsString.getBytes());

    }

    public static void writeToCSV(Path path, String csvContent) throws IOException {
        Files.write(path, csvContent.getBytes());
    }

    public static List<List<String>> readCSV(Path path, int headerCount) throws IOException {

        List<List<String>> collect = Files.lines(path)
                .skip(headerCount)
                .map(item -> Arrays.asList(item.split(",")))
                .collect(Collectors.toList());

        return collect;
    }


}
