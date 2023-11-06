package com.strategies.trade.strategies;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author hemasundarpenugonda
 */
public class OrderExamples {


    public void getProfile(KiteConnect kiteConnect) throws IOException, KiteException {
        Profile profile = kiteConnect.getProfile();
        System.out.println(profile.userName);
    }

    /**Gets Margin.
     * @return*/
    private Margin getMargins(KiteConnect kiteConnect, String marginType) throws KiteException, IOException {
        // Get margins returns margin model, you can pass equity or commodity as arguments to get margins of respective segments.
        Margin margins = kiteConnect.getMargins(marginType);
        System.out.println(margins.available.cash);
        System.out.println(margins.utilised.debits);
        System.out.println(margins.utilised.m2mUnrealised);

        return margins;
    }

    /**Gets Equity Margin.
     * @return*/
    public Margin getEquityMargins(KiteConnect kiteConnect) throws KiteException, IOException {
        return getMargins(kiteConnect, Constants.MARGIN_EQUITY);
    }

    /**Gets Commodity Margin.
     * @return*/
    public Margin getCommodityMargins(KiteConnect kiteConnect) throws KiteException, IOException {
        return getMargins(kiteConnect, Constants.MARGIN_COMMODITY);
    }

    @Deprecated
    public void getMarginCalculation(KiteConnect kiteConnect) throws IOException, KiteException {
        MarginCalculationParams param = new MarginCalculationParams();
        param.exchange = "NSE";
        param.tradingSymbol = "INFY";
        param.orderType = "MARKET";
        param.quantity = 1;
        param.product = "MIS";
        param.variety = "regular";
        List<MarginCalculationParams> params = new ArrayList<>();
        params.add(param);
        List<MarginCalculationData> data = kiteConnect.getMarginCalculation(params);
        System.out.println(data.get(0).total);
    }

    public double getMarginAmountRequired(KiteConnect kiteConnect, MarginCalculationParams param) throws IOException, KiteException {
        List<MarginCalculationParams> params = new ArrayList<>();
        params.add(param);
        return getMarginAmountRequired(kiteConnect, params);
    }

    public double getMarginAmountRequired(KiteConnect kiteConnect, List<MarginCalculationParams> params) throws IOException, KiteException {
        List<MarginCalculationData> data = kiteConnect.getMarginCalculation(params);
        Double totalMarginRequired = data.stream().mapToDouble(item -> item.total)
                .sum();
        System.out.println(totalMarginRequired);
        return totalMarginRequired;
    }

    public void getCombinedMarginCalculation(KiteConnect kiteConnect) throws IOException, KiteException{
        List<MarginCalculationParams> params = new ArrayList<>();

        MarginCalculationParams param = new MarginCalculationParams();
        param.exchange = "NFO";
        param.tradingSymbol = "NIFTY21MARFUT";
        param.orderType = "LIMIT";
        param.quantity = 75;
        param.product = "MIS";
        param.variety = "regular";
        param.transactionType = "BUY";
        param.price = 141819;

        MarginCalculationParams param2 = new MarginCalculationParams();
        param2.exchange = "NFO";
        param2.tradingSymbol = "NIFTY21MAR15000PE";
        param2.orderType = "LIMIT";
        param2.quantity = 75;
        param2.product = "MIS";
        param2.variety = "regular";
        param.transactionType = "BUY";
        param2.price = 300;

        params.add(param);
        params.add(param2);

        CombinedMarginData combinedMarginData = kiteConnect.getCombinedMarginCalculation(params, true, false);
        System.out.println(combinedMarginData.initialMargin.total);
    }

    /**Place order.
     * @return order id of the placed order*/
    public String placeOrder(KiteConnect kiteConnect, OrderParams orderParams) throws KiteException, IOException {
        /** Place order method requires a orderParams argument which contains,
         * tradingsymbol, exchange, transaction_type, order_type, quantity, product, price, trigger_price, disclosed_quantity, validity
         * squareoff_value, stoploss_value, trailing_stoploss
         * and variety (value can be regular, bo, co, amo)
         * place order will return order model which will have only orderId in the order model
         *
         * Following is an example param for LIMIT order,
         * if a call fails then KiteException will have error message in it
         * Success of this call implies only order has been placed successfully, not order execution. */

        Order order = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
        System.out.println(order.orderId);
        return order.orderId;
    }

