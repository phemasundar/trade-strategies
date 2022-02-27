package com.strategies.trade.utilities;

import com.strategies.trade.api_test_beans.CandleStick;
import com.strategies.trade.api_test_beans.ImprovedCandleStick;
import com.strategies.trade.api_utils.ApiRequests;
import com.strategies.trade.test_data_beans.Exchange;
import com.strategies.trade.test_data_beans.FilePaths;
import com.strategies.trade.test_data_beans.Securities;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
public class HistoricalDataUtils {
    public static List<List<CandleStick>> getBseDataInJavaObjects(List<String> securitiesList) throws IOException {
        Instant start = Instant.now();

        Collection<List<CandleStick>> values = Files.list(Paths.get(Exchange.BSE.getDataFolderPath() + FilePaths.DAILY_EXTRACTED_FILES_FOLDER))
                .filter(Files::isRegularFile)
                .map(item -> {
                    try {
                        return CsvUtils.getSheetData(item.toAbsolutePath().toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .map(item ->
                                item.stream()
                                        .skip(1)
                                        .filter(item1 -> item1.size() >= 16)
                                        .filter(item1 -> {
                                            try {
//                                                return securitiesList.contains(item1.getSymbol());
                                                return securitiesList.contains(item1.get(0));
                                            } catch (DateTimeParseException e) {
                                                e.printStackTrace();
                                                System.out.println();
                                                return false;
                                            }
                                        })
                                        .map(item1 -> new CandleStick(new String[]{item1.get(0), item1.get(1), item1.get(15), item1.get(9), item1.get(4), item1.get(5), item1.get(6), item1.get(8), item1.get(7), item1.get(12), item1.get(10)}))

                )
                .flatMap(item -> item)
                .collect(Collectors.groupingBy(item -> item.getSymbol())).values();
        List<List<CandleStick>> allData = new ArrayList<>(values);

        CustomLogging.writeLog("Time taken for reading BSE data " + Duration.between(start, Instant.now()).getSeconds());

         return allData;

    }

    public static List<List<CandleStick>> readNSEHistoricalData(List<String> allSecurities) throws IOException {

        List<List<CandleStick>> allHistoricalCandleSticks = new ArrayList<>();

        for (String currentSecurity : allSecurities) {
            String excelFileName = Exchange.NSE.getDataFolderPath() + FilePaths.HISTORICAL_DATA_FOLDER + currentSecurity + ".csv";
//            List<CandleStick> historicalCandleSticks = JavaUtils.deSerialize(FilePaths.HISTORICAL_DATA_FOLDER_PATH + currentSecurity, CandleStick.class);
            List<List<String>> sheetData = CsvUtils.getSheetData(excelFileName, 1);
            List<CandleStick> historicalCandleSticks = sheetData.stream()
                    .map(CandleStick::new)
                    .collect(Collectors.toList());

            allHistoricalCandleSticks.add(historicalCandleSticks);
        }

        return allHistoricalCandleSticks;

    }

    public static void percentageChangeOverTime(Exchange exchange, Securities securityType, String indexName, LocalDate date) throws IOException, ClassNotFoundException {
        Period duration = Period.between(LocalDate.now(), date);
        List<List<String>> list = new ArrayList<>();
        list.add(new ArrayList<>(Arrays.asList("Symbol",
                "Name",
                "Max Price",
                "Max Price on",
                "Least Price",
                "Least Price on",
                "Price on " + duration + " before",
                "Date on " + duration + " before",
                "Latest Close Price",
                " % Up from Low",
                " % Down from Max",
                " % Change from last " + duration)));
        List<List<CandleStick>> historicalCandleSticks = exchange.getHistoricalData(securityType, indexName);
        List<List<CandleStick>> historicalCandleSticksFromDate = CandleStick.getDataFromDateForListOfScrips(historicalCandleSticks, date);
        List<String> allSymbolNames = CandleStick.getAllSymbolNames(historicalCandleSticks);
        for (List<CandleStick> indHistoryData : historicalCandleSticksFromDate) {
            if (indHistoryData.size() == 0) {
                continue;
            }
            CandleStick maxCandleFromDate = null;
            try {
                maxCandleFromDate = CandleStick.getMaxCandle(indHistoryData);
            } catch (NoSuchElementException e) {
                e.printStackTrace();
            }
            CandleStick minCandleFromDate = CandleStick.getMinCandle(indHistoryData);
            CandleStick candleOnDate = CandleStick.getCandleByDate(indHistoryData, date);

            double latestCloseValue;
            //Get last traded price
            CandleStick latestCandle = CandleStick.getLatestCandle(indHistoryData);
            if (securityType == Securities.ALL) {
                latestCloseValue = latestCandle.getClosePrice();
            } else {
                latestCloseValue = Double.parseDouble(Objects.requireNonNull(ApiRequests.getGoogleFinanceClosePrice(exchange, latestCandle.getSymbol())));
                /*CustomResponse<TradingviewClosePrice> closePrices = ApiRequests.getTradingViewClosePrice(exchange, allSymbolNames);
                latestCloseValue = closePrices.getResponseObj().getData().stream()
                        .filter(item -> item.getS().substring(item.getS().indexOf(":") + 1).trim().equalsIgnoreCase(CandleStick.getSymbol(indHistoryData)))
                        .map(item -> item.getD().get(0))
                        .mapToDouble(Double::parseDouble)
                        .findFirst().getAsDouble();*/
            }
            double downInPriceFromMax = maxCandleFromDate.getClosePrice() - latestCloseValue;
            double downByPercentageFromMax = downInPriceFromMax * 100 / maxCandleFromDate.getClosePrice();
            downByPercentageFromMax = BigDecimal.valueOf(downByPercentageFromMax).setScale(2, RoundingMode.HALF_UP).doubleValue();

            double upInPriceFromLow = latestCloseValue - minCandleFromDate.getClosePrice();
            double upByPercentageFromLow = upInPriceFromLow * 100 / minCandleFromDate.getClosePrice();
            upByPercentageFromLow = BigDecimal.valueOf(upByPercentageFromLow).setScale(2, RoundingMode.HALF_UP).doubleValue();

            double upInPriceFromDate = latestCloseValue - candleOnDate.getClosePrice();
            double upByPercentageFromDate = upInPriceFromDate * 100 / candleOnDate.getClosePrice();
            upByPercentageFromDate = BigDecimal.valueOf(upByPercentageFromDate).setScale(2, RoundingMode.HALF_UP).doubleValue();

            System.out.println("Symbol: " + CandleStick.getSymbol(indHistoryData) + "\n\tPercentage Down: " + downByPercentageFromMax + "\n\tMax Price: " + maxCandleFromDate.getClosePrice() + "\n\tLatestClose Price: " + latestCloseValue);
            list.add(new ArrayList<>(
                    Arrays.asList(CandleStick.getSymbol(indHistoryData),
                            CandleStick.getName(indHistoryData),
                            String.valueOf(maxCandleFromDate.getClosePrice()),
                            String.valueOf(maxCandleFromDate.getDate()),
                            String.valueOf(minCandleFromDate.getClosePrice()),
                            String.valueOf(minCandleFromDate.getDate()),
                            String.valueOf(candleOnDate.getClosePrice()),
                            String.valueOf(candleOnDate.getDate()),
                            String.valueOf(latestCloseValue),
                            String.valueOf(upByPercentageFromLow),
                            String.valueOf(downByPercentageFromMax),
                            String.valueOf(upByPercentageFromDate))));

        }

        String collect = list.stream()
                .map(item -> item.toString().replace("[", "").replace("]", ""))
                .collect(Collectors.joining("\n"));
        System.out.println(collect);
        Files.write(Paths.get("percentageChangesIn" + duration + ".csv"), collect.getBytes());
    }

    public static void percentageChangeFromNumberOfMonths(Exchange exchange, Securities securityType, String indexName, int months) throws IOException, ClassNotFoundException {
        percentageChangeOverTime(exchange, securityType, indexName, LocalDate.now().minusMonths(months));
    }

    public static void percentageChangeFromNumberOfWeeks(Exchange exchange, Securities securityType, String indexName, int weeks) throws IOException, ClassNotFoundException {
        percentageChangeOverTime(exchange, securityType, indexName, LocalDate.now().minusWeeks(weeks));
    }

    public static void percentageChangeForCurrentWeek(Exchange exchange, Securities securityType, String indexName) throws IOException, ClassNotFoundException {
        percentageChangeOverTime(exchange, securityType, indexName, LocalDate.now().with(DayOfWeek.MONDAY));
    }

    public static List<List<ImprovedCandleStick>> saveEma(List<List<ImprovedCandleStick>> historicalImprovedCandleSticks, int interval) throws IOException, ClassNotFoundException {

        for (List<ImprovedCandleStick> indHistoricalImprovedCandleSticks : historicalImprovedCandleSticks) {
            List<ImprovedCandleStick> improvedHistoricalCandleSticks = TechIndicatorUtils.calculateEma(indHistoricalImprovedCandleSticks, interval);
            System.out.println(improvedHistoricalCandleSticks);
        }
        return historicalImprovedCandleSticks;
    }

    public static List<List<ImprovedCandleStick>> saveMacd(List<List<ImprovedCandleStick>> historicalImprovedCandleSticks) throws IOException, ClassNotFoundException {
        for (List<ImprovedCandleStick> indHistoricalImprovedCandleSticks : historicalImprovedCandleSticks) {
            List<ImprovedCandleStick> improvedHistoricalCandleSticks = TechIndicatorUtils.calculateMacd(indHistoricalImprovedCandleSticks);
            System.out.println(improvedHistoricalCandleSticks);
        }
        return historicalImprovedCandleSticks;
    }

    public static List<List<ImprovedCandleStick>> saveSignal(List<List<ImprovedCandleStick>> historicalImprovedCandleSticks) throws IOException, ClassNotFoundException {
        for (List<ImprovedCandleStick> indHistoricalImprovedCandleSticks : historicalImprovedCandleSticks) {
            List<ImprovedCandleStick> improvedHistoricalCandleSticks = TechIndicatorUtils.calculateSignal(indHistoricalImprovedCandleSticks, 9);
            System.out.println(improvedHistoricalCandleSticks);
        }
        return historicalImprovedCandleSticks;
    }

    public static List<List<ImprovedCandleStick>> saveRsi(List<List<ImprovedCandleStick>> historicalImprovedCandleSticks) throws IOException, ClassNotFoundException {
        for (List<ImprovedCandleStick> indHistoricalImprovedCandleSticks : historicalImprovedCandleSticks) {
            List<ImprovedCandleStick> improvedHistoricalCandleSticks = TechIndicatorUtils.calculateRsi(indHistoricalImprovedCandleSticks, 14);
            System.out.println(improvedHistoricalCandleSticks);
        }
        return historicalImprovedCandleSticks;
    }
}
