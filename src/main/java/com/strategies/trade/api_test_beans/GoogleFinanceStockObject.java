package com.strategies.trade.api_test_beans;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author hemasundarpenugonda
 */
@Data
public class GoogleFinanceStockObject {
    private final String ltp;
    private final String peRatio;

    public String getPeRatio() {
        if (this.peRatio.equalsIgnoreCase("-"))
            return "0";
        return peRatio;
    }
}