    /** Place bracket order.*/
    public void placeBracketOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Bracket order:- following is example param for bracket order*
         * trailing_stoploss and stoploss_value are points and not tick or price
         */
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.price = 30.5;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.trailingStoploss = 1.0;
        orderParams.stoploss = 2.0;
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.squareoff = 3.0;
        orderParams.product = Constants.PRODUCT_MIS;
        Order order10 = kiteConnect.placeOrder(orderParams, Constants.VARIETY_BO);
        System.out.println(order10.orderId);
    }

    /** Place cover order.*/
    public void placeCoverOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Cover Order:- following is an example param for the cover order
         * key: quantity value: 1
         * key: price value: 0
         * key: transaction_type value: BUY
         * key: tradingsymbol value: HINDALCO
         * key: exchange value: NSE
         * key: validity value: DAY
         * key: trigger_price value: 157
         * key: order_type value: MARKET
         * key: variety value: co
         * key: product value: MIS
         */
        OrderParams orderParams = new OrderParams();
        orderParams.price = 0.0;
        orderParams.quantity = 1;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.orderType = Constants.ORDER_TYPE_MARKET;
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.triggerPrice = 30.5;
        orderParams.product = Constants.PRODUCT_MIS;

        Order order11 = kiteConnect.placeOrder(orderParams, Constants.VARIETY_CO);
        System.out.println(order11.orderId);
    }

    /** Get trigger range.*/
    public void getTriggerRange(KiteConnect kiteConnect) throws KiteException, IOException {
        // You need to send transaction_type, exchange and tradingsymbol to get trigger range.
        String[] instruments = {"BSE:INFY", "NSE:APOLLOTYRE", "NSE:SBIN"};
        Map<String, TriggerRange> triggerRangeMap = kiteConnect.getTriggerRange(instruments, Constants.TRANSACTION_TYPE_BUY);
        System.out.println(triggerRangeMap.get("NSE:SBIN").lower);
        System.out.println(triggerRangeMap.get("NSE:APOLLOTYRE").upper);
        System.out.println(triggerRangeMap.get("BSE:INFY").percentage);
    }

    /** Get orderbook.
     * @return*/
    public List<Order> getOrders(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get orders returns order model which will have list of orders inside, which can be accessed as follows,
        List<Order> orders = kiteConnect.getOrders();
        for(int i = 0; i< orders.size(); i++){
            System.out.println(orders.get(i).tradingSymbol+" "+orders.get(i).orderId+" "+orders.get(i).parentOrderId+
                    " "+orders.get(i).orderType+" "+orders.get(i).averagePrice+" "+orders.get(i).exchangeTimestamp+" "+orders.get(i).exchangeUpdateTimestamp+" "+orders.get(i).guid);
        }
        System.out.println("list of orders size is "+orders.size());

        return orders;
    }

    /** Get order details
     * @return*/
    public List<Order> getOrder(KiteConnect kiteConnect, String orderID) throws KiteException, IOException {
        List<Order> orders = kiteConnect.getOrderHistory(orderID);
        for(int i = 0; i< orders.size(); i++){
            System.out.println(orders.get(i).orderId+" "+orders.get(i).status);
        }
        System.out.println("list size is "+orders.size());

        return orders;
    }

    /** Get tradebook
     * @return*/
    public List<Trade> getTrades(KiteConnect kiteConnect) throws KiteException, IOException {
        // Returns tradebook.
        List<Trade> trades = kiteConnect.getTrades();
        for (int i=0; i < trades.size(); i++) {
            System.out.println(trades.get(i).tradingSymbol+" "+trades.size());
        }
        System.out.println(trades.size());

        return trades;
    }

    /** Get trades for an order.
     * @return*/
    public List<Trade> getTradesWithOrderId(KiteConnect kiteConnect, String orderID) throws KiteException, IOException {
        // Returns trades for the given order.
        List<Trade> trades = kiteConnect.getOrderTrades(orderID);
        System.out.println(trades.size());

        return trades;
    }

    /** Modify order.
     * @return*/
    public String modifyOrder(KiteConnect kiteConnect, String orderID, OrderParams orderParams, String orderVariety) throws KiteException, IOException {
        // Order modify request will return order model which will contain only order_id.

        Order order21 = kiteConnect.modifyOrder(orderID, orderParams, orderVariety);
        System.out.println(order21.orderId);
        return order21.orderId;
    }

    /** Modify first leg bracket order.*/
    public void modifyFirstLegBo(KiteConnect kiteConnect) throws KiteException, IOException {
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.price = 31.0;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.tag = "myTag";
        orderParams.triggerPrice = 0.0;

        Order order = kiteConnect.modifyOrder("180116000798058", orderParams, Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    public void modifySecondLegBoSLM(KiteConnect kiteConnect) throws KiteException, IOException {

        OrderParams orderParams = new OrderParams();
        orderParams.parentOrderId = "180116000798058";
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.triggerPrice = 30.5;
        orderParams.price = 0.0;
        orderParams.orderType = Constants.ORDER_TYPE_SLM;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;

        Order order = kiteConnect.modifyOrder("180116000812154", orderParams, Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    public void modifySecondLegBoLIMIT(KiteConnect kiteConnect) throws KiteException, IOException {
        OrderParams orderParams =  new OrderParams();
        orderParams.parentOrderId = "180116000798058";
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.quantity =  1;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 35.3;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;

        Order order = kiteConnect.modifyOrder("180116000812153", orderParams, Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    /** Cancel an order
     * @return*/
    public String cancelOrder(KiteConnect kiteConnect, String orderID, String orderVariety) throws KiteException, IOException {
        // Cancel order will return order model which will only have orderId.
        Order order2 = kiteConnect.cancelOrder(orderID, orderVariety);
        System.out.println(order2.orderId);
        return order2.orderId;
    }

    public void exitBracketOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        Order order = kiteConnect.cancelOrder("180116000812153","180116000798058", Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    /**Get all gtts. */
    public void getGTTs(KiteConnect kiteConnect) throws KiteException, IOException {
        List<GTT> gtts = kiteConnect.getGTTs();
        System.out.println(gtts.get(0).createdAt);
        System.out.println(gtts.get(0).condition.exchange);
        System.out.println(gtts.get(0).orders.get(0).price);
    }

    /** Get a particular GTT. */
    public void getGTT(KiteConnect kiteConnect) throws IOException, KiteException {
        GTT gtt = kiteConnect.getGTT(177574);
        System.out.println(gtt.condition.tradingSymbol);
    }

    /** Place a GTT (Good till trigger)*/
    public void placeGTT(KiteConnect kiteConnect) throws IOException, KiteException {
        GTTParams gttParams = new GTTParams();
        gttParams.triggerType = Constants.OCO;
        gttParams.exchange = "NSE";
        gttParams.tradingsymbol = "SBIN";
        gttParams.lastPrice = 302.95;

        List<Double> triggerPrices = new ArrayList<>();
        triggerPrices.add(290d);
        triggerPrices.add(320d);
        gttParams.triggerPrices = triggerPrices;

        /** Only sell is allowed for OCO or two-leg orders.
         * Single leg orders can be buy or sell order.
         * Passing a last price is mandatory.
         * A stop-loss order must have trigger and price below last price and target order must have trigger and price above last price.
         * Only limit order type  and CNC product type is allowed for now.
         * */

        /** Stop-loss or lower trigger. */
        GTTParams.GTTOrderParams order1Params = gttParams. new GTTOrderParams();
        order1Params.orderType = Constants.ORDER_TYPE_LIMIT;
        order1Params.price = 290;
        order1Params.product = Constants.PRODUCT_CNC;
        order1Params.transactionType = Constants.TRANSACTION_TYPE_SELL;
        order1Params.quantity = 0;

        GTTParams.GTTOrderParams order2Params = gttParams. new GTTOrderParams();
        order2Params.orderType = Constants.ORDER_TYPE_LIMIT;
        order2Params.price = 320;
        order2Params.product = Constants.PRODUCT_CNC;
        order2Params.transactionType = Constants.TRANSACTION_TYPE_SELL;
        order2Params.quantity = 1;

        /** Target or upper trigger. */
        List<GTTParams.GTTOrderParams> ordersList = new ArrayList();
        ordersList.add(order1Params);
        ordersList.add(order2Params);
        gttParams.orders = ordersList;

        GTT gtt = kiteConnect.placeGTT(gttParams);
        System.out.println(gtt.id);
    }

    /** Modify a GTT (Good till trigger)*/
    public void modifyGTT(KiteConnect kiteConnect) throws IOException, KiteException {
        GTTParams gttParams = new GTTParams();
        gttParams.triggerType = Constants.OCO;
        gttParams.exchange = "NSE";
        gttParams.tradingsymbol = "SBIN";
        gttParams.lastPrice = 302.95;

        List<Double> triggerPrices = new ArrayList<>();
        triggerPrices.add(290d);
        triggerPrices.add(320d);
        gttParams.triggerPrices = triggerPrices;

        GTTParams.GTTOrderParams order1Params = gttParams. new GTTOrderParams();
        order1Params.orderType = Constants.ORDER_TYPE_LIMIT;
        order1Params.price = 290;
        order1Params.product = Constants.PRODUCT_CNC;
        order1Params.transactionType = Constants.TRANSACTION_TYPE_SELL;
        order1Params.quantity = 1;

        GTTParams.GTTOrderParams order2Params = gttParams. new GTTOrderParams();
        order2Params.orderType = Constants.ORDER_TYPE_LIMIT;
        order2Params.price = 320;
        order2Params.product = Constants.PRODUCT_CNC;
        order2Params.transactionType = Constants.TRANSACTION_TYPE_SELL;
        order2Params.quantity = 1;

        List<GTTParams.GTTOrderParams> ordersList = new ArrayList();
        ordersList.add(order1Params);
        ordersList.add(order2Params);
        gttParams.orders = ordersList;

        GTT gtt = kiteConnect.modifyGTT(176036, gttParams);
        System.out.println(gtt.id);
    }

    /** Cancel a GTT.*/
    public void cancelGTT(KiteConnect kiteConnect) throws IOException, KiteException {
        GTT gtt = kiteConnect.cancelGTT(175859);
        System.out.println(gtt.id);
    }

    /** Get all positions.
     * @return*/
    public Map<String, List<Position>> getPositions(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get positions returns position model which contains list of positions.
        Map<String, List<Position>> positions = kiteConnect.getPositions();
        System.out.println(positions.get("net").size());
        System.out.println(positions.get("day").size());
        System.out.println(positions.get("net").get(0).averagePrice);

        return positions;
    }

    /** Get all positions.
     * @return*/
    public List<Position> getNetPositions(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get positions returns position model which contains list of positions.
        Map<String, List<Position>> positions = kiteConnect.getPositions();
        System.out.println(positions.get("net").size());
        System.out.println(positions.get("day").size());
        System.out.println(positions.get("net").get(0).averagePrice);

        return positions.get("net");
    }

    /** Get all positions.
     * @return*/
    public List<Position> getDayPositions(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get positions returns position model which contains list of positions.
        Map<String, List<Position>> positions = kiteConnect.getPositions();
        System.out.println(positions.get("net").size());
        System.out.println(positions.get("day").size());
        System.out.println(positions.get("net").get(0).averagePrice);

        return positions.get("day");
    }

    /** Get holdings.
     * @return*/
    public List<Holding> getHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get holdings returns holdings model which contains list of holdings.
        List<Holding> holdings = kiteConnect.getHoldings();
        System.out.println(holdings.size());

        return holdings;
    }

    /** Converts position*/
    public void converPosition(KiteConnect kiteConnect) throws KiteException, IOException {
        //Modify product can be used to change MIS to NRML(CNC) or NRML(CNC) to MIS.
        JSONObject jsonObject6 = kiteConnect.convertPosition("ASHOKLEY", Constants.EXCHANGE_NSE, Constants.TRANSACTION_TYPE_BUY, Constants.POSITION_DAY, Constants.PRODUCT_MIS, Constants.PRODUCT_CNC, 1);
        System.out.println(jsonObject6);
    }



}
