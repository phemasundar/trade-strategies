package com.strategies.trade.test_data_beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */

@Data
@AllArgsConstructor
public class SmallcaseAdvInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    private String tradingSymbol;
    private String stockName;
    private String smallcaseName;
    private String smallcaseCAGR;
    private String smallcaseWeight;
//    private String json;
    private String rebalanceSchedule;
    private String isPremium;
    private String lastRebalanced;

    public SmallcaseAdvInfo(String tradingSymbol, String stockName, List<String> smallcaseDetails, String json) {
        this.tradingSymbol = tradingSymbol;
        this.stockName = stockName;
        try {
            this.smallcaseName = smallcaseDetails.get(0);
        } catch (IndexOutOfBoundsException ignored) {
            this.smallcaseName = "";
        }
        try {
            this.smallcaseCAGR = smallcaseDetails.get(1);
        } catch (IndexOutOfBoundsException ignored) {
            this.smallcaseCAGR = "";
        }
        try {
            this.smallcaseWeight = smallcaseDetails.get(2).trim();
        } catch (IndexOutOfBoundsException ignored) {
            this.smallcaseWeight = "";
        }
//        this.json = json;
        String smallcaseName = "", rebalanceSchedule = "", isPremium = "", lastRebalanced = "";

        try {
            SmallcaseAdvInfo.SmallcaseFullDetails smallcaseFullDetails = new ObjectMapper(new YAMLFactory())
                    .readValue(json, SmallcaseAdvInfo.SmallcaseFullDetails.class);
            smallcaseName = smallcaseFullDetails.getProps().getPageProps().getOverview().getData().getFeatured().getInfo().getName();
            rebalanceSchedule = smallcaseFullDetails.getProps().getPageProps().getOverview().getData().getFeatured().getInfo().getRebalanceSchedule();
            isPremium = smallcaseFullDetails.getProps().getPageProps().getOverview().getData().getFeatured().getInfo().getPricing();
            lastRebalanced = smallcaseFullDetails.getProps().getPageProps().getOverview().getData().getFeatured().getInfo().getLastRebalanced();
        }catch (JsonProcessingException ignored) {
        }

        this.rebalanceSchedule = rebalanceSchedule;
        this.isPremium = isPremium;
        this.lastRebalanced = lastRebalanced;
    }

    @Override
    public String toString() {
        return this.getTradingSymbol() + ", " + this.getStockName() + ", " + this.getSmallcaseName() + ", " + this.getSmallcaseCAGR() + ", " + this.getSmallcaseWeight() + ", " + this.getRebalanceSchedule() + ", " + this.getIsPremium() + ", " + this.getLastRebalanced();
    }

    public static SmallcaseAdvInfo getCsvHeader() {
        return new SmallcaseAdvInfo("Trading Symbol", "Stock name", Arrays.asList("Smallcase Name", "Smallcase CAGR", "Smallcase Weight"), "json columns");
    }

    @Data
    public static class SmallcaseFullDetails {
        private Props props;

        @Data
        public static class Props {
            private pageProps pageProps;

            @Data
            public static class pageProps {
                private Overview overview;

                @Data
                public static class Overview {
                    private Data data;

                    @lombok.Data
                    public static class Data {
                        private Featured featured;

                        @lombok.Data
                        public static class Featured {
                            private Info info;

                            @lombok.Data
                            public static class Info {
                                private String name;
                                private String rebalanceSchedule;
                                private String pricing;
                                private String lastRebalanced;
                            }
                        }
                    }
                }
            }
        }
    }
}
