package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class BseIndicesList implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("indxnm")
    private String indexName;
    @JsonProperty("code")
    private String code;
    @JsonProperty("ltp")
    private String ltp;
    @JsonProperty("chg")
    private String chg;
    @JsonProperty("perchg")
    private String perchg;
    @JsonProperty("pgord")
    private String pgord;
    @JsonProperty("PrevClose")
    private String PrevClose;
    @JsonProperty("pgcnt")
    private String pgcnt;

}

