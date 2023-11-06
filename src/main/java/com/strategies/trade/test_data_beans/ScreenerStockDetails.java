package com.strategies.trade.test_data_beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */

@Data
@AllArgsConstructor
public class ScreenerStockDetails implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tradingSymbol;
    private String stockName;
    private List<String> pros;
    private List<String> cons;

    public ScreenerStockDetails(String tradingSymbol, String stockName) {
        this.tradingSymbol = tradingSymbol;
        this.stockName = stockName;
    }

    public static ScreenerStockDetails getCsvHeader() {
        return new ScreenerStockDetails("Trading Symbol", "Stock Name", List.of("Pros"), List.of("Cons"));
    }

    @Override
    public String toString() {
        return this.getTradingSymbol() + ", " + this.getStockName() + ", " + String.join("\n", this.getPros() )+ ", " + String.join("\n", this.getCons());
    }

}
