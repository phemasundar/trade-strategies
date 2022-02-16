package com.strategies.trade.api_test_beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

/**
 * @author hemasundarpenugonda
 */
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseResponse {
    String error;
    String message;
}
