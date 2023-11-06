package com.strategies.trade.strategies;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import org.ta4j.core.*;
import org.ta4j.core.analysis.criteria.TotalProfitCriterion;
import org.ta4j.core.indicators.EMAIndicator;
import org.ta4j.core.indicators.MACDIndicator;
import org.ta4j.core.indicators.StochasticOscillatorKIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.trading.rules.CrossedDownIndicatorRule;
import org.ta4j.core.trading.rules.CrossedUpIndicatorRule;
import org.ta4j.core.trading.rules.OverIndicatorRule;
import org.ta4j.core.trading.rules.UnderIndicatorRule;

import java.io.IOException;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class MovingMomentumStrategy {

    /**
     * @param series the bar series
     * @return the moving momentum strategy
     */
    public static Strategy buildStrategy(BarSeries series) {
        if (series == null) {
            throw new IllegalArgumentException("Series cannot be null");
        }

        ClosePriceIndicator closePrice = new ClosePriceIndicator(series);

        // The bias is bullish when the shorter-moving average moves above the longer
        // moving average.
        // The bias is bearish when the shorter-moving average moves below the longer
        // moving average.
        EMAIndicator shortEma = new EMAIndicator(closePrice, 9);
        EMAIndicator longEma = new EMAIndicator(closePrice, 26);

        StochasticOscillatorKIndicator stochasticOscillK = new StochasticOscillatorKIndicator(series, 14);

        MACDIndicator macd = new MACDIndicator(closePrice, 9, 26);
        EMAIndicator emaMacd = new EMAIndicator(macd, 18);

        // Entry rule
        Rule entryRule = new OverIndicatorRule(shortEma, longEma) // Trend
                .and(new CrossedDownIndicatorRule(stochasticOscillK, 20)) // Signal 1
                .and(new OverIndicatorRule(macd, emaMacd)); // Signal 2

        // Exit rule
        Rule exitRule = new UnderIndicatorRule(shortEma, longEma) // Trend
                .and(new CrossedUpIndicatorRule(stochasticOscillK, 20)) // Signal 1
                .and(new UnderIndicatorRule(macd, emaMacd)); // Signal 2

        return new BaseStrategy(entryRule, exitRule);
    }

    public static void main(String[] args) throws KiteException, IOException {

        // Getting the bar series
//        BarSeries series = CsvTradesLoader.loadBitstampSeries();
        KiteConnect kiteConnect = null;
        Date from = null;
        Date to = null;
        HistoricalData historicalData = new InstrumentExamples().getHistoricalData(kiteConnect, from, to, "54872327", "15minute");

        List<Bar> collect = historicalData.dataArrayList.stream()
                .map(item -> new BaseBar(Duration.ofMinutes(15), ZonedDateTime.parse(item.timeStamp), item.open, item.high, item.low, item.close, item.volume))
                .collect(Collectors.toList());

//        List<Bar> collect1 = ApiUtil.stream()
//                .map(item -> new BaseBar(Duration.ofMinutes(15), ZonedDateTime.parse(item.timeStamp), item.open, item.high, item.low, item.close, item.volume))
//                .collect(Collectors.toList());

        BarSeries series = new BaseBarSeries(collect);

        // Building the trading strategy
        Strategy strategy = buildStrategy(series);

        // Running the strategy
        BarSeriesManager seriesManager = new BarSeriesManager(series);
        TradingRecord tradingRecord = seriesManager.run(strategy);
        System.out.println("Number of trades for the strategy: " + tradingRecord.getTradeCount());

        // Analysis
        System.out.println(
                "Total profit for the strategy: " + new TotalProfitCriterion().calculate(series, tradingRecord));
    }
}
