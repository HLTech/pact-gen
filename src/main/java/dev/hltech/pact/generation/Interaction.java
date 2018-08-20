package dev.hltech.pact.generation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class Interaction {

    private final String description;
    private final InteractionRequest request;
}
