package com.strategies.trade.ta4j_runners;

import com.strategies.trade.api_test_beans.CandleStick;
import com.strategies.trade.test_data_beans.Exchange;
import com.strategies.trade.test_data_beans.Securities;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.BaseBarSeriesBuilder;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.num.Num;
import org.testng.annotations.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

/**
 * @author hemasundarpenugonda
 */
public class SMA {

    @Test
    public void calculateSMA200() throws IOException {
        List<List<CandleStick>> historicalCandleSticks = Exchange.BSE.getHistoricalData(Securities.ALL, null);

        BarSeries baseBarSeries = historicalCandleSticks.stream()
                .filter(item -> item.get(item.size() - 1).getSymbol().equalsIgnoreCase("542752"))
                .map(item -> {
                    BaseBarSeries build = new BaseBarSeriesBuilder().build();
                    item.stream()
                            .sorted((candle1, candle2) -> ((candle1.getDate().isAfter(candle2.getDate())) ? 1 : -1))
                            .forEach(currentCandleSticks -> build.addBar(ZonedDateTime.of(currentCandleSticks.getDate(), LocalTime.of(15, 30), ZoneId.of("Asia/Kolkata")), currentCandleSticks.getOpenPrice(), currentCandleSticks.getHighPrice(), currentCandleSticks.getLowPrice(), currentCandleSticks.getClosePrice(), currentCandleSticks.getTurnOverInRs()));
                    return build;
                })
                .findAny()

                .orElseThrow(() -> new RuntimeException("did not find the script we are looking for."));

        // Getting the close price of the ticks
        Num firstClosePrice = baseBarSeries.getBar(0).getClosePrice();
        System.out.println("First close price: " + firstClosePrice.doubleValue());

        // Or within an indicator:
        ClosePriceIndicator closePrice = new ClosePriceIndicator(baseBarSeries);
        // Here is the same close price:
        System.out.println(firstClosePrice.isEqual(closePrice.getValue(0))); // equal to firstClosePrice

        // Getting the simple moving average (SMA) of the close price over the last 5 ticks
        SMAIndicator shortSma = new SMAIndicator(closePrice, 5);


        // Here is the 5-ticks-SMA value at the 42nd index
        System.out.println("5-ticks-SMA value at the 42nd index: " + shortSma.getValue(42).doubleValue());

        // Getting a longer SMA (e.g. over the 30 last ticks)
        SMAIndicator longSma = new SMAIndicator(closePrice, 30);
        System.out.println("5-ticks-SMA value at the 42nd index: " + longSma.getValue(42).doubleValue());

        SMAIndicator sma200 = new SMAIndicator(closePrice, 50);
        List<Double> objects = new ArrayList<>();
        try {
            for (int i = 0; true; i++) {
                objects.add(sma200.getValue(i).doubleValue());
            }
        }catch (Exception e) {
            System.out.println(objects);
        }
        System.out.println(objects);
        double v = sma200.getValue(baseBarSeries.getEndIndex()).doubleValue();
        System.out.println(v);
    }
}
