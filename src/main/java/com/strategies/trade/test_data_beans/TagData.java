package com.strategies.trade.test_data_beans;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
@Data
public class TagData {
    private List<AllTags> allTags;

    @Data
    public static class AllTags {
        private String name;
        private List<String> stocks;
    }

    public List<String> getTagsOfStock(String stockName) {
        List<String> collect = allTags.stream()
                .filter(item -> item.getStocks().contains(stockName))
                .map(AllTags::getName)
                .collect(Collectors.toList());

        return collect;
    }
}
