package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

@Data
@Builder
public class RequestProperties {

    private final HttpMethod httpMethod;
    private final String path;
}
