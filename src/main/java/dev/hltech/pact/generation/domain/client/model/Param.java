package dev.hltech.pact.generation.domain.client.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class Param {

    private final String name;
    private final Class<?> type;
    private final List<Class<?>> genericArgumentTypes;
    private final Object defaultValue;
}
