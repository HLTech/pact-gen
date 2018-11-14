package com.hltech.pact.gen.domain.client.feign

import com.hltech.pact.gen.domain.client.feign.sample.ResponseHeadersFeignClient
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation
import com.hltech.pact.gen.domain.client.model.RequestRepresentation
import com.hltech.pact.gen.domain.client.model.ResponseRepresentation
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.Specification
import spock.lang.Subject

import java.lang.reflect.Method

class FeignMethodRepresentationExtractorSpec extends Specification {

    @Subject
    FeignMethodRepresentationExtractor extractor = new FeignMethodRepresentationExtractor()

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
        representation.getHttpMethod() == HttpMethod.GET
        representation.getPath() == '/'
        representation.getHeaders().isEmpty()
        !representation.getBody().getType()
        !representation.getBody().getGenericArgumentTypes()
        !representation.getRequestParameters()
        !representation.getPathParameters()
    }

    def verifyResponseRepresentation(ResponseRepresentation representation) {
        representation.getStatus() == HttpStatus.OK
        representation.getHeaders().any { param ->
            param.name == 'key1'
            !param.type
            !param.genericArgumentType
            param.defaultValue == 'val1'
        }
        representation.getHeaders().any { param ->
            param.name == 'key2'
            !param.type
            !param.genericArgumentType
            param.defaultValue == 'val2'
        }
        representation.getBody().type == Void.TYPE
        representation.getBody().genericArgumentTypes.size() == 0
        !representation.getDescription()
    }
}
