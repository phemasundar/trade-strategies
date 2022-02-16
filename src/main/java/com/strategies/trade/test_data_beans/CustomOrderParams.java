package com.strategies.trade.test_data_beans;

import com.zerodhatech.models.OrderParams;

/**
 * @author hemasundarpenugonda
 */
public class CustomOrderParams {
    private String exchange;
    private String tradingsymbol;
    private String transactionType;
    private Integer quantity;
    private Double price;
    private String product;
    private String orderType;
    private String validity;
    private Integer disclosedQuantity;
    private Double triggerPrice;
    private Double squareoff;
    private Double stoploss;
    private Double trailingStoploss;
    private String tag;
    private String parentOrderId;

    public static class Builder {
        private String exchange;
        private String tradingsymbol;
        private String transactionType;
        private Integer quantity;
        private Double price;
        private String product;
        private String orderType;
        private String validity;
        private Integer disclosedQuantity;
        private Double triggerPrice;
        private Double squareoff;
        private Double stoploss;
        private Double trailingStoploss;
        private String tag;
        private String parentOrderId;

        public Builder(String exchange, String tradingsymbol, String transactionType) {
            this.exchange = exchange;
            this.tradingsymbol = tradingsymbol;
            this.transactionType = transactionType;
        }

        public Builder quantity(Integer val) {
            quantity = val;
            return this;
        }

        public Builder price(Double val) {
            price = val;
            return this;
        }

        public Builder product(String val) {
            product = val;
            return this;
        }

        public Builder orderType(String val) {
            orderType = val;
            return this;
        }

        public Builder validity(String val) {
            validity = val;
            return this;
        }

        public Builder disclosedQuantity(Integer val) {
            disclosedQuantity = val;
            return this;
        }

        public Builder triggerPrice(Double val) {
            triggerPrice = val;
            return this;
        }

        public Builder squareoff(Double val) {
            squareoff = val;
            return this;
        }

        public Builder stoploss(Double val) {
            stoploss = val;
            return this;
        }

        public Builder trailingStoploss(Double val) {
            trailingStoploss = val;
            return this;
        }

        public Builder tag(String val) {
            //TODO: tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed
            tag = val;
            return this;
        }

        public Builder parentOrderId(String val) {
            parentOrderId = val;
            return this;
        }

        public CustomOrderParams build() {
            return new CustomOrderParams(this);
        }
    }

    private CustomOrderParams(Builder builder) {
        exchange = builder.exchange;
        tradingsymbol = builder.tradingsymbol;
        transactionType = builder.transactionType;
        quantity = builder.quantity;
        price = builder.price;
        product = builder.product;
        orderType = builder.orderType;
        validity = builder.validity;
        disclosedQuantity = builder.disclosedQuantity;
        triggerPrice = builder.triggerPrice;
        squareoff = builder.squareoff;
        stoploss = builder.stoploss;
        trailingStoploss = builder.trailingStoploss;
        tag = builder.tag;
        parentOrderId = builder.parentOrderId;
    }

    public OrderParams getOrderParams() {
        OrderParams orderParams = new OrderParams();

        orderParams.exchange = this.exchange;
        orderParams.tradingsymbol = this.tradingsymbol;
        orderParams.transactionType = this.transactionType;
        orderParams.quantity = this.quantity;
        orderParams.price = this.price;
        orderParams.product = this.product;
        orderParams.orderType = this.orderType;
        orderParams.validity = this.validity;
        orderParams.disclosedQuantity = this.disclosedQuantity;
        orderParams.triggerPrice = this.triggerPrice;
        orderParams.squareoff = this.squareoff;
        orderParams.stoploss = this.stoploss;
        orderParams.trailingStoploss = this.trailingStoploss;
        orderParams.tag = this.tag;
        orderParams.parentOrderId = this.parentOrderId;

        return orderParams;
    }
}
