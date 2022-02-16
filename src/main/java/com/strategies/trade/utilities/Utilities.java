package com.strategies.trade.utilities;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author hemasundarpenugonda
 */
public class Utilities {

    public static String timeConversion(long milliSeconds) {

        final int MINUTES_IN_AN_HOUR = 60;
        final int SECONDS_IN_A_MINUTE = 60;
        final int MIILLIS_IN_A_SEC = 1000;

        long seconds = milliSeconds / MIILLIS_IN_A_SEC;
        int minutes = (int) (seconds / SECONDS_IN_A_MINUTE);
        seconds -= minutes * SECONDS_IN_A_MINUTE;

        int hours = minutes / MINUTES_IN_AN_HOUR;
        minutes -= hours * MINUTES_IN_AN_HOUR;

        return hours + ":" + minutes + ":" + (int) seconds;
    }

    public static String dateConversion(long unixTimeStamp) {
        return new Date(unixTimeStamp).toString();
    }

    public static String readFile(String path) {
        return readFile(new File(path));
    }

    public static String readFile(File file) {
        String content = "";
        try {
            content = new Scanner(file).useDelimiter("\\Z").next();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeFile(String filePath, String content) {
        writeFile(new File(filePath), content);
    }

    public static void writeFile(File file, String content) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getEffectiveConfig(String keyValue, String configFileValue) {
        String property = System.getProperty(keyValue);
        String env = System.getenv(keyValue);
        if (env != null && !env.equalsIgnoreCase("")) {
            return env;
        } else if (property != null) {
            if (!property.equalsIgnoreCase("")) {
                return property;
            }
        }
        return configFileValue;
    }

    public static File zipDirectory(String sourceDirectoryPath, String zipPath) throws IOException {
        Path zipFilePath = Files.createFile(Paths.get(zipPath));

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(zipFilePath))) {
            Path sourceDirPath = Paths.get(sourceDirectoryPath);

            Files.walk(sourceDirPath).filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(sourceDirPath.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            zipOutputStream.write(Files.readAllBytes(path));
                            zipOutputStream.closeEntry();

                        } catch (Exception e) {
                            System.err.println(e);
                        }
                    });
        }

        return zipFilePath.toFile();
    }

}
