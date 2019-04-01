package com.hltech.pact.gen.domain

import spock.lang.Specification

class ClientRepresentationGeneratorSpec extends Specification {

    def "Should get interactions"() {
        given:


        when:
            def clientRepresentation = RestInspector.inspectRestClient(JaxRsClient)

        then:
            clientRepresentation
            clientRepresentation.interactions
            clientRepresentation.interactions.size() == 3
    }

    def "Should verify http method"() {
        given:


        when:
            def clientRepresentation = RestInspector.inspectRestClient(JaxRsClient)

        then:
            clientRepresentation.interactions[0].httpMethod == 'GET'

    }

    def "Should verify path"() {
        given:


        when:
            def clientRepresentation = RestInspector.inspectRestClient(JaxRsClient)

        then:
            clientRepresentation.interactions[0].path == '/base/resource'

    }

    def "Should verify another path"() {
        given:


        when:
        def clientRepresentation = RestInspector.inspectRestClient(AnotherJaxRsClient)

        then:
        clientRepresentation.interactions[0].path == '/resource'
    }

    def "Should verify query params"() {
        given:


        when:
        def clientRepresentation = RestInspector.inspectRestClient(JaxRsClient)

        then:
        clientRepresentation.interactions[0].queryParams == ['name': String, 'age': int]
    }

    def "Should verify path params"() {
        given:


        when:
        def clientRepresentation = RestInspector.inspectRestClient(JaxRsClient)

        then:
        clientRepresentation.interactions[1].pathParams == ['pathParam': Double]
    }

    def "Should verify http method put"() {
        given:


        when:
        def clientRepresentation = RestInspector.inspectRestClient(JaxRsClient)

        then:
        clientRepresentation.interactions[2].httpMethod == 'PUT'
    }

    def "Should verify body"() {
        given:


        when:
        def clientRepresentation = RestInspector.inspectRestClient(JaxRsClient)

        then:
        clientRepresentation.interactions[2].body == SampleDto
    }
}
