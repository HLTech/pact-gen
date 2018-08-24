package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class InteractionResponse {

    private String status;
    private final List<Header> headers;
}
