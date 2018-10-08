package dev.hltech.pact.generation.domain.client.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Body {
    private final Class<?> type;
    private final List<Class<?>> genericArgumentTypes;
}
