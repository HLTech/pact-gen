package dev.hltech.pact.generation.domain.pact

import dev.hltech.pact.generation.domain.GenericResponseType
import dev.hltech.pact.generation.domain.ResponseType
import dev.hltech.pact.generation.domain.client.model.Body
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation
import dev.hltech.pact.generation.domain.client.model.RequestRepresentation
import dev.hltech.pact.generation.domain.client.model.ResponseRepresentation
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
            .requestRepresentation(
                RequestRepresentation.builder()
                    .body(
                        Body.builder()
                            .type(SampleClassWithNestedOnes)
                            .genericArgumentTypes([GenericResponseType])
                            .build())
                    .build()
            )
            .responseRepresentationList([
            ResponseRepresentation.builder()
                        .body(
                            Body.builder()
                                .type(void)
                                .genericArgumentTypes([])
                                .build())
                        .build(),
            ResponseRepresentation.builder()
                        .body(Body.builder()
                                .type(ResponseType)
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
