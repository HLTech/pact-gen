package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RawParam {
    String name;
    Object value;
}
