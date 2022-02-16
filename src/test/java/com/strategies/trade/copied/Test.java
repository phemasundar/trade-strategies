package com.strategies.trade.copied;

import com.neovisionaries.ws.client.WebSocketException;
import com.strategies.trade.api_utils.ApiRequests;
import com.strategies.trade.strategies.*;
import com.strategies.trade.test_data_beans.CustomOrderParams;
import com.strategies.trade.test_data_beans.Exchange;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import org.json.JSONException;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 7/10/16.
 * This class has example of how to initialize kiteSdk and make rest api calls to place order, get orders, modify order, cancel order,
 * get positions, get holdings, convert positions, get instruments, logout user, get historical data dump, get trades
 */
public class Test {

    public static void main(String[] args) {
        Examples examples = new Examples();
        InstrumentExamples instrumentExamples = new InstrumentExamples();
        MFExamples mfExamples = new MFExamples();
        OrderExamples orderExamples = new OrderExamples();
        TickerExamples tickerExamples = new TickerExamples();

        try {
            KiteConnect kiteConnect = examples.loginKiteConnect();

            Profile profile = examples.getProfile(kiteConnect);

            Margin equityMargins = orderExamples.getEquityMargins(kiteConnect);
            Margin commodityMargins = orderExamples.getCommodityMargins(kiteConnect);

            MarginCalculationParams param = new MarginCalculationParams();
            param.exchange = "NSE";
            param.tradingSymbol = "INFY";
            param.orderType = "MARKET";
            param.quantity = 1;
            param.product = "MIS";
            param.variety = "regular";
            double marginAmountRequired = orderExamples.getMarginAmountRequired(kiteConnect, param);

            OrderParams orderParams = new CustomOrderParams.Builder(Constants.EXCHANGE_NSE, "ASHOKLEY", Constants.TRANSACTION_TYPE_BUY)
                    .quantity(1)
                    .orderType(Constants.ORDER_TYPE_LIMIT)
                    .product(Constants.PRODUCT_CNC)
                    .validity(Constants.VALIDITY_DAY)
                    .price(122.2)
                    .triggerPrice(0.0)
                    .tag("myTag")
                    .build()
                    .getOrderParams();
            String orderID = orderExamples.placeOrder(kiteConnect, orderParams);

            OrderParams orderParams1 = new CustomOrderParams.Builder(Constants.EXCHANGE_NSE, "ASHOKLEY", Constants.TRANSACTION_TYPE_BUY)
                    .quantity(1)
                    .orderType(Constants.ORDER_TYPE_LIMIT)
                    .product(Constants.PRODUCT_CNC)
                    .validity(Constants.VALIDITY_DAY)
                    .price(122.2)
                    .build()
                    .getOrderParams();
            String modifiedOrderID = orderExamples.modifyOrder(kiteConnect, "180116000984900", orderParams1, Constants.VARIETY_REGULAR);

            String cancelledOrderID = orderExamples.cancelOrder(kiteConnect, "180116000727266", Constants.VARIETY_REGULAR);

            orderExamples.placeBracketOrder(kiteConnect);

            orderExamples.modifyFirstLegBo(kiteConnect);

            orderExamples.modifySecondLegBoSLM(kiteConnect);

            orderExamples.modifySecondLegBoLIMIT(kiteConnect);

            orderExamples.exitBracketOrder(kiteConnect);

            orderExamples.getTriggerRange(kiteConnect);

            orderExamples.placeCoverOrder(kiteConnect);

            orderExamples.converPosition(kiteConnect);

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date from = new Date();
            Date to = new Date();
            try {
                from = formatter.parse("2019-09-20 09:15:00");
                to = formatter.parse("2019-09-20 15:30:00");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            HistoricalData historicalData = instrumentExamples.getHistoricalData(kiteConnect, from, to, "54872327", "15minute");

            List<Order> orders = orderExamples.getOrders(kiteConnect);

            List<Order> order = orderExamples.getOrder(kiteConnect, "180111000561605");

            List<Trade> trades = orderExamples.getTrades(kiteConnect);

            List<Trade> tradesWithOrderId = orderExamples.getTradesWithOrderId(kiteConnect, "180111000561605");

            Map<String, List<Position>> positions = orderExamples.getPositions(kiteConnect);
            List<Position> netPositions = orderExamples.getNetPositions(kiteConnect);
            List<Position> dayPositions = orderExamples.getDayPositions(kiteConnect);

            List<Holding> holdings = orderExamples.getHoldings(kiteConnect);

            List<Instrument> allInstruments = instrumentExamples.getAllInstruments(kiteConnect);

            List<Instrument> instrumentsByExchange = instrumentExamples.getInstrumentsForExchange(kiteConnect, Constants.EXCHANGE_CDS);

            Map<String, Quote> quotes = instrumentExamples.getQuote(kiteConnect, new String[]{"256265", "BSE:INFY", "NSE:APOLLOTYRE", "NSE:NIFTY 50", "24507906"});
            Quote quote = instrumentExamples.getQuote(kiteConnect, "NSE:APOLLOTYRE");

            String[] instruments = {"256265", "BSE:INFY", "NSE:INFY", "NSE:NIFTY 50"};
            Map<String, OHLCQuote> ohlc = instrumentExamples.getOHLC(kiteConnect, instruments);

            Map<String, LTPQuote> ltp = instrumentExamples.getLTP(kiteConnect, new String[]{"256265", "BSE:INFY", "NSE:INFY", "NSE:NIFTY 50"});
            LTPQuote ltp1 = instrumentExamples.getLTP(kiteConnect, "NSE:INFY");

            orderExamples.getGTTs(kiteConnect);

            orderExamples.getGTT(kiteConnect);

            orderExamples.placeGTT(kiteConnect);

            orderExamples.modifyGTT(kiteConnect);

            orderExamples.cancelGTT(kiteConnect);

            mfExamples.getMFInstruments(kiteConnect);

            mfExamples.placeMFOrder(kiteConnect);

            mfExamples.cancelMFOrder(kiteConnect);

            mfExamples.getMFOrders(kiteConnect);

            mfExamples.getMFOrder(kiteConnect);

            mfExamples.placeMFSIP(kiteConnect);

            mfExamples.modifyMFSIP(kiteConnect);

            mfExamples.cancelMFSIP(kiteConnect);

            mfExamples.getMFSIPS(kiteConnect);

            mfExamples.getMFSIP(kiteConnect);

            mfExamples.getMFHoldings(kiteConnect);

            examples.logout(kiteConnect);

            ArrayList<Long> tokens = new ArrayList<>();
            tokens.add(Long.parseLong("256265"));
            tickerExamples.tickerUsage(kiteConnect, tokens);
        } catch (KiteException e) {
            System.out.println(e.message + " " + e.code + " " + e.getClass().getName());
        } catch (JSONException | WebSocketException | IOException e) {
            e.printStackTrace();
        }
    }

}
