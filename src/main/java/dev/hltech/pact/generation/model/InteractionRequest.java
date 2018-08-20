package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InteractionRequest {

    private final String method;
    private final String path;
}
