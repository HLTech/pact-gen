package dev.hltech.pact.generation.domain.client.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ClientMethodRepresentation {

    private RequestRepresentation requestRepresentation;
    private List<ResponseRepresentation> responseRepresentationList;
}
