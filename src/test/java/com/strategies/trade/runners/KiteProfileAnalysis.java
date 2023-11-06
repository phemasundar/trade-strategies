package com.strategies.trade.runners;

import com.strategies.trade.test_data_beans.DefaultKiteHoldings;
import com.strategies.trade.test_data_beans.FilePaths;
import com.strategies.trade.test_data_beans.ProcessedKiteHoldings;
import com.strategies.trade.utilities.CustomCsvUtils;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */
public class KiteProfileAnalysis {

    @Test
    public void createCustomKiteHoldings() throws IOException {
        List<List<String>> defaultKiteHoldingAsStrings = CustomCsvUtils.readCSV(Path.of(FilePaths.HOLDINGS_CSV_FILE), 1);
        DefaultKiteHoldings defaultKiteHoldings = DefaultKiteHoldings.getDefaultKiteHoldingsObject(defaultKiteHoldingAsStrings);

        ProcessedKiteHoldings processedHoldingsObject = ProcessedKiteHoldings.getProcessedKiteHoldingsObject(defaultKiteHoldings)
                .calculatePercentageInPortfolio()
                .assignTags()
                .calculatePercentageOfTagsInPortfolio();

        String s = processedHoldingsObject.convertToCSVFormat();
        CustomCsvUtils.writeToCSV(Path.of(FilePaths.PROCESSED_HOLDINGS_CSV_FILE), s);


    }
}
