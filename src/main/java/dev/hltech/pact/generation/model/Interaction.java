package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Interaction {

    private final String description;
    private final InteractionRequest request;
    private final InteractionResponse response;
}
