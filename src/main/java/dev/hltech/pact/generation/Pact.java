package dev.hltech.pact.generation;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Pact {

    private final Provider provider;
    private final Consumer consumer;
    private final List<Interaction> interactions;
    private final Metadata metadata;
}
