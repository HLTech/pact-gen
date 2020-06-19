package com.hltech.pact.gen.domain.client.model;

import com.google.common.collect.Lists;
import com.hltech.pact.gen.domain.client.util.TypeExtractor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Method;
import java.util.List;

@Data
@Builder
public class ResponseRepresentation {

    private final HttpStatus status;
    private final List<Param> headers;
    private final Body body;
    private final String description;
    private final boolean emptyBodyExpected;

    public static ResponseRepresentation getDefaultForMethod(Method method) {
        return ResponseRepresentation.from(HttpStatus.OK,
            Lists.newArrayList(),
            method,
            "",
            false);
    }

    public static ResponseRepresentation from(HttpStatus status,
                                                          List<Param> headers,
                                                          Method method,
                                                          String description,
                                                          boolean isEmptyBodyExpected) {

        return ResponseRepresentation.builder()
            .status(status)
            .headers(headers)
            .body(Body.builder()
                .type(method.getReturnType())
                .genericArgumentTypes(
                    TypeExtractor.extractParameterTypesFromType(method.getGenericReturnType()))
                .build())
            .description(description)
            .emptyBodyExpected(isEmptyBodyExpected)
            .build();
    }
}
