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
            jaxRsClients.size() == 2
            verifyJaxRsClient(jaxRsClients, 'ClassAnnotatedClient')
            verifyJaxRsClient(jaxRsClients, 'MethodAnnotatedClient')
    }

    private static boolean verifyJaxRsClient(Set<Class<?>> jaxRsClients, String name) {
        jaxRsClients.any { client ->
            client.simpleName == name
        }
    }
}
