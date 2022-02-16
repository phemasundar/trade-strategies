package com.strategies.trade.test_data_beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
@Data
@AllArgsConstructor
public class Smallcase {
    private String name;
    private List<IndStock> stocks;

    @Data
    @AllArgsConstructor
    public static class IndStock {
        private String tradingSymbol;
        private String stockName;
        private String stockWeight;
    }

    public double getTotalWeight () {
        try {
            double sum = getStocks().stream()
                    .map(item -> item.getStockWeight().replaceAll("%", ""))
                    .filter(item -> !item.isEmpty())
                    .mapToDouble(Float::parseFloat)
                    .sum();
            return sum;
        } catch (NumberFormatException e) {
            System.out.println();
        }
        return 0;
    }

    @Override
    public String toString() {
        return getStocks().stream()
                .map(item -> getName() + ", " + item.getTradingSymbol() + ", "+ item.getStockName() + ", "+item.getStockWeight())
                .collect(Collectors.joining("\n"));
    }

    public static String getCsvHeader() {
        return "smallcase name, trading symbol, stock name, stock weight";
    }
}
