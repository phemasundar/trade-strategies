package com.strategies.trade.test_data_beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */

@Data
@AllArgsConstructor
public class DefaultKiteHoldings {
    private List<DefaultKiteHolding> defaultKiteHoldings;

    public static DefaultKiteHoldings getDefaultKiteHoldingsObject(List<List<String>> defaultKiteHoldingAsStrings) {

        List<DefaultKiteHolding> collect = defaultKiteHoldingAsStrings.stream()
                .map(DefaultKiteHolding::new)
                .collect(Collectors.toList());

        return new DefaultKiteHoldings(collect);
    }

    @Data
    @NoArgsConstructor
    public static class DefaultKiteHolding {
        private String instrument;
        private Integer quantity;
        private Float avgCost;
        private Float ltp;
        private Float currentValue;
        private Float profitNloss;
        private Float netChange;
        private Float dayChange;

        protected DefaultKiteHolding(List<String> str) {
            this.instrument = str.get(0).split("-")[0];
            this.quantity = Integer.parseInt(str.get(1));
            this.avgCost = Float.parseFloat(str.get(2));
            this.ltp = Float.parseFloat(str.get(3));
            this.currentValue = Float.parseFloat(str.get(4));
            this.profitNloss = Float.parseFloat(str.get(5));
            this.netChange = Float.parseFloat(str.get(6));
            this.dayChange = Float.parseFloat(str.get(7));
        }
    }

}
