package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class IndicesList implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("Broad Market Indices")
    private List<String> broadMarketIndices;
    @JsonProperty("Sectoral Indices")
    private List<String> sectoralIndices;
    @JsonProperty("Thematic Indices")
    private List<String> thematicIndices;
    @JsonProperty("Strategy Indices")
    private List<String> strategyIndices;
    @JsonProperty("Others")
    private List<String> others;

    //Utility methods
    public List<String> getAllIndices() {
        ArrayList<String> allList = new ArrayList<>();
        allList.addAll(this.getBroadMarketIndices());
        allList.addAll(this.getSectoralIndices());
        allList.addAll(this.getThematicIndices());
        allList.addAll(this.getStrategyIndices());
//        allList.addAll(this.getOthers());
        return allList;
    }

}

