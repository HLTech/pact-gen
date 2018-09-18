package dev.hltech.pact.generation.domain.pact.model;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InteractionResponse {

    private final String status;
    private final List<Header> headers;
    private final JsonNode body;
}
