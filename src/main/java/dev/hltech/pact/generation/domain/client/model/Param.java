package dev.hltech.pact.generation.domain.client.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Param {

    String name;
    Class<?> type;
    Object defaultValue;
}