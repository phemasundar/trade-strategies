package com.strategies.trade.test_data_beans;

import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */

@Data
public class ProcessedKiteHoldings {
    private List<IndProcessedKiteHolding> indProcessedKiteHoldings;

    private Float currentPortfolioValue;

    private ProcessedKiteHoldings(List<IndProcessedKiteHolding> indProcessedKiteHoldings) {
        this.indProcessedKiteHoldings = indProcessedKiteHoldings;
        this.currentPortfolioValue = indProcessedKiteHoldings.stream()
                .map(IndProcessedKiteHolding::getCurrentValue)
                .reduce(0.00f, Float::sum);
    }

    public static ProcessedKiteHoldings getProcessedKiteHoldingsObject(DefaultKiteHoldings defaultKiteHoldings) {
        List<IndProcessedKiteHolding> collect = defaultKiteHoldings.getDefaultKiteHoldings()
                .stream()
                .map(IndProcessedKiteHolding::new)
                .collect(Collectors.toList());

        return new ProcessedKiteHoldings(collect);
    }

    public ProcessedKiteHoldings calculatePercentageInPortfolio() {
        getIndProcessedKiteHoldings()
                .forEach(item -> item.setPercentageInPortfolio(item.getCurrentValue() / getCurrentPortfolioValue() * 100));
        return this;
    }

    public ProcessedKiteHoldings assignTags() {
        TagData allTagsData = TestData.getAllTagsData();

        getIndProcessedKiteHoldings()
                .forEach(item -> item.setTags(allTagsData.getTagsOfStock(item.getInstrument())));
        return this;
    }

    public Float getCurrentValueOfTag(String tagName) {
        Float reduce = getIndProcessedKiteHoldings().stream()
                .filter(item -> item.getTags().contains(tagName))
                .map(IndProcessedKiteHolding::getCurrentValue)
                .reduce(0.00f, Float::sum);
        return reduce;
    }

    public ProcessedKiteHoldings calculatePercentageOfTagsInPortfolio() {
        getIndProcessedKiteHoldings()
                .forEach(item -> {
                    List<Float> collect = item.getTags()
                            .stream()
                            .map(item1 -> getCurrentValueOfTag(item1) / getCurrentPortfolioValue() * 100)
                            .collect(Collectors.toList());

                    item.setTagPercentageInPortfolio(collect);
                });
        return this;
    }

    public String convertToCSVFormat() {

        String header = "Instrument, Qty., Avg. cost, LTP, Cur. val, P&L, Net chg., Day chg., % in Portfolio, tags, tag % in portfolio";
        String collect = getIndProcessedKiteHoldings().stream()
                .map(IndProcessedKiteHolding::convertToCSVFormat)
                .collect(Collectors.joining("\n"));

        return header + "\n" + collect;
    }

    @Data
    public static class IndProcessedKiteHolding {
        private String instrument;
        private Integer quantity;
        private Float avgCost;
        private Float ltp;
        private Float currentValue;
        private Float profitNloss;
        private Float netChange;
        private Float dayChange;

        private Float percentageInPortfolio;
        private List<String> tags;
        private List<Float> tagPercentageInPortfolio;

        private IndProcessedKiteHolding(DefaultKiteHoldings.DefaultKiteHolding defaultKiteHolding) {
            this.instrument = defaultKiteHolding.getInstrument();
            this.quantity = defaultKiteHolding.getQuantity();
            this.avgCost = defaultKiteHolding.getAvgCost();
            this.ltp = defaultKiteHolding.getLtp();
            this.currentValue = defaultKiteHolding.getCurrentValue();
            this.profitNloss = defaultKiteHolding.getProfitNloss();
            this.netChange = defaultKiteHolding.getNetChange();
            this.dayChange = defaultKiteHolding.getDayChange();

            percentageInPortfolio = 0.00f;
            tags = new ArrayList<>();
            tagPercentageInPortfolio = new ArrayList<>();
        }

        public String convertToCSVFormat() {
            return getInstrument() + ", "
                    + getQuantity() + ", "
                    + getAvgCost() + ", "
                    + getLtp() + ", "
                    + getCurrentValue() + ", "
                    + getProfitNloss() + ", "
                    + getNetChange() + ", "
                    + getDayChange() + ", "
                    + getPercentageInPortfolio() + ", "
                    + getTags() + ", "
                    + getTagPercentageInPortfolio();
        }

        public Float getPercentageInPortfolio() {
            return new BigDecimal(percentageInPortfolio).setScale(2, RoundingMode.HALF_UP).floatValue();
        }

        public List<Float> getTagPercentageInPortfolio() {
            return tagPercentageInPortfolio.stream()
                    .map(item -> new BigDecimal(item).setScale(2, RoundingMode.HALF_UP).floatValue())
                    .collect(Collectors.toList());
        }
    }
}
