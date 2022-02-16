package com.strategies.trade.runners;

import com.strategies.trade.api_test_beans.BaseResponse;
import com.strategies.trade.api_test_beans.CustomResponse;
import com.strategies.trade.api_utils.ApiRequests;
import com.strategies.trade.test_data_beans.*;
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
public class StocksInSmallcaseAnalysis {

    @Test
    public void createCustomKiteHoldings() throws IOException {

        Instruments instrumentsData = TestData.getInstrumentsData();
        List<Instruments.Instrument> allStocksInNSE = instrumentsData.getInstruments()
                .stream()
                .filter(item -> item.getExchange().equalsIgnoreCase("NSE"))
                .filter(item -> !item.getName().isBlank())
                .collect(Collectors.toList());
        System.out.println("Total number of stockts: " + allStocksInNSE.size());

        List<SmallcaseAdvInfo> res = new ArrayList<>();
        res.add(SmallcaseAdvInfo.getCsvHeader());
        for (Instruments.Instrument indStock : allStocksInNSE) {
            CustomResponse<BaseResponse> smallCaseDetails = ApiRequests.getSmallCaseBannerDetailsForStock(indStock.getTradingsymbol());
            String htmlResponse = smallCaseDetails.getResponse();
            Document document = Jsoup.parse(htmlResponse);

            List<String> list = Xsoup.compile("//a[contains(@href, 'smallcase')]//h4/text()").evaluate(document).list();
            String json = String.valueOf(Xsoup.compile("//script[@id='__NEXT_DATA__']/text()").evaluate(document));

            SmallcaseAdvInfo smallcaseAdvInfo = new SmallcaseAdvInfo(indStock.getTradingsymbol(), indStock.getName(), list, json);

            System.out.println(smallcaseAdvInfo);
            res.add(smallcaseAdvInfo);

        }

        JavaUtils.serialize(FilePaths.PROCESSED_SMALLCASE_OBJ_FILE, res);

//        String collect = res.stream().map(SmallcaseAdvInfo::toString)
//                .collect(Collectors.joining("\n"));
//        CustomCsvUtils.writeToCSV(Path.of(FilePaths.PROCESSED_SMALLCASE_CSV_FILE), collect);
        String collect = JavaUtils.deSerialize(FilePaths.PROCESSED_SMALLCASE_OBJ_FILE, SmallcaseAdvInfo.class)
                .stream()
                .map(SmallcaseAdvInfo::toString)
                .collect(Collectors.joining("\n"));
        CustomCsvUtils.writeToCSV(Path.of(FilePaths.PROCESSED_SMALLCASE_CSV_FILE), collect);

    }

    @Test
    public void analyseSmallcaseObjFile() throws IOException {
        List<SmallcaseAdvInfo> smallcaseAdvInfos = JavaUtils.deSerialize(FilePaths.PROCESSED_SMALLCASE_OBJ_FILE, SmallcaseAdvInfo.class);
        List<String> allSmallcaseNames = smallcaseAdvInfos.stream()
                .skip(1)
                .filter(item -> !item.getSmallcaseWeight().trim().isBlank())
                .map(SmallcaseAdvInfo::getSmallcaseName)
//                .filter(item -> !item.equalsIgnoreCase("All Weather Investing"))
                .distinct()
                .collect(Collectors.toList());

        System.out.println("All smallcase names: \n\t" + String.join(", ", allSmallcaseNames));

        List<Smallcase> allSmallcaseObjects = new ArrayList<>();
        for (String indSmallcaseName : allSmallcaseNames) {
            List<Smallcase.IndStock> collect = smallcaseAdvInfos.stream()
                    .filter(item -> item.getSmallcaseName().equalsIgnoreCase(indSmallcaseName))
                    .map(item -> new Smallcase.IndStock(item.getTradingSymbol(), item.getStockName(), item.getSmallcaseWeight()))
                    .collect(Collectors.toList());
            Smallcase smallcase = new Smallcase(indSmallcaseName, collect);
            allSmallcaseObjects.add(smallcase);
        }

        List<Smallcase> collect = allSmallcaseObjects.stream()
                .filter(item -> item.getTotalWeight() > 50.0)
                .collect(Collectors.toList());

        String collect1 = collect.stream()
                .map(Smallcase::toString)
                .collect(Collectors.joining("\n"));

        CustomCsvUtils.writeToCSV(Path.of(FilePaths.PROCESSED_SMALLCASE_CSV_FILE1), Smallcase.getCsvHeader() + "\n" + collect1);
    }
}
