package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Pact {

    private final Service provider;
    private final Service consumer;
    private final List<Interaction> interactions;
    private final Metadata metadata;
}
