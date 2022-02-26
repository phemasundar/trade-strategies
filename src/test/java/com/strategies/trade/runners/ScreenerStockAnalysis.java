package com.strategies.trade.runners;

import com.strategies.trade.api_test_beans.BaseResponse;
import com.strategies.trade.api_test_beans.CustomResponse;
import com.strategies.trade.api_utils.ApiRequests;
import com.strategies.trade.test_data_beans.FilePaths;
import com.strategies.trade.test_data_beans.Instruments;
import com.strategies.trade.test_data_beans.ScreenerStockDetails;
import com.strategies.trade.test_data_beans.TestData;
import com.strategies.trade.utilities.CustomCsvUtils;
import com.strategies.trade.utilities.JavaUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.Test;
import us.codecraft.xsoup.Xsoup;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */
public class ScreenerStockAnalysis {

    @Test
    public void createScreenerDetailsForAllEquityInstruments() throws IOException, InterruptedException {

        Instruments instrumentsData = TestData.getInstrumentsData();
        List<Instruments.Instrument> allStocksInNSE = instrumentsData.getInstruments()
                .stream()
                .filter(item -> item.getExchange().trim().equalsIgnoreCase("NSE"))
                .filter(item -> item.getSegment().trim().equalsIgnoreCase("NSE"))
                .filter(item -> item.getTick_size().trim().equalsIgnoreCase("0.05"))
                .filter(item -> !item.getName().isBlank())
                .collect(Collectors.toList());
        System.out.println("Total number of stocks: " + allStocksInNSE.size());

        List<ScreenerStockDetails> res = new ArrayList<>();
        res.add(ScreenerStockDetails.getCsvHeader());
        for (Instruments.Instrument indStock : allStocksInNSE) {
            Thread.sleep(2000);
            CustomResponse<BaseResponse> screenerStockDetailsObj = ApiRequests.getScreenerStockDetails(indStock.getTradingsymbol());
            String htmlResponse = screenerStockDetailsObj.getResponse();

            Document document = Jsoup.parse(htmlResponse);

            List<String> pros = Xsoup.compile("//*[@class='pros']//li/text()").evaluate(document).list();
            List<String> cons = Xsoup.compile("//*[@class='cons']//li/text()").evaluate(document).list();
//            String pageTitle = Xsoup.compile("//title/text()").evaluate(document).list().get(0);

            if (pros.isEmpty() && cons.isEmpty()) {
                System.out.println("");
            }
            ScreenerStockDetails screenStockDetails = new ScreenerStockDetails(indStock.getTradingsymbol(), indStock.getName(), pros, cons);

            System.out.println(screenStockDetails);
            res.add(screenStockDetails);

        }

        JavaUtils.serialize(FilePaths.SCREENER_STOCK_DETAILS_OBJ_FILE, res);

        String collect = JavaUtils.deSerialize(FilePaths.SCREENER_STOCK_DETAILS_OBJ_FILE, ScreenerStockDetails.class)
                .stream()
                .map(ScreenerStockDetails::toString)
                .collect(Collectors.joining("\n"));
        CustomCsvUtils.writeToCSV(Path.of(FilePaths.SCREENER_STOCK_DETAILS_CSV_FILE), collect);

    }

    @Test
    public void getAllInstrumentsWithZeroConsInScreener() {
        List<ScreenerStockDetails> screenerStockDetails = JavaUtils.deSerialize(FilePaths.SCREENER_STOCK_DETAILS_OBJ_FILE, ScreenerStockDetails.class);
        List<ScreenerStockDetails> allScreenerStockDetails = screenerStockDetails.stream()
                .skip(1)
                .collect(Collectors.toList());

        List<String> collect = allScreenerStockDetails.stream()
                .filter(item -> item.getCons().size() == 0)
                .map(ScreenerStockDetails::getStockName)
                .collect(Collectors.toList());
        System.out.println(collect);
    }
}
