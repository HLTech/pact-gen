package dev.hltech.pact.generation.domain.client.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientMethodRepresentation {

    private RequestProperties requestProperties;
    private ResponseProperties responseProperties;
}
