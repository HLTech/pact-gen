package dev.hltech.pact.generation.domain.pact

import dev.hltech.pact.generation.domain.GenericResponseType
import dev.hltech.pact.generation.domain.ResponseType
import dev.hltech.pact.generation.domain.client.model.Body
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation
import dev.hltech.pact.generation.domain.client.model.RequestProperties
import dev.hltech.pact.generation.domain.client.model.ResponseProperties
import spock.lang.Specification

import java.time.LocalDateTime

class PojoExtractorSpec extends Specification {

    def "should extract pojos from client mtehod representation"() {
        given:
            final ClientMethodRepresentation methodRepresentation = sampleMethodRepresentation()

        when:
            def result = PojoExtractor.extractPojoTypes(methodRepresentation)

        then:
            result.size() == 6
            result.contains(GenericResponseType)
            result.contains(ResponseType)
            result.contains(SampleClassWithNestedOnes)
            result.contains(NestedClassA)
            result.contains(NestedClassB)
            result.contains(NestedClassC)
    }

    private ClientMethodRepresentation sampleMethodRepresentation() {
        ClientMethodRepresentation.builder()
            .requestProperties(
                RequestProperties.builder()
                    .body(
                        Body.builder()
                            .bodyType(SampleClassWithNestedOnes)
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

    class SampleClassWithNestedOnes {

        private NestedClassA foo
        private NestedClassB bar
        private String fooBar
    }

    class NestedClassA {

        private LocalDateTime someTime
        private NestedEnum anEnum
    }

    class NestedClassB {

        private NestedClassC foo
        private int someNumber
        private long anotherNumber
    }

    class NestedClassC {

        private String foo
    }

    enum NestedEnum {
        A,
        B
    }
}
