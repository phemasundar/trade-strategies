package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class BseIndexResponse implements Serializable {

    private static final long serialVersionUID = 1L;
//    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");

    @JsonProperty("Table")
    private List<IndTable> Table;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    @NoArgsConstructor
    public static class IndTable implements Serializable {
        private static final long serialVersionUID = 1L;

        @JsonProperty("FileDTTM")
        private String FileDTTM;
        private String scrip_cd;
        private String scripname;
        private String scrip_grp;
        private String openrate;
        private String highrate;
        private String lowrate;
        private String ltradert;
        private String prevdayclose;
        private String change_val;
        private String change_percent;
        private String index_code;
        private String trd_val;
        private String trd_vol;
        private String nooftrd;
        private String trend;
        private String dt_tm;
        @JsonProperty("Ishighflag")
        private String Ishighflag;
        @JsonProperty("IsLowflag")
        private String IsLowflag;
        @JsonProperty("URL")
        private String URL;
        @JsonProperty("NSUrl")
        private String NSUrl;
        @JsonProperty("PRIORITY_FLAG")
        private String PRIORITY_FLAG;
        @JsonProperty("RN")
        private String RN;
    }

    public List<String> getALlScripNames() {
        return this.getTable().stream().map(item -> item.getScrip_cd()).collect(Collectors.toList());
    }
}

