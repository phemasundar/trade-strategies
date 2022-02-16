package com.strategies.trade.test_data_beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.strategies.trade.utilities.CustomCsvUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */
//TODO: Need to synchronise
public class TestData {

    private static TagData tagData;
    private static Instruments instrumentsData;

    public static TagData getAllTagsData() {
        if (tagData == null) {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            try {
                tagData = mapper.readValue(new File(FilePaths.TAGS_DATA_JSON), TagData.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return tagData;
    }

    public static Instruments getInstrumentsData() {
        if (instrumentsData == null) {
            try {
                List<List<String>> instrumentAsStrings = CustomCsvUtils.readCSV(Path.of(FilePaths.INSTRUMENTS_CSV_FILE), 1);
                instrumentsData = Instruments.getInstrumentsObject(instrumentAsStrings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return instrumentsData;
    }

}