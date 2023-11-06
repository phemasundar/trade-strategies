package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class IndexResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    private String name;
    private Advance advance;
    private String timestamp;
    private List<IndexData> data;
    private MetaData metadata;
    private MarketStatus marketStatus;
    private String date30dAgo;
    private String date365dAgo;

    //Utility methods
    public List<String> getAllStocksInIndex() {
        return this.getData().stream()
                .filter(item -> item.getPriority() == 0)
                .map(item -> item.getSymbol())
                .collect(Collectors.toList());

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Advance implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer declines;
        private Integer advances;
        private Integer unchanged;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IndexData implements Serializable {
        private static final long serialVersionUID = 1L;

        private Integer priority;
        private String symbol;
        private String identifier;
        private Double open;
        private Double dayHigh;
        private Double dayLow;
        private Double lastPrice;
        private Double previousClose;
        private Double change;
        private Double pChange;
        private Double ffmc;
        private Double yearHigh;
        private Double yearLow;
        private Long totalTradedVolume;
        private Double totalTradedValue;
        private String lastUpdateTime;
        private Double nearWKH;
        private Double nearWKL;
        private Double perChange365d;
        private String date365dAgo;
        private String chart365dPath;
        private String date30dAgo;
        private Double perChange30d;
        private String chart30dPath;
        private String chartTodayPath;

        public void setPerChange365d(String perChange365d) {
            try {
                this.perChange365d = Double.parseDouble(perChange365d);
            } catch (NumberFormatException e) {
                this.perChange365d = null;
            }
        }

        public void setPerChange30d(String perChange30d) {
            try {
                this.perChange30d = Double.parseDouble(perChange30d);
            } catch (NumberFormatException e) {
                this.perChange30d = null;
            }
        }

    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MetaData implements Serializable {
        private static final long serialVersionUID = 1L;

        private String indexName;
        private Double open;
        private Double high;
        private Double low;
        private Double previousClose;
        private Double last;
        private Double percChange;
        private Double change;
        private String timeVal;
        private Double yearHigh;
        private Double yearLow;
        private Long totalTradedVolume;
        private Double totalTradedValue;
        private Double ffmc_sum;
    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MarketStatus implements Serializable {
        private static final long serialVersionUID = 1L;

        private String market;
        private String marketStatus;
        private String tradeDate;
        private String index;
        private Double last;
        private Double variation;
        private Double percentChange;
        private String marketStatusMessage;
    }
}

