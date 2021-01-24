package com.hltech.pact.gen.domain.client.jaxrs

import com.hltech.pact.gen.domain.client.jaxrs.sample.RequestBodyJaxRsClient
import com.hltech.pact.gen.domain.client.jaxrs.sample.RequestFormParamJaxRsClient
import com.hltech.pact.gen.domain.client.jaxrs.sample.RequestHeaderParamJaxRsClient
import com.hltech.pact.gen.domain.client.jaxrs.sample.RequestMatrixParamJaxRsClient
import com.hltech.pact.gen.domain.client.jaxrs.sample.RequestPathParamJaxRsClient
import com.hltech.pact.gen.domain.client.jaxrs.sample.RequestQueryParamJaxRsClient
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation
import com.hltech.pact.gen.domain.client.model.RequestRepresentation
import com.hltech.pact.gen.domain.client.model.ResponseRepresentation
import com.hltech.pact.gen.domain.pact.PactFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import spock.lang.PendingFeature
import spock.lang.Specification
import spock.lang.Subject

import javax.ws.rs.core.HttpHeaders
import java.lang.reflect.Method

class JaxRsMethodRepresentationExtractorUT extends Specification {

    @Subject
    JaxRsMethodRepresentationExtractor extractor = new JaxRsMethodRepresentationExtractor(PactFactory.annotatedMethodHandlers)

    @PendingFeature
    //TODO turned off - unfinished
    def "Should correctly extract jaxrs method representation - request with body"() {
        given:
            Method method = RequestBodyJaxRsClient.getMethod('httpGetWithBody', String.class)

        when:
            ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
            assert representation.getRequestRepresentation().getHttpMethod() == HttpMethod.GET
            assert representation.getRequestRepresentation().getPath() == '/testBody'
            assert representation.getRequestRepresentation().getBody().getType() == String.class
    }

    @PendingFeature
    //TODO turned off - unfinished
    def "Should correctly extract jaxrs method representation - request with headers by @HeaderParam"() {
        given:
            Method method = RequestHeaderParamJaxRsClient.getMethod('httpGetWithHeadersByParam', String.class, String.class)

        when:
            ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
            assert representation.getRequestRepresentation().getHttpMethod() == HttpMethod.GET
            assert representation.getRequestRepresentation().getPath() == '/testHeadersByParam'
            assert representation.getRequestRepresentation().getHeaders().get(0).name == 'Header-A'
            assert representation.getRequestRepresentation().getHeaders().get(1).name == 'Header-B'
    }

    @PendingFeature
    //TODO not implemented yet
    def "Should correctly extract jaxrs method representation - request with headers by @Context"() {
        given:
            Method method = RequestHeaderParamJaxRsClient.getMethod('httpGetWithHeadersByContext', HttpHeaders.class)

        when:
            ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
            assert representation.getRequestRepresentation().getHttpMethod() == HttpMethod.GET
            assert representation.getRequestRepresentation().getPath() == '/testHeadersByContext'
            assert representation.getRequestRepresentation().getHeaders().get(0).name == 'Header-A'
    }

    @PendingFeature
    //TODO turned off - unfinished
    def "Should correctly extract jaxrs method representation - request with query parameters"() {
        given:
            Method method = RequestQueryParamJaxRsClient.getMethod('httpGetWithQueryParameters', String.class, Integer.class)

        when:
            ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
            assert representation.getRequestRepresentation().getHttpMethod() == HttpMethod.GET
            assert representation.getRequestRepresentation().getPath() == '/testWithQueryParameters'
            assert representation.getRequestRepresentation().getRequestParameters().get(0).name == 'Param-Q'
            assert representation.getRequestRepresentation().getRequestParameters().get(0).type == String.class
            assert representation.getRequestRepresentation().getRequestParameters().get(1).name == 'Param-W'
            assert representation.getRequestRepresentation().getRequestParameters().get(1).type == Integer.class
    }

