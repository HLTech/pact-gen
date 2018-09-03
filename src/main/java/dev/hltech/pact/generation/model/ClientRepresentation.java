package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientRepresentation {

    private RequestProperties requestProperties;
    private ResponseProperties responseProperties;
}
