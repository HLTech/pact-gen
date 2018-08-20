package dev.hltech.pact.generation;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
class InteractionRequest {

    private final String method;
    private final String path;
}
