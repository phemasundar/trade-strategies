package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.formula.functions.T;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
//@NoArgsConstructor
public class CandleStick implements Serializable, Comparable<CandleStick> {

    private static final long serialVersionUID = 1L;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    private static DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("dd-MMM-yy");

    private String symbol;
    private String name;
    private LocalDate date;
    private Double prevClose;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double lastPrice;
    private Double closePrice;
    private Double turnOverInRs;
    private Long numberOfTrades;

    public CandleStick(List<String> item) {
        try {
            setSymbol(item.get(0));
            setName(item.get(1));
            setDate(item.get(2));
            setPrevClose(item.get(3));
            setOpenPrice(item.get(4));
            setHighPrice(item.get(5));
            setLowPrice(item.get(6));
            setLastPrice(item.get(7));
            setClosePrice(item.get(8));
            setTurnOverInRs(item.get(9));
            setNumberOfTrades(item.get(10));
        } catch (IndexOutOfBoundsException | DateTimeParseException e) {
            System.out.println(item);
            e.printStackTrace();
        }
    }

    public CandleStick(CandleStick item) {
        try {
            setSymbol(item.getSymbol());
            setName(item.getName());
            setDate(item.getDate().format(formatter));
            setPrevClose(item.getPrevClose().toString());
            setOpenPrice(item.getOpenPrice().toString());
            setHighPrice(item.getHighPrice().toString());
            setLowPrice(item.getLowPrice().toString());
            setLastPrice(item.getLastPrice().toString());
            setClosePrice(item.getClosePrice().toString());
            setTurnOverInRs(item.getTurnOverInRs().toString());
            setNumberOfTrades(item.getNumberOfTrades().toString());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public List<String> getAsList() {
            List<String> res = Arrays.asList(this.getSymbol(),
                    this.getDate().format(formatter),
                    this.getPrevClose().toString(),
                    this.getOpenPrice().toString(),
                    this.getHighPrice().toString(),
                    this.getLowPrice().toString(),
                    this.getLastPrice().toString(),
                    this.getClosePrice().toString(),
                    new BigDecimal(this.getTurnOverInRs().toString()).stripTrailingZeros().toPlainString(),
                    this.getNumberOfTrades().toString());
            return res;
    }

    public void setDate(String date) {
        if (date.contains(">"))
            date = date.substring(date.lastIndexOf(">") + 1);
        try {
            this.date = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            try {
                this.date = LocalDate.parse(date, formatter1);
            } catch (DateTimeParseException e1) {
                e1.printStackTrace();
                throw e;
            }
        }
    }

    public void setPrevClose(String prevClose) {
        this.prevClose = Double.valueOf(prevClose.replace(",", ""));
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = Double.valueOf(openPrice.replace(",", ""));
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = Double.valueOf(highPrice.replace(",", ""));
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = Double.valueOf(lowPrice.replace(",", ""));
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = Double.valueOf(lastPrice.replace(",", ""));
    }

    public void setClosePrice(String closePrice) {
        this.closePrice = Double.valueOf(closePrice.replace(",", ""));
    }

    public void setTurnOverInRs(String turnOverInRs) {
        this.turnOverInRs = Double.valueOf(turnOverInRs.replace(",", "").trim());
    }

    public void setNumberOfTrades(String numberOfTrades) {
        this.numberOfTrades = Long.valueOf(numberOfTrades.replace(",", ""));
    }

    @Override
    public String toString() {
        return symbol + ", "
                + date + ", "
                + prevClose + ", "
                + openPrice + ", "
                + highPrice + ", "
                + lowPrice + ", "
                + lastPrice + ", "
                + closePrice + ", "
                + turnOverInRs + ", "
                + numberOfTrades;
    }

    @Override
    public int compareTo(@NotNull CandleStick NSECandleStick) {
        if (this.getDate().isBefore(NSECandleStick.getDate()))
            return 1;
        else if (this.getDate().isBefore(NSECandleStick.getDate()))
            return -1;
        else
            return 0;
    }

    //Utility methods
    public static String getSymbol(List<? extends CandleStick> candleSticks) {
        return candleSticks.get(candleSticks.size() - 1).getSymbol();
    }

    public static String getName(List<? extends CandleStick> candleSticks) {
        return candleSticks.get(candleSticks.size() - 1).getName();
    }

    public static List<String> getAllSymbolNames(List<List<CandleStick>> historicalCandleSticks) {
        List<String> allSymbolNames = historicalCandleSticks.stream()
                .map(item -> item.get(item.size() - 1).getSymbol())
                .collect(Collectors.toList());
        return allSymbolNames;
    }

    public static List<CandleStick> getDataFromDate(List<CandleStick> indHistoryData, LocalDate date) {
        return indHistoryData.stream()
                .filter(item -> Objects.nonNull(item.getDate()))
                .filter(item -> item.getDate().isAfter(date))
                .collect(Collectors.toList());
    }

    public static List<List<CandleStick>> getDataOfLastNumOfMonthsFromListOfScrips (List<List<CandleStick>> indHistoryData, int numOfMonths) {
        List<List<CandleStick>> lastMonthsHistory = indHistoryData.stream()
                .map(item -> getDataFromDate(item, LocalDate.now().minusMonths(numOfMonths)))
                .collect(Collectors.toList());
        return lastMonthsHistory;
    }

    public static List<List<CandleStick>> getDataFromDateForListOfScrips (List<List<CandleStick>> indHistoryData, LocalDate date) {
        List<List<CandleStick>> lastMonthsHistory = indHistoryData.stream()
                .map(item -> getDataFromDate(item, date))
                .collect(Collectors.toList());
        return lastMonthsHistory;
    }

    public static CandleStick getMaxCandle (List<CandleStick> historyData) {
        CandleStick maxCandleInLast6Months = historyData.stream()
                .reduce((item1, item2) -> (item1.getClosePrice() > item2.getClosePrice() ? item1 : item2))
                .get();
        return maxCandleInLast6Months;
    }

    public static CandleStick getMinCandle (List<CandleStick> historyData) {
        CandleStick maxCandleInLast6Months = historyData.stream()
                .reduce((item1, item2) -> (item1.getClosePrice() < item2.getClosePrice() ? item1 : item2))
                .get();
        return maxCandleInLast6Months;
    }

    public static CandleStick getCandleByDate (List<CandleStick> historyData, LocalDate date) {
        CandleStick NSECandleStick = historyData.stream()
                .filter(item -> item.getDate().equals(date) || item.getDate().isAfter(date))
                .findFirst().orElse(null);
        return NSECandleStick;
    }

    public static CandleStick getLatestCandle (List<CandleStick> historyData) {
        CandleStick latestCandle = historyData.stream()
                .reduce((item1, item2) -> (item1.getDate().isAfter(item2.getDate()) ? item1 : item2))
                .get();
        return latestCandle;
    }
}

