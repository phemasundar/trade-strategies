package com.strategies.trade.test_data_beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strategies.trade.api_test_beans.IndexResponse;
import com.strategies.trade.utilities.CsvUtils;
import com.strategies.trade.utilities.JavaUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hemasundarpenugonda
 */
public enum SecuritiesList implements SecuritiesListFunctions {
    PORTFOLIO_SECURITIES_NSE {
        @Override
        public List<String> getSecuritiesList() throws JsonProcessingException {
            PortfolioList portfolioList = PortfolioList.getPortfolioListObj();
            return portfolioList.getKite()
                    .stream()
                    .map(PortfolioList.StockSymbols::getNse)
                    .collect(Collectors.toList());
        }
    },PORTFOLIO_SECURITIES_BSE {
        @Override
        public List<String> getSecuritiesList() throws JsonProcessingException {
            PortfolioList portfolioList = PortfolioList.getPortfolioListObj();

            return PortfolioList.getBsePortfolioSecurities();
        }
    },PORTFOLIO_EXITED_SECURITIES_BSE {
        @Override
        public List<String> getSecuritiesList() throws JsonProcessingException {

            return Stream.of(PortfolioList.getBsePortfolioSecurities(), PortfolioList.getBseExitedSecurities(), PortfolioList.getBseBlueChipSecurities())
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
        }
    }, ALL_INDEX_SECURITIES_NSE {
        @Override
        public List<String> getSecuritiesList() throws IOException {
            List<String> allSecurities = Files.list(Paths.get(FilePaths.INDEX_DETAILS_FOLDER_PATH))
                    .filter(Files::isRegularFile)
                    .filter(item -> item.getFileName().toString().startsWith("NIFTY"))
                    .filter(item -> !item.getFileName().toString().endsWith(".json"))
                    .map(item -> JavaUtils.deSerialize(item.toString(), IndexResponse.class).get(0))
                    .map(item -> item.getAllStocksInIndex())
                    .flatMap(item -> item.stream())
                    .distinct()
                    .filter(item -> !Arrays.asList(new String[]{"MAZDOCK", "BARBEQUE", "BURGERKING", "CHEMCON", "EASEMYTRIP","EQUITASBNK", "GLAND", "HOMEFIRST", "KALYANKJIL", "PCBL", "STOVEKRAFT"}).contains(item))
                    .sorted()
                    .collect(Collectors.toList());

            return allSecurities;
        }
    }, ALL_BSE {
        @Override
        public List<String> getSecuritiesList() throws IOException {
            Path path = Files.list(Paths.get(FilePaths.EXTRACTED_OUTPUT_FOLDER_PATH))
                    .filter(Files::isRegularFile)
                    .sorted((p1, p2) -> Long.compare(p2.toFile().lastModified(), p1.toFile().lastModified()))
                    .findFirst()
                    .get();
            List<String> collect = CsvUtils.getSheetData(path.toAbsolutePath().toString())
                    .stream().skip(1)
                    .map(item -> item.get(0))
                    .collect(Collectors.toList());
            return collect;
        }
    };


}

interface SecuritiesListFunctions {
    List<String> getSecuritiesList() throws IOException;
}