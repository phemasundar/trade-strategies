package com.strategies.trade.strategies;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author hemasundarpenugonda
 */
public class InstrumentExamples {


    /**
     * Get all instruments that can be traded using kite connect.
     *
     * @return list of all instruments across all exchanges
     */
    public List<Instrument> getAllInstruments(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get all instruments list. This call is very expensive as it involves downloading of large data dump.
        // Hence, it is recommended that this call be made once and the results stored locally once every morning before market opening.
        List<Instrument> instruments = kiteConnect.getInstruments();
        System.out.println(instruments.size());
        return instruments;
    }

    /**
     * Get instruments for the desired exchange.
     *
     * @return list of all instruments in the given exchange
     */
    public List<Instrument> getInstrumentsForExchange(KiteConnect kiteConnect, String exchange) throws KiteException, IOException {
        // Get instruments for an exchange.
        List<Instrument> instrumentsByExchange = kiteConnect.getInstruments(exchange);
        System.out.println(instrumentsByExchange.size());
        return instrumentsByExchange;
    }

    /**
     * Get quote for a scrip.
     * @return Quotes of all the instruments given
     */
    public Map<String, Quote> getQuote(KiteConnect kiteConnect, String[] instruments) throws KiteException, IOException {
        // Get quotes returns quote for desired trading symbol
        Map<String, Quote> quotes = kiteConnect.getQuote(instruments);
        System.out.println(quotes.get("NSE:APOLLOTYRE").instrumentToken + "");
        System.out.println(quotes.get("NSE:APOLLOTYRE").oi + "");
        System.out.println(quotes.get("NSE:APOLLOTYRE").depth.buy.get(4).getPrice());
        System.out.println(quotes.get("NSE:APOLLOTYRE").timestamp);
        System.out.println(quotes.get("NSE:APOLLOTYRE").lowerCircuitLimit + "");
        System.out.println(quotes.get("NSE:APOLLOTYRE").upperCircuitLimit + "");
        System.out.println(quotes.get("24507906").oiDayHigh);
        System.out.println(quotes.get("24507906").oiDayLow);

        return quotes;
    }

    /**
     * Get quote for a scrip.
     * @return Quotes of the instrument given
     */
    public Quote getQuote(KiteConnect kiteConnect, String instrument) throws KiteException, IOException {
        Map<String, Quote> quote = getQuote(kiteConnect, new String[]{instrument});
        return quote.get(instrument);
    }

    /* Get ohlc and lastprice for multiple instruments at once.
     * Users can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}*/
    public Map<String, OHLCQuote> getOHLC(KiteConnect kiteConnect, String[] instruments) throws KiteException, IOException {
        Map<String, OHLCQuote> ohlc = kiteConnect.getOHLC(instruments);
        System.out.println(ohlc.get("256265").lastPrice);
        System.out.println(ohlc.get("NSE:NIFTY 50").ohlc.open);

        return ohlc;
    }

    /**
     * Get last price for multiple instruments at once.
     * USers can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
     *
     * @return LTPQuote objects for all the instruments given
     */
    public Map<String, LTPQuote> getLTP(KiteConnect kiteConnect, String[] instruments) throws KiteException, IOException {
        Map<String, LTPQuote> ltp = kiteConnect.getLTP(instruments);
        System.out.println(ltp.get("256265").lastPrice);
        return ltp;
    }

    /**
     * Get last price for multiple instruments at once.
     * USers can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
     *
     * @return LTPQuote object for the instrument given
     */
    public LTPQuote getLTP(KiteConnect kiteConnect, String instrument) throws KiteException, IOException {
        Map<String, LTPQuote> ltp = getLTP(kiteConnect, new String[]{instrument});
        System.out.println(ltp.get("256265").lastPrice);
        return ltp.get(instrument);
    }

    /**
     * Get historical data for an instrument.
     * @return
     */
    public HistoricalData getHistoricalData(KiteConnect kiteConnect, Date from, Date to, String instrumentToken, String interval) throws KiteException, IOException {
        /** Get historical data dump, requires from and to date, instrument token, interval, continuous (for expired F&O contracts), oi (open interest)
         * returns historical data object which will have list of historical data inside the object.*/
        HistoricalData historicalData = kiteConnect.getHistoricalData(from, to, instrumentToken, interval, false, true);
        System.out.println(historicalData.dataArrayList.size());
        System.out.println(historicalData.dataArrayList.get(0).volume);
        System.out.println(historicalData.dataArrayList.get(historicalData.dataArrayList.size() - 1).volume);
        System.out.println(historicalData.dataArrayList.get(0).oi);

        return historicalData;
    }

}
