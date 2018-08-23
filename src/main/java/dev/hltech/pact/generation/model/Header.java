package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Header {
    String name;
    String value;
}
