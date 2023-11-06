package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class VacsineResponse {

    private List<Centers> centers;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Centers {
        private String center_id;
        private String name;
        private String address;
        private String state_name;
        private String district_name;
        private String block_name;
        private String pincode;
        private String lat;
        @JsonProperty("long")
        private String longProp;
        private String from;
        private String to;
        private String fee_type;
        private List<Sessions> sessions;


    }

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sessions {
        private String session_id;
        private String date;
        private Integer available_capacity;
        private Integer min_age_limit;
        private String vaccine;
        private List<String> slots;
        private Integer available_capacity_dose1;
        private Integer available_capacity_dose2;
    }

}
