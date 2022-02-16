package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
//@NoArgsConstructor
public class NSECandleStick implements Serializable, Comparable<NSECandleStick> {

    private static final long serialVersionUID = 1L;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    private String symbol;
    private String series;
    private LocalDate date;
    private Double prevClose;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double lastPrice;
    private Double closePrice;
    //volume weighted average price
    private Double vwap;
    //volume of trade
    private Long totalTradedQuantity;
    private Double turnOverInRs;
    private Long numberOfTrades;
    private Long deliverableQty;
    private Double dlyQtToTradedQtPercentage;

    public NSECandleStick(List<String> item) {
        try {
            setSymbol(item.get(0));
            setSeries(item.get(1));
            setDate(item.get(2));
            setPrevClose(item.get(3));
            setOpenPrice(item.get(4));
            setHighPrice(item.get(5));
            setLowPrice(item.get(6));
            setLastPrice(item.get(7));
            setClosePrice(item.get(8));
            setVwap(item.get(9));
            setTotalTradedQuantity(item.get(10));
            setTurnOverInRs(item.get(11));
            setNumberOfTrades(item.get(12));
            setDeliverableQty(item.get(13));
            setDlyQtToTradedQtPercentage(item.get(14));
        } catch (IndexOutOfBoundsException | DateTimeParseException e) {
            System.out.println(item);
            e.printStackTrace();
        }
    }

    public NSECandleStick(NSECandleStick item) {
        try {
            setSymbol(item.getSymbol());
            setSeries(item.getSeries());
            setDate(item.getDate().format(formatter));
            setPrevClose(item.getPrevClose().toString());
            setOpenPrice(item.getOpenPrice().toString());
            setHighPrice(item.getHighPrice().toString());
            setLowPrice(item.getLowPrice().toString());
            setLastPrice(item.getLastPrice().toString());
            setClosePrice(item.getClosePrice().toString());
            setVwap(item.getVwap().toString());
            setTotalTradedQuantity(item.getTotalTradedQuantity().toString());
            setTurnOverInRs(item.getTurnOverInRs().toString());
            setNumberOfTrades(item.getNumberOfTrades().toString());
            setDeliverableQty(item.getDeliverableQty().toString());
            setDlyQtToTradedQtPercentage(item.getDlyQtToTradedQtPercentage().toString());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    public List<String> getAsList() {
            List<String> res = Arrays.asList(this.getSymbol(), getSeries(),
                    this.getDate().format(formatter),
                    this.getPrevClose().toString(),
                    this.getOpenPrice().toString(),
                    this.getHighPrice().toString(),
                    this.getLowPrice().toString(),
                    this.getLastPrice().toString(),
                    this.getClosePrice().toString(),
                    this.getVwap().toString(),
                    this.getTotalTradedQuantity().toString(),
                    new BigDecimal(this.getTurnOverInRs().toString()).stripTrailingZeros().toPlainString(),
                    this.getNumberOfTrades().toString(),
                    (this.getDeliverableQty() == null) ? "-":this.getDeliverableQty().toString(),
                    (this.getDlyQtToTradedQtPercentage() == null) ? "-":this.getDlyQtToTradedQtPercentage().toString());
            return res;
    }

    public void setDate(String date) {
        if (date.contains(">"))
            date = date.substring(date.lastIndexOf(">") + 1);
        try {
            this.date = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();;
            throw e;
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

    public void setVwap(String vwap) {
        this.vwap = Double.valueOf(vwap.replace(",", ""));
    }

    public void setTotalTradedQuantity(String totalTradedQuantity) {
        this.totalTradedQuantity = Long.valueOf(totalTradedQuantity.replace(",", "").trim());
    }

    public void setTurnOverInRs(String turnOverInRs) {
        this.turnOverInRs = Double.valueOf(turnOverInRs.replace(",", "").trim());
    }

    public void setNumberOfTrades(String numberOfTrades) {
        this.numberOfTrades = Long.valueOf(numberOfTrades.replace(",", ""));
    }

    public void setDeliverableQty(String deliverableQty) {
        try {
            this.deliverableQty = Long.valueOf(deliverableQty.replace(",", ""));
        } catch (NumberFormatException e) {
            this.deliverableQty = null;
        }
    }

    public void setDlyQtToTradedQtPercentage(String dlyQtToTradedQtPercentage) {
        try {
            this.dlyQtToTradedQtPercentage = Double.valueOf(dlyQtToTradedQtPercentage.replace(",", "").trim());
        } catch (NumberFormatException e) {
            this.dlyQtToTradedQtPercentage = null;
        }
    }

    @Override
    public String toString() {
        return symbol + ", "
                + series + ", "
                + date + ", "
                + prevClose + ", "
                + openPrice + ", "
                + highPrice + ", "
                + lowPrice + ", "
                + lastPrice + ", "
                + closePrice + ", "
                + vwap + ", "
                + totalTradedQuantity + ", "
                + turnOverInRs + ", "
                + numberOfTrades + ", "
                + deliverableQty + ", "
                + dlyQtToTradedQtPercentage;
    }

    public int compareTo(@NotNull NSECandleStick NSECandleStick) {
        if (this.getDate().isBefore(NSECandleStick.getDate()))
            return 1;
        else if (this.getDate().isBefore(NSECandleStick.getDate()))
            return -1;
        else
            return 0;
    }
}

