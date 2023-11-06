package com.strategies.trade.utilities;

import com.strategies.trade.api_test_beans.CandleStick;
import com.strategies.trade.api_test_beans.ImprovedCandleStick;
import com.strategies.trade.api_utils.ApiRequests;
import com.strategies.trade.test_data_beans.Exchange;
import com.strategies.trade.test_data_beans.FilePaths;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
public class TechIndicatorUtils {

    public static List<List<ImprovedCandleStick>> convert(List<List<CandleStick>> candleSticks) {
        List<List<ImprovedCandleStick>> improvedCandleSticks = candleSticks.stream()
                .map(item -> item.stream().map(ImprovedCandleStick::new).collect(Collectors.toList()))
                .collect(Collectors.toList());
        return improvedCandleSticks;
    }

    public static List<ImprovedCandleStick> calculateEma(List<ImprovedCandleStick> historicalImprovedCandleSticks, int interval) {

        for (int i = 0; i < historicalImprovedCandleSticks.size(); i++) {
            if (i < interval) {
                System.out.println();
            } else {
                double close = historicalImprovedCandleSticks.get(i).getClosePrice();
                Double previousEma = ImprovedCandleStick.getPreviousEma(historicalImprovedCandleSticks, interval, i);

                double factor = (2.0 / (interval + 1));
                double currentEma = close * factor + (1 - factor) * previousEma;
                historicalImprovedCandleSticks.get(i).setEma(currentEma, interval);
            }
        }
        return historicalImprovedCandleSticks;
    }

    public static List<ImprovedCandleStick> calculateSignal(List<ImprovedCandleStick> historicalImprovedCandleSticks, int interval) {

        for (int i = 0; i < historicalImprovedCandleSticks.size(); i++) {
            ImprovedCandleStick currentImprovedCandleStick = historicalImprovedCandleSticks.get(i);
            if (i < interval) {
                System.out.println();
            } else {
                List<ImprovedCandleStick> collect = historicalImprovedCandleSticks.subList(i - 9, i);
                boolean nullExists = collect.stream()
                        .anyMatch(item -> item.getMacd() == null);
                Double currentSignal;
                if (nullExists) {
                    currentSignal = null;
                } else {
                    double macd = currentImprovedCandleStick.getMacd();
                    Double previousSignal = ImprovedCandleStick.getPreviousSignal(historicalImprovedCandleSticks, interval, i);

                    double factor = (2.0 / (interval + 1));
                    currentSignal = macd * factor + (1 - factor) * previousSignal;
                    currentImprovedCandleStick.setSignal(currentSignal);
                }
            }
            /*if (currentImprovedCandleStick.getMacd() == null) {
                System.out.println();
            } else {
                double macd = currentImprovedCandleStick.getMacd();
                Double previousSignal = ImprovedCandleStick.getPreviousSignal(historicalImprovedCandleSticks, interval, i);

                double factor = (2.0 / (interval + 1));
                double currentSignal = macd * factor + (1 - factor) * previousSignal;
                currentImprovedCandleStick.setEma(currentSignal, interval);
            }*/
        }
        return historicalImprovedCandleSticks;
    }

    public static List<ImprovedCandleStick> calculateMacd(List<ImprovedCandleStick> historicalImprovedCandleSticks) {
        return calculateMacd(historicalImprovedCandleSticks, 26, 12);
    }

    public static List<ImprovedCandleStick> calculateMacd(List<ImprovedCandleStick> historicalImprovedCandleSticks, int longTermInterval, int shortTermInterval) {

        for (int i = 0; i < historicalImprovedCandleSticks.size(); i++) {
            if (i < longTermInterval) {
                System.out.println();
            } else {
                Double longTermEma = historicalImprovedCandleSticks.get(i).getEma(longTermInterval);
                Double shortTermEma = historicalImprovedCandleSticks.get(i).getEma(shortTermInterval);
                historicalImprovedCandleSticks.get(i).setMacd(shortTermEma - longTermEma);
            }
        }
        return historicalImprovedCandleSticks;
    }

    public static List<ImprovedCandleStick> calculateRsi(List<ImprovedCandleStick> historicalImprovedCandleSticks, int interval) {

        for (int i = 0; i < historicalImprovedCandleSticks.size(); i++) {
            if (i <= interval) {
                System.out.println();
            } else {
                List<Double> collect = historicalImprovedCandleSticks.subList(i - interval - 1, i)
                        .stream()
                        .map(CandleStick::getClosePrice)
                        .collect(Collectors.toList());
                List<Double> upMoves = new ArrayList<>();
                List<Double> downMoves = new ArrayList<>();
                for (int j = 1; j < collect.size(); j++) {
                    double v = collect.get(j) - collect.get(j - 1);
                    if (v > 0) {
                        upMoves.add(v);
                        downMoves.add(0.0);
                    } else if (v < 0) {
                        upMoves.add(0.0);
                        downMoves.add(Math.abs(v));
                    } else {
                        upMoves.add(0.0);
                        downMoves.add(0.0);
                    }
                }
                double avgUpMoves = upMoves.stream()
                        .mapToDouble(item -> item)
                        .sum() / upMoves.size();
                double avgDownMoves = downMoves.stream()
                        .mapToDouble(item -> item)
                        .sum() / downMoves.size();

                double rs = avgUpMoves / avgDownMoves;
                double currentRsi = 100 * (1 - (1 / (1 + rs)));

                historicalImprovedCandleSticks.get(i).setRsi(currentRsi);
            }
        }
        return historicalImprovedCandleSticks;
    }

