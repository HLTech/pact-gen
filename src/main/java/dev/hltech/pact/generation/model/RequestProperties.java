package dev.hltech.pact.generation.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.List;

@Data
@Builder
public class RequestProperties {

    private final HttpMethod httpMethod;
    private final String path;
    private final String[] requestMappingHeaders;
    private final List<RawHeader> requestHeaderHeaders;
    private final Class<?> bodyType;
    private final List<RawParam> requestParameters;
}
