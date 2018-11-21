package com.hltech.pact.gen.domain.pact

import com.fasterxml.jackson.databind.ObjectMapper
import com.hltech.pact.gen.domain.client.feign.sample.AdditionalNotAnnotatedMethodsFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.BrokenNestedRequestFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.BrokenRequestFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.BrokenResponseFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.DescriptionFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.OptionalResponseFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.PathFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.RequestBodyFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.RequestHeadersFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.RequestParamFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.RequestTypeFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.ResponseBodyFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.ResponseHeadersFeignClient
import com.hltech.pact.gen.domain.client.feign.sample.ResponseInfoFeignClient
import com.hltech.pact.gen.domain.pact.model.Interaction
import com.hltech.pact.gen.domain.pact.model.Pact
import org.apache.commons.lang.StringUtils
import spock.lang.Specification
import spock.lang.Subject

class PactFactorySpec extends Specification {

    @Subject
    private PactFactory pactFactory = new PactFactory()

    final ObjectMapper objectMapper = new ObjectMapper()

    def "should get http method from feign client"() {
        when:
            final Pact pact = pactFactory.createFromFeignClient(RequestTypeFeignClient, 'SpecConsumer', objectMapper)

        then:
            with(pact) {
                consumer.name == 'SpecConsumer'
                provider.name == 'SpecProvider'
                interactions.size() == 8
                verifyHTTPMethod(interactions, 'DELETE')
                verifyHTTPMethod(interactions, 'GET')
                verifyHTTPMethod(interactions, 'HEAD')
                verifyHTTPMethod(interactions, 'OPTIONS')
                verifyHTTPMethod(interactions, 'PATCH')
                verifyHTTPMethod(interactions, 'POST')
                verifyHTTPMethod(interactions, 'PUT')
                verifyHTTPMethod(interactions, 'TRACE')
            }
    }

    def "should get description from feign client"() {
        when:
        final Pact pact = pactFactory.createFromFeignClient(DescriptionFeignClient, 'SpecConsumer', objectMapper)

        then:
        with(pact) {
            consumer.name == 'SpecConsumer'
            provider.name == 'SpecProvider'
            interactions.size() == 2
            verifyDescription(interactions, 'getTestObject request; 200 response')
            verifyDescription(interactions, 'Update test object in the test service')
        }
    }

    def "should get path from feign client"() {
        when:
        Pact pact = pactFactory.createFromFeignClient(PathFeignClient, 'SpecConsumer', objectMapper)

        then:
        with(pact) {
            consumer.name == 'SpecConsumer'
            provider.name == 'SpecProvider'
            interactions.size() == 1
            verifyMultiplePathVariables(interactions[0])
        }
    }

    def "should get request headers from feign client"() {
        when:
        final Pact pact = pactFactory.createFromFeignClient(RequestHeadersFeignClient, 'SpecConsumer', objectMapper)

        then:
        with(pact) {
            consumer.name == 'SpecConsumer'
            provider.name == 'SpecProvider'
            interactions.size() == 1
            interactions[0].request.headers.containsKey('key1')
            interactions[0].request.headers.get('key1') == 'val1'
            interactions[0].request.headers.containsKey('key2')
            interactions[0].request.headers.get('key2') == 'val2'
            interactions[0].request.headers.containsKey('key3')
            !interactions[0].request.headers.get('key3').isEmpty()
            interactions[0].request.headers.containsKey('key4')
            !interactions[0].request.headers.get('key4').isEmpty()
            interactions[0].request.headers.containsKey('key5')
            !interactions[0].request.headers.get('key5').isEmpty()
        }
    }

    def "should get response from optional return value from feign client"() {
        when:
            final Pact pact = pactFactory.createFromFeignClient(OptionalResponseFeignClient, 'SpecConsumer', objectMapper)

        then:
            with(pact) {
                consumer.name == 'SpecConsumer'
                provider.name == 'SpecProvider'
                interactions.size() == 1
                interactions[0].response.body =~ /[a-z A-Z0-9_]+/
            }
    }

    def "should ignore methods, which do not represent interactions, in a feign client"() {
        when:
            final Pact pact = pactFactory.createFromFeignClient(AdditionalNotAnnotatedMethodsFeignClient, 'SpecConsumer', objectMapper)

        then:
            with(pact) {
                consumer.name == 'SpecConsumer'
                provider.name == 'SpecProvider'
                interactions.size() == 1
            }
    }

