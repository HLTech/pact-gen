package com.hltech.pact.gen.domain;

import lombok.Data;

import java.util.Map;

@Data
public class Interaction {
    private final String httpMethod;
    private final String path;
    private final Map<String, Class<?>> queryParams;
    private final Map<String, Class<?>> pathParams;
    private final Class<?> body;
}
