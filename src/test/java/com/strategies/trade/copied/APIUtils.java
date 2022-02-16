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
