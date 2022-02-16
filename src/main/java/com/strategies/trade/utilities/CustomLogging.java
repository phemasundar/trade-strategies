package com.strategies.trade.utilities;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class CustomLogging {

    public static void writeToTestNGReport(String message) {
        String fullMessage = writeLog(message);
    }

    public static String writeLog(Object message) {
        return writeLog("", message, new String[0]);
    }

    public static String writeLog(String message, String[] testDataToHighlight) {
        return writeLog("", message, testDataToHighlight);
    }

    public static String writeLog(String prefix, String message) {
        return writeLog(prefix, message, new String[0]);
    }

    public static String writeLog(String prefix, Object message, String[] testDataToHighlight) {
        return writeLog(Level.INFO, prefix, message, testDataToHighlight);
    }

    public static String writeLog(Level logLevel, String prefix, Object message, String[] testDataToHighlight) {
        String[] var4 = testDataToHighlight;
        int var5 = testDataToHighlight.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String str = var4[var6];
            message = message.toString().replace(str, " <b>" + str + "</b>");
        }

        String fullMessage = message.toString();
        if (!prefix.trim().equalsIgnoreCase("")) {
            fullMessage = prefix + ": " + message;
        }

        LogManager.getLogger().log(logLevel, fullMessage);
        return fullMessage;
    }

}

