package com.strategies.trade.test_data_beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strategies.trade.api_test_beans.CandleStick;
import com.strategies.trade.api_test_beans.IndexResponse;
import com.strategies.trade.utilities.HistoricalDataUtils;
import com.strategies.trade.utilities.JavaUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
public enum Exchange implements ExchangeFunctions {
    NSE {
        @Override
        public List<List<CandleStick>> getHistoricalData(SecuritiesList securitiesList) throws IOException {
            return HistoricalDataUtils.readNSEHistoricalData(securitiesList);
        }

        @Override
        public String getSymbol() {
            return "NSE";
        }
    }, BSE {
        @Override
        public List<List<CandleStick>> getHistoricalData(SecuritiesList securitiesList) throws IOException {
            return HistoricalDataUtils.getBseDataInJavaObjects(securitiesList);
        }

        @Override
        public String getSymbol() {
            return "BOM";
        }
    };


}

interface ExchangeFunctions {
    List<List<CandleStick>> getHistoricalData(SecuritiesList securitiesList) throws IOException;
    String getSymbol();
}