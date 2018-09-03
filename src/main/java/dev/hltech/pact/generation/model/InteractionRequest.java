package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InteractionRequest {

    private final String method;
    private final String path;
    private final List<Header> headers;
    private final String query;
    private final String body;
}
