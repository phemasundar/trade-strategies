package com.strategies.trade.copied;

import com.strategies.trade.test_data_beans.Exchange;
import com.strategies.trade.test_data_beans.FilePaths;
import com.strategies.trade.test_data_beans.Securities;
import com.strategies.trade.utilities.JavaUtils;
import com.strategies.trade.utilities.TechIndicatorUtils;
import com.strategies.trade.utilities.Tested;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */
public class HistoricalDataTest {

    @Test
    @Tested
    public void writeBSEHistoricalDataForAllStocks() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");

        for (LocalDate startDateObj = LocalDate.of(2019, 1, 1);
             startDateObj.isBefore(LocalDate.now());
             startDateObj = startDateObj.plusDays(1)) {

            if (startDateObj.getDayOfWeek() == DayOfWeek.SATURDAY || startDateObj.getDayOfWeek() == DayOfWeek.SUNDAY)
                continue;

            String zipFileName = "EQ_ISINCODE_" + formatter.format(startDateObj) + ".zip";
            String zipFilePath = Exchange.BSE.getDataFolderPath() + FilePaths.DAILY_ZIP_FILES_FOLDER + zipFileName;

            if (!new File(zipFilePath).exists()) {

                JavaUtils.executeCommand(("curl https://www.bseindia.com/download/BhavCopy/Equity/" + zipFileName + " --output " + zipFilePath).split(" "));
                try {
                    new ZipFile(zipFilePath).extractAll(Exchange.BSE.getDataFolderPath() + FilePaths.DAILY_EXTRACTED_FILES_FOLDER);
                    // Ignoring the exception. As this means the zip file is of on Holiday.
                } catch (ZipException ignored) {}
            }
        }

    }

    @Test
    @Tested
    public void writeNSEHistoricalData4ListOfSecurities() throws IOException {

//        List<String> allSecurities = SecuritiesList.PORTFOLIO_SECURITIES.getSecuritiesList();
//        List<String> allSecurities1 = SecuritiesList.ALL_INDEX_SECURITIES_NSE.getSecuritiesList();
        List<String> allSecurities = Exchange.NSE.getSecuritiesList(Securities.ALL, "");

        TechIndicatorUtils.writeOrUpdateHistoricalData(allSecurities);
    }

    @Test
    @Tested
    public void writeAllIndexDetails() throws IOException {
        Exchange.NSE.writeAllIndexDetails();
    }

}
