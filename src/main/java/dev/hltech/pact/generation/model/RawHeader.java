package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RawHeader {
    String name;
    Object value;
}
