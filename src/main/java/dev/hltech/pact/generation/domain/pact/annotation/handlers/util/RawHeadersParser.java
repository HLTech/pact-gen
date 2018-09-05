package dev.hltech.pact.generation.domain.pact.annotation.handlers.util;

import dev.hltech.pact.generation.domain.client.model.Param;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class RawHeadersParser {

    private RawHeadersParser() {}

    public static List<Param> parseHeaders(String[] stringHeaderArray) {
        return Arrays.stream(stringHeaderArray)
            .map(stringHeader -> stringHeader.split("="))
            .map(RawHeadersParser::parseHeader)
            .collect(Collectors.toList());
    }

    private static Param parseHeader(String[] stringHeaderArray) {
        return Param.builder()
            .name(stringHeaderArray[0])
            .defaultValue(stringHeaderArray[1])
            .build();
    }
}
