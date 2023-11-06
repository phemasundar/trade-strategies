package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;

/**
 * @author hemasundarpenugonda
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BSECandleStick extends CandleStick {
    private static final long serialVersionUID = 1L;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yy");

    private String SC_CODE;
    private String SC_NAME;
    private String SC_GROUP;
    private String SC_TYPE;
    private Double OPEN;
    private Double HIGH;
    private Double LOW;
    private Double CLOSE;
    private Double LAST;
    private Double PREVCLOSE;
    private Long NO_TRADES;
    private Long NO_OF_SHRS;
    private Long NET_TURNOV;
    private String TDCLOINDI;
    private String ISIN_CODE;
    private LocalDate TRADING_DATE;
    private String FILLER2;
    private String FILLER3;

    public BSECandleStick(List<String> item) {
        super(Arrays.asList(item.get(0), item.get(1), item.get(15), item.get(9), item.get(4), item.get(5), item.get(6), item.get(8), item.get(7), item.get(12), item.get(10)));
        try {
            setSC_NAME(item.get(1));
            setSC_GROUP(item.get(2));
            setSC_TYPE(item.get(3));
            setNO_OF_SHRS(item.get(11));
            setTDCLOINDI(item.get(13));
            setISIN_CODE(item.get(14));
            try {
                setFILLER2(item.get(16));
            } catch (ArrayIndexOutOfBoundsException e) {
                setFILLER2("");
            }
            try {
                setFILLER3(item.get(17));
            } catch (ArrayIndexOutOfBoundsException e) {
                setFILLER3("");
            }

        } catch (IndexOutOfBoundsException | DateTimeParseException e) {
            System.out.println(item);
            e.printStackTrace();
        }
    }

    public void setTRADING_DATE(String TRADING_DATE) {
        try {
            this.TRADING_DATE = LocalDate.parse(TRADING_DATE, formatter);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            ;
            throw e;
        }
    }

    public void setOPEN(String OPEN) {
        this.OPEN = Double.valueOf(OPEN.replace(",", ""));
    }

    public void setHIGH(String HIGH) {
        this.HIGH = Double.valueOf(HIGH.replace(",", ""));
    }

    public void setLOW(String LOW) {
        this.LOW = Double.valueOf(LOW.replace(",", ""));
    }

    public void setCLOSE(String CLOSE) {
        this.CLOSE = Double.valueOf(CLOSE.replace(",", ""));
    }

    public void setLAST(String LAST) {
        this.LAST = Double.valueOf(LAST.replace(",", ""));
    }

    public void setPREVCLOSE(String PREVCLOSE) {
        this.PREVCLOSE = Double.valueOf(PREVCLOSE.replace(",", ""));
    }

    public void setNO_TRADES(String NO_TRADES) {
        this.NO_TRADES = Long.valueOf(NO_TRADES.replace(",", "").trim());
    }

    public void setNO_OF_SHRS(String NO_OF_SHRS) {
        this.NO_OF_SHRS = Long.valueOf(NO_OF_SHRS.replace(",", "").trim());
    }

    public void setNET_TURNOV(String NET_TURNOV) {
        this.NET_TURNOV = Long.valueOf(NET_TURNOV.replace(",", "").trim());
    }

}
