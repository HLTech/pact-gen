package dev.hltech.pact.generation.domain.client.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
@Builder
public class RequestProperties {

    private final HttpMethod httpMethod;
    private final String path;
    private final List<Header> headers;
    private final Class<?> bodyType;
    private final List<Param> parameters;
}
