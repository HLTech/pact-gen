package com.hltech.pact.gen.domain.client.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Param {

    private final String name;
    private final Class<?> type;
    private final Class<?> genericArgumentType;
    private final Object defaultValue;
}
