package com.strategies.trade.test_data_beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hemasundarpenugonda
 */

@Data
@AllArgsConstructor
public class Instruments {
    private List<Instrument> instruments;

    public static Instruments getInstrumentsObject(List<List<String>> instrumentAsStrings) {

        List<Instrument> collect = instrumentAsStrings.stream()
                .map(Instrument::new)
                .collect(Collectors.toList());

        return new Instruments(collect);
    }

    @Data
    @NoArgsConstructor
    public static class Instrument {
        private String instrument_token;
        private String exchange_token;
        private String tradingsymbol;
        private String name;
        private String last_price;
        private String expiry;
        private String strike;
        private String tick_size;
        private String lot_size;
        private String instrument_type;
        private String segment;
        private String exchange;

        protected Instrument(List<String> str) {
            this.instrument_token = str.get(0);
            this.exchange_token = str.get(1);
            this.tradingsymbol = str.get(2).split("-")[0];
            this.name = str.get(3);
            this.last_price = str.get(4);
            this.expiry = str.get(5);
            this.strike = str.get(6);
            this.tick_size = str.get(7);
            this.lot_size = str.get(8);
            this.instrument_type = str.get(9);
            this.segment = str.get(10);
            this.exchange = str.get(11);
        }
    }

}
