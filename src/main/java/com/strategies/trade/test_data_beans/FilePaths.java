package com.strategies.trade.test_data_beans;

import java.io.File;

/**
 * @author hemasundarpenugonda
 */
public class FilePaths {

    public static final String HOLDINGS_CSV_FILE, INSTRUMENTS_CSV_FILE;
    public static final String PROCESSED_HOLDINGS_CSV_FILE, PROCESSED_SMALLCASE_CSV_FILE, SCREENER_STOCK_DETAILS_CSV_FILE, PROCESSED_SMALLCASE_OBJ_FILE, SCREENER_STOCK_DETAILS_OBJ_FILE, PROCESSED_SMALLCASE_CSV_FILE1;
    public static final String TAGS_DATA_JSON;
    public static final String INDEX_DETAILS_FOLDER_PATH;
    public static final String DOWNLOADED_OUTPUT_FOLDER_PATH, EXTRACTED_OUTPUT_FOLDER_PATH;
    public static final String TRADING_VIEW_LTP_JSON_REQUEST_FILE_PATH;
    public static final String HISTORICAL_DATA_FOLDER_PATH;
    public static final String ALL_INDEX_DETAILS_FOLDER_PATH;
    public static final String ALL_INDICES_FILE_NAME;
    public static final String ALL_INDICES_LIST, ALL_INDICES_LIST_JSON_FILE;
    public static final String PORTFOLIO_STOCKS_LIST;
    static {
        String userDir = System.getProperty("user.dir");

        final String mainResourcesFolderPath = userDir + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator;
        final String testResourcesFolderPath = userDir + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator;

        HOLDINGS_CSV_FILE = mainResourcesFolderPath + File.separator + "holdings.csv";
        INSTRUMENTS_CSV_FILE = mainResourcesFolderPath + File.separator + "instruments.csv";
        PROCESSED_HOLDINGS_CSV_FILE = testResourcesFolderPath + File.separator + "holdings_processed.csv";
        PROCESSED_SMALLCASE_CSV_FILE = testResourcesFolderPath + File.separator + "holdings_smallcase_processed.csv";
        SCREENER_STOCK_DETAILS_CSV_FILE = testResourcesFolderPath + File.separator + "screener_stock_details.csv";
        PROCESSED_SMALLCASE_CSV_FILE1 = testResourcesFolderPath + File.separator + "holdings_smallcase_processed1.csv";
        PROCESSED_SMALLCASE_OBJ_FILE = testResourcesFolderPath + File.separator + "holdings_smallcase_processed.obj";
        SCREENER_STOCK_DETAILS_OBJ_FILE = testResourcesFolderPath + File.separator + "screener_stock_details.obj";
        TAGS_DATA_JSON = mainResourcesFolderPath + File.separator + "tags-data.json";

        TRADING_VIEW_LTP_JSON_REQUEST_FILE_PATH = mainResourcesFolderPath + "api_requests" + File.separator + "request_closePrice.json";
        HISTORICAL_DATA_FOLDER_PATH = userDir + File.separator + "HistoricalData" + File.separator;
        INDEX_DETAILS_FOLDER_PATH = userDir + File.separator + "IndexDetails" + File.separator;
        DOWNLOADED_OUTPUT_FOLDER_PATH = userDir + File.separator + "BseDailyZipFiles" + File.separator;
        EXTRACTED_OUTPUT_FOLDER_PATH = userDir + File.separator + "ExtractedBseDailyFiles" + File.separator;
        ALL_INDEX_DETAILS_FOLDER_PATH = INDEX_DETAILS_FOLDER_PATH + "AllIndeciesDetails";
        ALL_INDICES_FILE_NAME = "AllIndices";
        ALL_INDICES_LIST = INDEX_DETAILS_FOLDER_PATH + ALL_INDICES_FILE_NAME;
        ALL_INDICES_LIST_JSON_FILE = INDEX_DETAILS_FOLDER_PATH + ALL_INDICES_FILE_NAME + ".json";
        PORTFOLIO_STOCKS_LIST = mainResourcesFolderPath + "portfolio_securities.json";

    }
}
