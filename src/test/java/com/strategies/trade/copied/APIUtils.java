package com.strategies.trade.copied;

import com.strategies.trade.api_test_beans.*;
import com.strategies.trade.api_utils.ApiRequests;
import com.strategies.trade.test_data_beans.Exchange;
import com.strategies.trade.test_data_beans.FilePaths;
import com.strategies.trade.test_data_beans.SecuritiesList;
import com.strategies.trade.utilities.HistoricalDataUtils;
import com.strategies.trade.utilities.JavaUtils;
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

/**
 * @author hemasundarpenugonda
 */
public class APIUtils {

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

    @Test
    @Tested
    public void percentageChangesFromMonths() throws IOException, ClassNotFoundException {
        HistoricalDataUtils.percentageChangeFromNumberOfMonths(Exchange.BSE, SecuritiesList.PORTFOLIO_EXITED_SECURITIES_BSE, 2);
    }

    @Test
    @Tested
    public void percentageChangesFromWeeks() throws IOException, ClassNotFoundException {
        HistoricalDataUtils.percentageChangeFromNumberOfWeeks(Exchange.BSE, SecuritiesList.PORTFOLIO_SECURITIES_BSE, 1);
    }

    @Test
    @Tested
    public void percentageChangesForCurrentWeek() throws IOException, ClassNotFoundException {
        HistoricalDataUtils.percentageChangeForCurrentWeek(Exchange.BSE, SecuritiesList.PORTFOLIO_SECURITIES_BSE);
    }

    @Test
    public void fillTechIndicators() throws IOException, ClassNotFoundException {
        List<List<CandleStick>> historicalCandleSticks = HistoricalDataUtils.readNSEHistoricalData(SecuritiesList.PORTFOLIO_SECURITIES_NSE);
        List<List<ImprovedCandleStick>> historicalImprovedCandleSticks = TechIndicatorUtils.convert(historicalCandleSticks);

        historicalImprovedCandleSticks = HistoricalDataUtils.saveEma(historicalImprovedCandleSticks, 21);
        historicalImprovedCandleSticks = HistoricalDataUtils.saveEma(historicalImprovedCandleSticks, 26);
        historicalImprovedCandleSticks = HistoricalDataUtils.saveEma(historicalImprovedCandleSticks, 12);

        historicalImprovedCandleSticks = HistoricalDataUtils.saveMacd(historicalImprovedCandleSticks);
        historicalImprovedCandleSticks = HistoricalDataUtils.saveSignal(historicalImprovedCandleSticks);
        historicalImprovedCandleSticks = HistoricalDataUtils.saveRsi(historicalImprovedCandleSticks);
        System.out.println(historicalImprovedCandleSticks);

        historicalImprovedCandleSticks.stream()
                .forEach(item ->
                        JavaUtils.serialize(FilePaths.HISTORICAL_DATA_FOLDER_PATH + CandleStick.getSymbol(item), item));
    }

    @Test
    public void manipulateLedger() throws IOException {
        Double amountAdded = Files.lines(Paths.get("/Users/hemasundarpenugonda/Downloads/ledger-EG5775.csv"))
//                .skip(2)
                .map(item -> item.split(","))
                .filter(item -> item[3].equalsIgnoreCase("Bank Receipts"))
                .map(item -> Double.parseDouble(item[5]))
                .mapToDouble(item -> item)
                .sum();
        Double amountWithdrawn = Files.lines(Paths.get("/Users/hemasundarpenugonda/Downloads/ledger-EG5775.csv"))
//                .skip(2)
                .map(item -> item.split(","))
                .filter(item -> item[3].equalsIgnoreCase("Bank Payments"))
                .map(item -> Double.parseDouble(item[4]))
                .mapToDouble(item -> item)
                .sum();

        Double amountInvested = amountAdded - amountWithdrawn;
        System.out.println(amountInvested);

        Double chargesCut = Files.lines(Paths.get("/Users/hemasundarpenugonda/Downloads/ledger-EG5775.csv"))
//                .skip(2)
                .map(item -> item.split(","))
                .filter(item -> item[3].equalsIgnoreCase("Journal Entry"))
                .map(item -> Double.parseDouble(item[4]))
                .mapToDouble(item -> item)
                .sum();

        Double dividendIncome = Files.lines(Paths.get("/Users/hemasundarpenugonda/Downloads/ledger-EG5775.csv"))
//                .skip(2)
                .map(item -> item.split(","))
                .filter(item -> item[3].equalsIgnoreCase("Journal Entry"))
                .map(item -> Double.parseDouble(item[5]))
                .mapToDouble(item -> item)
                .sum();


        Double totalAmountBought = Files.lines(Paths.get("/Users/hemasundarpenugonda/Downloads/ledger-EG5775.csv"))
//                .skip(2)
                .map(item -> item.split(","))
                .filter(item -> item[3].equalsIgnoreCase("Book Voucher"))
                .map(item -> Double.parseDouble(item[4]))
                .mapToDouble(item -> item)
                .sum();
        Double totalAmountSold = Files.lines(Paths.get("/Users/hemasundarpenugonda/Downloads/ledger-EG5775.csv"))
//                .skip(2)
                .map(item -> item.split(","))
                .filter(item -> item[3].equalsIgnoreCase("Book Voucher"))
                .map(item -> Double.parseDouble(item[5]))
                .mapToDouble(item -> item)
                .sum();
        Double currentAmountBoughtIn = totalAmountBought - totalAmountSold;

        System.out.println(currentAmountBoughtIn);
    }

}
