package dev.hltech.pact.generation.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestType {

    private final String requestFoo;
    private final String requestBar;
}