    @PendingFeature
    //TODO not implemented yet
    def "Should correctly extract jaxrs method representation - request with matrix parameters"() {
        given:
            Method method = RequestMatrixParamJaxRsClient.getMethod('httpGetWithMatrixParameters', String.class, String.class)

        when:
            ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
            assert representation.getRequestRepresentation().getHttpMethod() == HttpMethod.GET
            assert representation.getRequestRepresentation().getPath() == '/testWithMatrixParameters'
            assert representation.getRequestRepresentation().getPathParameters().get(0).type == String.class
            assert representation.getRequestRepresentation().getPathParameters().get(0).name == 'matrix-a'
            assert representation.getRequestRepresentation().getPathParameters().get(1).type == String.class
            assert representation.getRequestRepresentation().getPathParameters().get(1).name == 'matrix-b'

    }

    @PendingFeature
    //TODO not implemented yet
    def "Should correctly extract jaxrs method representation - request with form parameters"() {
        given:
            Method method = RequestFormParamJaxRsClient.getMethod('httpGetWithFromParameters', String.class, Integer.class)

        when:
            ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
            assert representation.getRequestRepresentation().getHttpMethod() == HttpMethod.GET
            assert representation.getRequestRepresentation().getPath() == '/testWithFormParameters'
            assert representation.getRequestRepresentation().getPathParameters().get(0).type == String.class
            assert representation.getRequestRepresentation().getPathParameters().get(0).name == 'form-a'
            assert representation.getRequestRepresentation().getPathParameters().get(1).type == Integer.class
            assert representation.getRequestRepresentation().getPathParameters().get(1).name == 'form-b'

    }

    @PendingFeature
    //TODO turned off - unfinished
    def "Should correctly extract jaxrs method representation - request path parameters"() {
        given:
            Method method = RequestPathParamJaxRsClient.getMethod('httpGetWithPathParameters', String.class)

        when:
            ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
            assert representation.getRequestRepresentation().getHttpMethod() == HttpMethod.GET
            assert representation.getRequestRepresentation().getPath() == '/testWithPathParameters/{pathParamK}'
            assert representation.getRequestRepresentation().getPathParameters().get(0).name == 'pathParamK'
            assert representation.getRequestRepresentation().getPathParameters().get(0).type == String.class
    }

    //TODO remove @PendingFeature after handler for jaxrs GET implemented
    @PendingFeature
    def "Should correctly extract jaxrs method representation"() {
        given:
        Method method = GETAnnotatedClassClient.getMethod('httpGetResponseAccepted',
            String.class,
            String.class,
            String.class,
            Integer.class,
            String.class,
            String.class,
        )

        when:
        ClientMethodRepresentation representation = extractor.extractClientMethodRepresentation(method)

        then:
        verifyRequestRepresentation(representation.getRequestRepresentation())

        and:
        representation.getResponseRepresentationList().size() == 1
        verifyResponseRepresentation(representation.getResponseRepresentationList().get(0))
    }

    def verifyRequestRepresentation(RequestRepresentation representation) {
        assert representation.getHttpMethod() == HttpMethod.GET
        assert representation.getPath() == '/testPathGet/{pathParamK}'
        assert representation.getHeaders().get(0).name == 'Header-A'
        assert representation.getHeaders().get(1).name == 'Header-B'
        assert representation.getBody().getType() == String.class
        assert representation.getRequestParameters().get(0).name == 'Param-Q'
        assert representation.getRequestParameters().get(0).type == String.class
        assert representation.getRequestParameters().get(1).name == 'Param-W'
        assert representation.getRequestParameters().get(1).type == Integer.class
        assert representation.getPathParameters().get(0).name == 'pathParamK'
        assert representation.getPathParameters().get(0).type == String.class
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
        assert representation.getBody().type == GETAnnotatedClassClient.ExampleDto.class
        assert representation.getBody().genericArgumentTypes.size() == 0
        assert !representation.getDescription()
        true
    }
}
