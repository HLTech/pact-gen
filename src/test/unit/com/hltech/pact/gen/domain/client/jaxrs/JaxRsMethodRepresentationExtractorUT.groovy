package com.hltech.pact.gen.domain.client.jaxrs

import com.hltech.pact.gen.domain.client.jaxrs.sample.GETAnnotatedClassClient
import com.hltech.pact.gen.domain.client.model.ClientMethodRepresentation
import com.hltech.pact.gen.domain.client.model.RequestRepresentation
import com.hltech.pact.gen.domain.pact.PactFactory
import org.springframework.http.HttpMethod
import spock.lang.Specification
import spock.lang.Subject

import java.lang.reflect.Method

class JaxRsMethodRepresentationExtractorUT extends Specification {

    @Subject
    JaxRsMethodRepresentationExtractor extractor = new JaxRsMethodRepresentationExtractor(PactFactory.annotatedMethodHandlers)

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
            //TODO add verification for response representation list
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
}
