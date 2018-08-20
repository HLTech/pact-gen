package dev.hltech.pact.generation;

import lombok.Data;

import java.util.List;

@Data
public class Pact {

    private final Provider provider;
    private final Consumer consumer;
    private final List<Interaction> interactions;
    private final Metadata metadata;
}
