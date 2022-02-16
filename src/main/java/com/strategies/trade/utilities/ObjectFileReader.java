package com.strategies.trade.utilities;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * This class reads the PageObjectRepository text files. Uses buffer reader.
 *
 * @author hemasundarpenugonda
 */
public class ObjectFileReader {


    public static String setSpecFilePath(String folderName, String pageName) {
        return "src/main/resources/page_objects/" + folderName + File.separator + pageName + ".yaml";

    }

    //TODO: Need to refactor. To be deleted mostly.
    private static Stream<String[]> getSpecFileStream(String specFilePath) {
        try {
            return Files.lines(Paths.get(specFilePath))
                    .filter((str) -> !str.startsWith("=="))
                    .filter((str) -> !str.startsWith("#"))
                    .filter((str) -> !str.trim().equalsIgnoreCase(""))
                    .filter((str) -> !str.startsWith("Page Title:"))
                    .map(str -> str.replaceAll("[\\s]+", " "))
                    .map(str -> str.split(" ", 3));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    //TODO: To be Deleted
    private static String[] getElement1(String specFilePath, String elementName) {
        return getSpecFileStream(specFilePath)
                .filter(str -> str[0].equalsIgnoreCase(elementName))
                .findFirst()
                .get();
    }

}
