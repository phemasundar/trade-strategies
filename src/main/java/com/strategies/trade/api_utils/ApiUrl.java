package com.strategies.trade.api_utils;

/**
 * @author hemasundarpenugonda
 */
public class ApiUrl {
    public static final String TRADING_VIEW_SCAN_URL = "https://scanner.tradingview.com/india/scan";
    public static final String NSE_HISTORICAL_DATA_URL = "https://www1.nseindia.com/products/dynaContent/common/productsSymbolMapping.jsp?symbol=${stockName}&segmentLink=3&symbolCount=2&series=EQ&dateRange=&fromDate=${fromDate}&toDate=${toDate}&dataType=PRICEVOLUMEDELIVERABLE";
    public static final String NSE_MARKET_DATA_REFERER = "https://www.nseindia.com/market-data/live-equity-market?symbol=${SYMBOL_NAME}";
    public static final String NSE_INDICES_LIST_URL = "https://www.nseindia.com/api/equity-master";
    public static final String NSE_INDEX_DATA_URL = "https://www.nseindia.com/api/equity-stockIndices?index=${INDEX_NAME}";
}
