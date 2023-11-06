package com.strategies.trade.test_data_beans;

import com.strategies.trade.api_test_beans.*;
import com.strategies.trade.api_utils.ApiRequests;
import com.strategies.trade.utilities.CsvUtils;
import com.strategies.trade.utilities.HistoricalDataUtils;
import com.strategies.trade.utilities.JavaUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author hemasundarpenugonda
 */
public enum Exchange implements ExchangeFunctions {
    NSE {
        @Override
        public String getDataFolderPath() {
            return FilePaths.NSE_DATA_FOLDER_PATH;
        }

        @Override
        public List<String> getSecuritiesList(Securities securityType, String indexName) throws IOException {

            if (securityType == Securities.ALL) {

                return Files.list(Paths.get(this.getDataFolderPath() + FilePaths.INDEX_DETAILS_FOLDER))
                        .filter(Files::isRegularFile)
                        .filter(item -> item.getFileName().toString().startsWith("NIFTY"))
                        .filter(item -> !item.getFileName().toString().endsWith(".json"))
                        .map(item -> JavaUtils.deSerialize(item.toString(), IndexResponse.class).get(0))
                        .map(item -> item.getAllStocksInIndex())
                        .flatMap(Collection::stream)
                        .distinct()
                        .filter(item -> !Arrays.asList(new String[]{"MAZDOCK", "BARBEQUE", "BURGERKING", "CHEMCON", "EASEMYTRIP", "EQUITASBNK", "GLAND", "HOMEFIRST", "KALYANKJIL", "PCBL", "STOVEKRAFT"}).contains(item))
                        .sorted()
                        .collect(Collectors.toList());
            } else if (securityType == Securities.PORTFOLIO) {
                PortfolioList portfolioList = PortfolioList.getPortfolioListObj();
                return portfolioList.getKite()
                        .stream()
                        .map(PortfolioList.StockSymbols::getNse)
                        .collect(Collectors.toList());
            } else {
                //ToDo: Not yet implemented
                return null;
            }
        }

        @Override
        public List<List<CandleStick>> getHistoricalData(Securities securityType, String indexName) throws IOException {
            List<String> allSecurities = Exchange.NSE.getSecuritiesList(securityType, indexName);
            return HistoricalDataUtils.readNSEHistoricalData(allSecurities);
        }

        @Override
        public String getSymbol() {
            return "NSE";
        }

        @Override
        public void writeIndicesList() throws IOException {
            CustomResponse<IndicesList> indicesList = ApiRequests.getIndicesList();
            IndicesList responseObj = indicesList.getResponseObj();
            List<IndicesList> indicesLists = Collections.singletonList(responseObj);
            JavaUtils.serialize(this.getDataFolderPath() + FilePaths.ALL_INDICES_FILE, indicesLists);
            JavaUtils.writeToFile(this.getDataFolderPath() + FilePaths.ALL_INDICES_LIST_JSON_FILE, new JSONObject(indicesList.getResponse()).toString(4).getBytes());
        }

        @Override
        public void writeAllIndexDetails() throws IOException {
            this.writeIndicesList();

            JavaUtils.deSerialize(this.getDataFolderPath() + FilePaths.ALL_INDICES_FILE, IndicesList.class)
                    .get(0)
                    .getAllIndices()
                    .stream()
                    .map(String::trim)
                    .filter(item -> !item.isEmpty())
                    .filter(item -> Files.notExists(Paths.get(this.getDataFolderPath() + FilePaths.INDEX_DETAILS_FOLDER + JavaUtils.getValidFileName(item))))
                    .map(item -> {
                        try {
                            CustomResponse<IndexResponse> indexDetails = ApiRequests.getIndexDetails(item);
                            return indexDetails;
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    })
                    .forEach(item -> {
                        IndexResponse responseObj = item.getResponseObj();
                        String filePath = this.getDataFolderPath() + FilePaths.INDEX_DETAILS_FOLDER + JavaUtils.getValidFileName(responseObj.getName());
                        JavaUtils.serialize(filePath, Stream.of(responseObj).collect(Collectors.toList()));
                        JavaUtils.writeToFile(filePath + ".json", new JSONObject(item.getResponse()).toString(4).getBytes());
                    });
        }
    },
    BSE {
        @Override
        public String getDataFolderPath() {
            return FilePaths.BSE_DATA_FOLDER_PATH;
        }

        @Override
        public List<String> getSecuritiesList(Securities securityType, String indexName) throws IOException {
            if (securityType == Securities.ALL) {
                Path latestDailyReportFilePath = JavaUtils.getLatestModifiedFileFromFolder(this.getDataFolderPath() + FilePaths.DAILY_EXTRACTED_FILES_FOLDER);
                return CsvUtils.getFirstColumnInSheet(latestDailyReportFilePath.toAbsolutePath().toString(), 1);

            } else if (securityType == Securities.PORTFOLIO) {
                return Stream.of(PortfolioList.getBsePortfolioSecurities(), PortfolioList.getBseExitedSecurities(), PortfolioList.getBseBlueChipSecurities())
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());

            } else {
                String filePath = this.getDataFolderPath() + FilePaths.INDEX_DETAILS_FOLDER + JavaUtils.getValidFileName(indexName);

                return JavaUtils.deSerialize(filePath, BseIndexResponse.class)
                        .get(0).getALlScripNames();
            }
        }

        @Override
        public List<List<CandleStick>> getHistoricalData(Securities securityType, String indexName) throws IOException {
            List<String> securitiesList = this.getSecuritiesList(securityType, indexName);
            return HistoricalDataUtils.getBseDataInJavaObjects(securitiesList);
        }

        @Override
        public String getSymbol() {
            return "BOM";
        }

        @Override
        public void writeIndicesList() throws IOException {
            CustomResponse<BseIndicesList> bseIndicesList = ApiRequests.getBseIndicesList();
            JavaUtils.serialize(this.getDataFolderPath() + FilePaths.ALL_INDICES_FILE, bseIndicesList.getResponseObjList());
            JavaUtils.writeToFile(this.getDataFolderPath() + FilePaths.ALL_INDICES_LIST_JSON_FILE, new JSONArray(bseIndicesList.getResponse()).toString(4).getBytes());
        }

        @Override
        public void writeAllIndexDetails() throws IOException {
            this.writeIndicesList();

            JavaUtils.deSerialize(this.getDataFolderPath() + FilePaths.ALL_INDICES_FILE, BseIndicesList.class)
                    .stream()
                    .map(item -> item.getIndexName().trim())
                    .filter(item -> !item.isEmpty())
                    .filter(item -> Files.notExists(Paths.get(this.getDataFolderPath() + FilePaths.INDEX_DETAILS_FOLDER + JavaUtils.getValidFileName(item))))
                    .forEach(item -> {
                        try {
                            CustomResponse<BseIndexResponse> indexDetails = ApiRequests.getBseIndexDetails(item);
                            BseIndexResponse responseObj = indexDetails.getResponseObj();


                            String filePath = this.getDataFolderPath() + FilePaths.INDEX_DETAILS_FOLDER + JavaUtils.getValidFileName(item);
                            JavaUtils.serialize(filePath, Stream.of(responseObj).collect(Collectors.toList()));
                            JavaUtils.writeToFile(filePath + ".json", indexDetails.getResponse().getBytes());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

        }
    };


}

interface ExchangeFunctions {
    List<List<CandleStick>> getHistoricalData(Securities securityType, String indexName) throws IOException;

    String getSymbol();

    void writeIndicesList() throws IOException;

    void writeAllIndexDetails() throws IOException;

    String getDataFolderPath();

    List<String> getSecuritiesList(Securities securityType, String indexName) throws IOException;
}