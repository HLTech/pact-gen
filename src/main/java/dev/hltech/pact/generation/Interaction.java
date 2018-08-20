package dev.hltech.pact.generation;

import lombok.Data;

@Data
class Interaction {

    private final String description;
    private final InteractionRequest request;
    private final InteractionResponse response;
}
