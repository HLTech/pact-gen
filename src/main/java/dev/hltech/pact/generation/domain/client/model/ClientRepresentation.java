package dev.hltech.pact.generation.domain.client.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ClientRepresentation {

    private RequestProperties requestProperties;
    private ResponseProperties responseProperties;
}
