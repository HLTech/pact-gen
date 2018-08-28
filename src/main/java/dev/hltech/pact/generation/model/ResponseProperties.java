package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@Builder
public class ResponseProperties {

    private final HttpStatus status;
    private final String[] headers;
    private final Class<?> returnType;
}
