package dev.hltech.pact.generation.domain.pact

import dev.hltech.pact.generation.domain.GenericResponseType
import dev.hltech.pact.generation.domain.RequestType
import dev.hltech.pact.generation.domain.ResponseType
import dev.hltech.pact.generation.domain.TestParam
import dev.hltech.pact.generation.domain.client.model.Body
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation
import dev.hltech.pact.generation.domain.client.model.RequestProperties
import dev.hltech.pact.generation.domain.client.model.ResponseProperties
import spock.lang.Specification

class PojoExtractorSpec extends Specification {

    def "should extract pojos from client mtehod representation"() {
        given:
            final ClientMethodRepresentation methodRepresentation = sampleMethodRepresentation()

        when:
            def result = PojoExtractor.extractPojoTypes(methodRepresentation)

        then:
            result.size() == 3
            result.contains(RequestType)
            result.contains(GenericResponseType)
            result.contains(ResponseType)
    }

    private ClientMethodRepresentation sampleMethodRepresentation() {
        ClientMethodRepresentation.builder()
            .requestProperties(
                RequestProperties.builder()
                    .body(
                        Body.builder()
                            .bodyType(RequestType)
                            .genericArgumentTypes([GenericResponseType])
                            .build())
                    .build()
            )
            .responsePropertiesList([
                    ResponseProperties.builder()
                        .body(
                            Body.builder()
                                .bodyType(void)
                                .genericArgumentTypes([])
                                .build())
                        .build(),
                    ResponseProperties.builder()
                        .body(Body.builder()
                                .bodyType(ResponseType)
                                .genericArgumentTypes([GenericResponseType])
                                .build())
                        .build()
            ])
            .build()
    }
}
