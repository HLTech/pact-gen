package com.hltech.pact.gen.domain.pact.model;

import com.hltech.pact.gen.domain.pact.Service;
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