    def "should get response info from feign client"() {
        when:
        final Pact pact = pactFactory.createFromFeignClient(ResponseInfoFeignClient, 'SpecConsumer', objectMapper)

        then:
        with(pact) {
            consumer.name == 'SpecConsumer'
            provider.name == 'SpecProvider'
            interactions.size() == 3
            verifyHTTPStatus(interactions, '200')
            verifyHTTPStatus(interactions, '404')
            verifyHTTPStatus(interactions, '202')
        }
    }

    def "should get response headers from feign client"() {
        when:
        final Pact pact = pactFactory.createFromFeignClient(ResponseHeadersFeignClient, 'SpecConsumer', objectMapper)

        then:
        with(pact) {
            consumer.name == 'SpecConsumer'
            provider.name == 'SpecProvider'
            interactions.size() == 1
            interactions[0].response.headers.containsKey('key1')
            interactions[0].response.headers.get('key1') == 'val1'
            interactions[0].response.headers.containsKey('key2')
            interactions[0].response.headers.get('key2') == 'val2'
        }
    }

    def "should get request body from feign client"() {
        when:
        final Pact pact = pactFactory.createFromFeignClient(RequestBodyFeignClient, 'SpecConsumer', objectMapper)

        then:
        with(pact) {
            consumer.name == 'SpecConsumer'
            provider.name == 'SpecProvider'
            interactions.size() == 2
            interactions.every { interaction ->
                interaction.request.body =~ /\{"requestFoo":".+","requestBar":".+"}/
            }
        }
    }

    def "should get request param from feign client"() {
        when:
        final Pact pact = pactFactory.createFromFeignClient(RequestParamFeignClient, 'SpecConsumer', objectMapper)

        then:
        with(pact) {
            consumer.name == 'SpecConsumer'
            provider.name == 'SpecProvider'
            interactions.size() == 1
            interactions[0].request.query =~ /longP=123&parameter=.+&params=.+/
        }
    }

    def "should throw exception when trying to generate pacts out from feign client with broken request"() {
        when:
            pactFactory.createFromFeignClient(BrokenRequestFeignClient, 'SpecConsumer', objectMapper)

        then:
            thrown(PojoNonCompliantWithPodamException)
    }

    def "should throw exception when trying to generate pacts out from feign client with request with nested broken type"() {
        when:
            pactFactory.createFromFeignClient(BrokenNestedRequestFeignClient, 'SpecConsumer', objectMapper)

        then:
            thrown(PojoNonCompliantWithPodamException)
    }

    def "should throw exception when trying to generate pacts out from feign client with broken response"() {
        when:
            pactFactory.createFromFeignClient(BrokenResponseFeignClient, 'SpecConsumer', objectMapper)

        then:
            thrown(MissingGettersException)
    }

    def "should get response body from feign client"() {
        when:
        final Pact pact = pactFactory.createFromFeignClient(ResponseBodyFeignClient, 'SpecConsumer', objectMapper)

        then:
        with(pact) {
            consumer.name == 'SpecConsumer'
            provider.name == 'SpecProvider'
            interactions.size() == 3

            interactions.any { interaction ->
                interaction.request.method == 'GET'
                interaction.response.body == null
            }

            interactions.any { interaction ->
                interaction.request.method == 'POST'
                interaction.response.body =~ /\{"responseFoo":".+","responseBar":"responseReplacedBar"}/
            }

            interactions.any { interaction ->
                interaction.request.method == 'PUT'
                interaction.response.body =~ /\{"data":\[(\{"testField":".+"},*)]}/
            }
        }
    }

    private static boolean verifyHTTPMethod(List<Interaction> interactions, String method) {
        interactions.any { interaction ->
            interaction.request.method == method
        }
    }

    private static boolean verifyHTTPStatus(List<Interaction> interactions, String status) {
        interactions.any { interaction ->
            interaction.response.status == status
        }
    }

    private static boolean verifyDescription(List<Interaction> interactions, String description) {
        interactions.any { interaction ->
            interaction.description == description
        }
    }

    private static boolean verifyMultiplePathVariables(Interaction interaction) {
        def path = interaction.request.path

        def firstPathVariable = StringUtils.substringsBetween(interaction.request.path,'/common/test/', '/objects')[0]
        def secondPathVariable = StringUtils.substringAfter(path, '/objects')

        !firstPathVariable.isEmpty() && firstPathVariable != '{testId}' && !secondPathVariable.isEmpty() && secondPathVariable != '{anotherTestId}'
    }
}
