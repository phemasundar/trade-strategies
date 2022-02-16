package com.strategies.trade.copied;

import com.strategies.trade.api_test_beans.CustomResponse;
import com.strategies.trade.api_test_beans.IndexResponse;
import com.strategies.trade.api_test_beans.IndicesList;
import com.strategies.trade.api_utils.ApiRequests;
import com.strategies.trade.test_data_beans.FilePaths;
import com.strategies.trade.test_data_beans.SecuritiesList;
import com.strategies.trade.utilities.TechIndicatorUtils;
import com.strategies.trade.utilities.Tested;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.strategies.trade.utilities.JavaUtils;
/**
 * @author hemasundarpenugonda
 */
public class HistoricalDataTest {

    @Test @Tested
    public void writeBSEHistoricalDataForAllStocks() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");

        for (LocalDate startDateObj = LocalDate.of(2019, 1, 1);
             startDateObj.isBefore(LocalDate.now());
             startDateObj = startDateObj.plusDays(1)) {

            if (startDateObj.getDayOfWeek() == DayOfWeek.SATURDAY || startDateObj.getDayOfWeek() == DayOfWeek.SUNDAY)
                continue;

            String zipFileName = "EQ_ISINCODE_" + formatter.format(startDateObj) + ".zip";
            String zipFilePath = FilePaths.DOWNLOADED_OUTPUT_FOLDER_PATH + zipFileName;

            if (!new File(zipFilePath).exists()) {

                JavaUtils.executeCommand(("curl https://www.bseindia.com/download/BhavCopy/Equity/" + zipFileName + " --output " + zipFilePath).split(" "));
                try {
                    new ZipFile(zipFilePath).extractAll(FilePaths.EXTRACTED_OUTPUT_FOLDER_PATH);
                } catch (ZipException e) {
                    // Ignoring the exception. As this means the zip file is of on Holiday.
                    e.printStackTrace();
                }
            }
        }

    }

    @Test
    @Tested
    public void writeNSEHistoricalData4ListOfSecurities() throws IOException {

//        List<String> allSecurities = SecuritiesList.PORTFOLIO_SECURITIES.getSecuritiesList();
        List<String> allSecurities = SecuritiesList.ALL_INDEX_SECURITIES_NSE.getSecuritiesList();

        TechIndicatorUtils.writeOrUpdateHistoricalData(allSecurities);
    }

    @Test
    public void writeListOfAllIndices() throws IOException {
        CustomResponse<IndicesList> indicesList = ApiRequests.getIndicesList();
        IndicesList responseObj = indicesList.getResponseObj();
        List<IndicesList> indicesLists = Collections.singletonList(responseObj);
        JavaUtils.serialize(FilePaths.ALL_INDICES_LIST, indicesLists);
        JavaUtils.writeToFile(FilePaths.ALL_INDICES_LIST_JSON_FILE, new JSONObject(indicesList.getResponse()).toString(4).getBytes());
    }

    @Test
    @Tested
    public void writeAllIndexDetails() throws IOException {

        JavaUtils.deSerialize(FilePaths.ALL_INDICES_LIST, IndicesList.class)
                .get(0)
                .getAllIndices()
                .stream()
                .map(String::trim)
                .filter(item -> !item.isEmpty())
                .filter(item -> Files.notExists(Paths.get(FilePaths.INDEX_DETAILS_FOLDER_PATH + JavaUtils.getValidFileName(item))))
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
                    String filePath = FilePaths.INDEX_DETAILS_FOLDER_PATH + JavaUtils.getValidFileName(responseObj.getName());
                    JavaUtils.serialize(filePath, Stream.of(responseObj).collect(Collectors.toList()));
                    JavaUtils.writeToFile(filePath + ".json", new JSONObject(item.getResponse()).toString(4).getBytes());
                });
    }

}
