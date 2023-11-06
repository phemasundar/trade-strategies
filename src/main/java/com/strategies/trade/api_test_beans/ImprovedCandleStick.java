package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImprovedCandleStick extends CandleStick implements Serializable {

    private static final long serialVersionUID = 1L;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    //    private CandleStick basicCandle;
    private Double ema21;
    private Double ema26;
    private Double ema12;
    private Double macd;
    private Double signal;
    private Double rsi;

    public ImprovedCandleStick(CandleStick NSECandleStick) {
        super(NSECandleStick);
        ema21 = null;
        ema26 = null;
        ema12 = null;
        macd = null;
        signal = null;
        rsi = null;
    }

    @Override
    public String toString() {
        return super.toString() + ", " + getEma21();
    }

    /**
     * Utility methods
     */
//    public static ImprovedCandleStick of(CandleStick candleStick) {
//
//    }
    public Double getEma(int interval) {
        switch (interval) {
            case 26:
                return getEma26();
            case 21:
                return getEma21();
            case 12:
                return getEma12();
            default:
                throw new RuntimeException("Not defined" + interval);
        }
    }

    public void setEma(Double emaValue, int interval) {
        switch (interval) {
            case 26:
                setEma26(emaValue);
                break;
            case 21:
                setEma21(emaValue);
                break;
            case 12:
                setEma12(emaValue);
                break;
            default:
                throw new RuntimeException("Not defined" + interval);
        }
    }

    public static Double getPreviousEma(List<ImprovedCandleStick> improvedCandleSticks, int interval, int index) {
        Double previousEma = improvedCandleSticks.get(index - 1).getEma(interval);
        if (previousEma == null) {
            int previousIndex = index - 1;
            double sum = improvedCandleSticks.subList(0, previousIndex)
                    .stream()
                    .map(item -> item.getClosePrice())
                    .mapToDouble(item -> item)
                    .sum();
            previousEma = sum / previousIndex;
        }

        return previousEma;
    }

    public static Double getPreviousSignal(List<ImprovedCandleStick> improvedCandleSticks, int interval, int index) {
        Double previousSignal = improvedCandleSticks.get(index - 1).getSignal();
        //TODO: Yet to verify the logic
        if (previousSignal == null) {
            int previousIndex = index - 1;
            List<ImprovedCandleStick> collect = improvedCandleSticks
                    .subList(0, previousIndex)
                    .stream()
                    .filter(item -> item.getMacd() != null)
                    .collect(Collectors.toList());
            double sum = collect.stream()
                    .map(item -> item.getMacd())
                    .mapToDouble(item -> item)
                    .sum();

//            double sum = improvedCandleSticks.subList(0, previousIndex)
//                    .stream()
//                    .map(item -> item.getMacd())
//                    .mapToDouble(item -> item)
//                    .sum();
            previousSignal = sum / collect.size();
        }

        return previousSignal;
    }
}
