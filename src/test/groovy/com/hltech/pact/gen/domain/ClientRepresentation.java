package com.hltech.pact.gen.domain;

import lombok.Data;

import java.util.List;

@Data
public class ClientRepresentation {
    private final List<Interaction> interactions;
}
