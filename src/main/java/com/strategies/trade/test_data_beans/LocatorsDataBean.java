package com.strategies.trade.test_data_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.strategies.trade.utilities.CommonEnums;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author hemasundarpenugonda
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class LocatorsDataBean {

    private List<indLocator> locators;

    @Getter
    @Setter
    @JsonIgnoreProperties(ignoreUnknown = true)
    @AllArgsConstructor
    public static class indLocator {
        private String pageName;
        private String locatorName;
        private CommonEnums.Locators locatorType;
        private String locatorValue;

        public indLocator() {

        }
    }
}
