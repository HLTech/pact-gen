package dev.hltech.pact.generation.domain.pact.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class InteractionRequest {

    private final String method;
    private final String path;
    private final Map<String, String> headers;
    private final String query;
    private final JsonNode body;
}
