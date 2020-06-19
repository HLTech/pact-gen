package com.hltech.pact.gen.domain.client.feign

import com.hltech.pact.gen.domain.client.feign.sample.ResponseHeadersFeignClient
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation
import com.hltech.pact.gen.domain.client.model.RequestRepresentation
import com.hltech.pact.gen.domain.client.model.ResponseRepresentation
import com.hltech.pact.gen.domain.pact.PactFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Subject

import java.lang.reflect.Method

class FeignMethodRepresentationExtractorUT extends Specification {

    @Subject
    FeignMethodRepresentationExtractor extractor = new FeignMethodRepresentationExtractor(PactFactory.annotatedMethodHandlers)

    def "Should correctly extract feign method representation"() {
        given:
            Method method = ResponseHeadersFeignClient.getMethod('getTestObject', null)

        when:
            ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
            verifyRequestRepresentation(representation.getRequestRepresentation())

            representation.getResponseRepresentationList().size() == 1
            verifyResponseRepresentation(representation.getResponseRepresentationList().get(0))
    }

    def verifyRequestRepresentation(RequestRepresentation representation) {
        assert representation.getHttpMethod() == HttpMethod.GET
        assert representation.getPath() == '/'
        assert representation.getHeaders().isEmpty()
        assert !representation.getBody().getType()
        assert !representation.getBody().getGenericArgumentTypes()
        assert !representation.getRequestParameters()
        assert !representation.getPathParameters()
        true
    }

    def verifyResponseRepresentation(ResponseRepresentation representation) {
        assert representation.getStatus() == HttpStatus.OK
        assert representation.getHeaders().any { param ->
            param.name == 'key1'
            !param.type
            !param.genericArgumentType
            param.defaultValue == 'val1'
        }
        assert representation.getHeaders().any { param ->
            param.name == 'key2'
            !param.type
            !param.genericArgumentType
            param.defaultValue == 'val2'
        }
        assert representation.getBody().type == Void.TYPE
        assert representation.getBody().genericArgumentTypes.size() == 0
        assert !representation.getDescription()
        true
    }
}
