package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.NoSuchElementException;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class TradingviewClosePrice /*extends BaseResponse */{

    private List<StockInfo> data;

    public String getClosePrice(String exchange, String stockName) {
        try {
            return this.getData().stream()
                    .filter(item -> item.getS().equalsIgnoreCase(exchange + ":" + stockName))
                    .findAny()
                    .get()
                    .getD()
                    .get(0);
        } catch (NoSuchElementException e) {
            return "";
        }

    }

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class StockInfo {
        private String s;
        private List<String> d;
    }
}