    public static void writeOrUpdateHistoricalData(List<String> allSecurities) throws IOException {
        for (String currentSecurity : allSecurities) {
            String csvFileName = Exchange.NSE.getDataFolderPath() + FilePaths.HISTORICAL_DATA_FOLDER + currentSecurity + ".csv";

            LocalDate endDateObj = LocalDate.now();
            List<CandleStick> latestHistoricalDataObject = new ArrayList<>();
            List<List<String>> latestHistoricalDataString = new ArrayList<>();
            latestHistoricalDataString.add(Arrays.asList("symbol",
                    "series",
                    "date",
                    "prevClose",
                    "openPrice",
                    "highPrice",
                    "lowPrice",
                    "lastPrice",
                    "closePrice",
                    "volume weighted average price",
                    "volume of trade",
                    "turnOverInRs",
                    "numberOfTrades",
                    "deliverableQty",
                    "dlyQtToTradedQtPercentage"));
            LocalDate startDateObj = null;
            if (Files.exists(Paths.get(csvFileName))) {
                // Get current security historical data
                // List<CandleStick> historicalCandleSticksBySecurity = JavaUtils.deSerialize(FilePaths.HISTORICAL_DATA_FOLDER_PATH + currentSecurity, CandleStick.class);
                latestHistoricalDataObject = CsvUtils.getSheetData(csvFileName, 1)
                        .stream()
                        .map(CandleStick::new)
                        .collect(Collectors.toList());

                LocalDate latestDateHistoryAvailable = latestHistoricalDataObject.stream()
                        .map(CandleStick::getDate)
                        .reduce((item1, item2) -> item1.isBefore(item2) ? item2 : item1).orElseThrow(RuntimeException::new);

                if (!latestDateHistoryAvailable.isEqual(endDateObj.minusDays(1))) {
                    startDateObj = latestDateHistoryAvailable.plusDays(1);
                } else {
                    continue;
                }

            } else {
                //Starting date for historical data
                startDateObj = LocalDate.of(2019, 1, 1);
            }

            //need to get historical data till current month end / date
            endDateObj = endDateObj.withDayOfMonth(endDateObj.lengthOfMonth());

            // get all 3 month timeframes from start date to end date
            List<LocalDate> threeMonthTimeFrames = new ArrayList<>();
            LocalDate tempDateObj = startDateObj;
            while (tempDateObj.isBefore(endDateObj)) {
                threeMonthTimeFrames.add(tempDateObj);
                tempDateObj = tempDateObj.plusMonths(3);
            }


            for (LocalDate currentThreeMonthTimeFrameStartDate : threeMonthTimeFrames) {
                List<CandleStick> data = ApiRequests.getStockHistoricalDataFor3Months(currentSecurity, currentThreeMonthTimeFrameStartDate);
                latestHistoricalDataObject.addAll(data);
                Files.write(Paths.get("OutputFiles/output_" + currentSecurity + "_" + currentThreeMonthTimeFrameStartDate + ".html"), data.toString().getBytes());
            }
            JavaUtils.hardWait(2);

            boolean isSymbolChanged = latestHistoricalDataObject.stream()
                    .anyMatch(item -> item.getSymbol().contains("SYMBOL CHANGE"));

            if (isSymbolChanged) {
                String latestSecurityName = latestHistoricalDataObject.stream().map(CandleStick::getSymbol).distinct().reduce((first, second) -> second)
                        .orElse(String.valueOf(-1));
                latestHistoricalDataObject
                        .forEach(item -> item.setSymbol(latestSecurityName));
            }

//                JavaUtils.serialize(fileName, latestHistoricalDataObject);
            List<List<String>> collect = latestHistoricalDataObject.stream().map(CandleStick::getAsList).collect(Collectors.toList());
            latestHistoricalDataString.addAll(collect);

                /*if (Files.exists(Paths.get(csvFileName))) {
                    Files.copy(Paths.get(csvFileName), Paths.get(csvFileName + ".bkp"), StandardCopyOption.REPLACE_EXISTING);
                }*/

            CsvUtils.writeCsv(csvFileName, latestHistoricalDataString);
        }
    }

}
