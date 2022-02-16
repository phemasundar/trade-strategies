package com.strategies.trade.test_data_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strategies.trade.utilities.JavaUtils;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class PortfolioList {
    private List<StockSymbols> kite;
    private List<StockSymbols> smallcase;
    private List<StockSymbols> exited;
    private List<StockSymbols> free;
    private List<StockSymbols> blueChips;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class StockSymbols {
    private String kite;
    private String nse;
    private String bse;
    }

    /*
     * Utility methods
     */
    public static PortfolioList getPortfolioListObj () throws JsonProcessingException {
        String readFile = JavaUtils.readFile(FilePaths.PORTFOLIO_STOCKS_LIST);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(readFile, PortfolioList.class);
    }

    public static List<String> getBsePortfolioSecurities () throws JsonProcessingException {
        return getPortfolioListObj().getKite()
                .stream()
                .map(PortfolioList.StockSymbols::getBse)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toList());
    }

    public static List<String> getBseExitedSecurities () throws JsonProcessingException {
        return getPortfolioListObj().getExited()
                .stream()
                .map(PortfolioList.StockSymbols::getBse)
                .filter(item -> !item.isBlank())
                .collect(Collectors.toList());
    }
}



