package com.hltech.pact.gen.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RequestType {

    private final String requestFoo;
    private final String requestBar;
    private final TestParam[] testParams;
    private final BigDecimal monetaryValue;
}
