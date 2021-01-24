package com.hltech.pact.gen.domain.client.jaxrs

import spock.lang.Specification
import spock.lang.Subject

class JaxRsClientsFinderUT extends Specification {

    @Subject
    private JaxRsClientsFinder finder = new JaxRsClientsFinder()

    def "should find jax rs client"() {
        when:
            Set<Class<?>> jaxRsClients = finder.findJaxRsClients('com.hltech.pact.gen.domain.client.jaxrs.sample')

        then:
            jaxRsClients.size() == 10
            verifyJaxRsClient(jaxRsClients, 'PathJaxRsClassAnnotatedClient')
            verifyJaxRsClient(jaxRsClients, 'PathJaxRsMethodAnnotatedClient')
            verifyJaxRsClient(jaxRsClients, 'RequestBodyJaxRsClient')
            verifyJaxRsClient(jaxRsClients, 'RequestHeaderParamJaxRsClient')
            verifyJaxRsClient(jaxRsClients, 'RequestPathParamJaxRsClient')
            verifyJaxRsClient(jaxRsClients, 'RequestQueryParamJaxRsClient')
            verifyJaxRsClient(jaxRsClients, 'RequestFormParamJaxRsClient')
            verifyJaxRsClient(jaxRsClients, 'RequestMatrixParamJaxRsClient')
            verifyJaxRsClient(jaxRsClients, 'GETAnnotatedClassClient')
            verifyJaxRsClient(jaxRsClients, 'GETAnnotatedMethodClient')
    }

    def "should not find feign clients"() {
        when:
            Set<Class<?>> feignClients = finder.findJaxRsClients('some.not.existing.package')

        then:
            feignClients.size() == 0
    }

    private static boolean verifyJaxRsClient(Set<Class<?>> jaxRsClients, String name) {
        jaxRsClients.any { client ->
            client.simpleName == name
        }
    }
}
