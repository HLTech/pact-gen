package dev.hltech.pact.generation.domain.client.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@Builder
public class ResponseProperties {

    private final HttpStatus status;
    private final List<Param> headers;
    private final Body body;
}
