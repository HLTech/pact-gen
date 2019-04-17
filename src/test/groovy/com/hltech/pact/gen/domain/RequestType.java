package com.hltech.pact.gen.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestType {

    private final String requestFoo;
    private final String requestBar;
    private final TestParam[] testParams;
}
