package com.hltech.pact.gen.domain.client.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ResponseRepresentation {

    private final HttpStatus status;
    private final List<Param> headers;
    private final Body body;
    private final String description;
    private final boolean emptyBodyExpected;
}
