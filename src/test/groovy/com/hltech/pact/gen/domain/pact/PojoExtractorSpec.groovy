package com.hltech.pact.gen.domain.pact


import com.hltech.pact.gen.domain.GenericResponseType
import com.hltech.pact.gen.domain.ResponseType
import com.hltech.pact.gen.domain.client.model.Body
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation
import com.hltech.pact.gen.domain.client.model.RequestRepresentation
import com.hltech.pact.gen.domain.client.model.ResponseRepresentation
import spock.lang.Specification

import java.time.LocalDateTime

class PojoExtractorSpec extends Specification {

    def "should extract pojos from client method representation"() {
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
        private NestedClassB[] bars
        private String fooBar
        private String[] fooBars
        private byte[] barFoos
    }

    class NestedClassA {

        private LocalDateTime someTime
        private NestedEnum anEnum
    }

    class NestedClassB {

        private NestedClassC[] foos
        private int[] someNumbers
        private long anotherNumber
    }

    class NestedClassC {

        private String foos
    }

    enum NestedEnum {
        A,
        B
    }
}
