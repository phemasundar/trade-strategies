package com.strategies.trade.test_data_beans;

import java.io.File;

/**
 * @author hemasundarpenugonda
 */
public class FilePaths {

    public static final String HOLDINGS_CSV_FILE, INSTRUMENTS_CSV_FILE;
    public static final String PROCESSED_HOLDINGS_CSV_FILE, PROCESSED_SMALLCASE_CSV_FILE, SCREENER_STOCK_DETAILS_CSV_FILE, PROCESSED_SMALLCASE_OBJ_FILE, SCREENER_STOCK_DETAILS_OBJ_FILE, PROCESSED_SMALLCASE_CSV_FILE1;
    public static final String TAGS_DATA_JSON;
    public static final String TRADING_VIEW_LTP_JSON_REQUEST_FILE_PATH;
    public static final String PORTFOLIO_STOCKS_LIST;

    public static final String BSE_DATA_FOLDER_PATH, NSE_DATA_FOLDER_PATH;
    public static final String HISTORICAL_DATA_FOLDER,
            INDEX_DETAILS_FOLDER,
            DAILY_ZIP_FILES_FOLDER,
            DAILY_EXTRACTED_FILES_FOLDER,
            ALL_INDICES_FILE,
            ALL_INDICES_LIST_JSON_FILE;

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
        PORTFOLIO_STOCKS_LIST = mainResourcesFolderPath + "portfolio_securities.json";

        BSE_DATA_FOLDER_PATH = userDir + File.separator + "Exchanges" + File.separator + "BSE" + File.separator;
        NSE_DATA_FOLDER_PATH = userDir + File.separator + "Exchanges" + File.separator + "NSE" + File.separator;

        HISTORICAL_DATA_FOLDER = "HistoricalData" + File.separator;
        INDEX_DETAILS_FOLDER = "IndexDetails" + File.separator;
        DAILY_ZIP_FILES_FOLDER = "BseDailyZipFiles" + File.separator;
        DAILY_EXTRACTED_FILES_FOLDER = "ExtractedBseDailyFiles" + File.separator;
        ALL_INDICES_FILE = INDEX_DETAILS_FOLDER + "AllIndices";
        ALL_INDICES_LIST_JSON_FILE = ALL_INDICES_FILE + ".json";


    }
}
