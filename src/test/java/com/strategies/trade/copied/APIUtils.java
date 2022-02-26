package com.strategies.trade.copied;

import com.strategies.trade.api_test_beans.CandleStick;
import com.strategies.trade.api_test_beans.ImprovedCandleStick;
import com.strategies.trade.test_data_beans.Exchange;
import com.strategies.trade.test_data_beans.FilePaths;
import com.strategies.trade.test_data_beans.Securities;
import com.strategies.trade.utilities.HistoricalDataUtils;
import com.strategies.trade.utilities.JavaUtils;
import com.strategies.trade.utilities.TechIndicatorUtils;
import com.strategies.trade.utilities.Tested;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */
public class APIUtils {

    @Test
    @Tested
    public void percentageChangesFromMonths() throws IOException, ClassNotFoundException {
        HistoricalDataUtils.percentageChangeFromNumberOfMonths(Exchange.BSE, Securities.INDEX, "S&P BSE 500", 2);
    }

    @Test
    @Tested
    public void percentageChangesFromWeeks() throws IOException, ClassNotFoundException {
        HistoricalDataUtils.percentageChangeFromNumberOfWeeks(Exchange.BSE, Securities.PORTFOLIO, "", 1);
    }

    @Test
    @Tested
    public void percentageChangesForCurrentWeek() throws IOException, ClassNotFoundException {
        HistoricalDataUtils.percentageChangeForCurrentWeek(Exchange.BSE, Securities.PORTFOLIO, "");
    }

    @Test
    public void fillTechIndicators() throws IOException, ClassNotFoundException {
        List<String> securitiesList = Exchange.NSE.getSecuritiesList(Securities.PORTFOLIO, "");

        List<List<CandleStick>> historicalCandleSticks = HistoricalDataUtils.readNSEHistoricalData(securitiesList);
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
                        JavaUtils.serialize(Exchange.NSE.getDataFolderPath() + FilePaths.HISTORICAL_DATA_FOLDER + CandleStick.getSymbol(item), item));
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
