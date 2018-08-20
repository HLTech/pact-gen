package dev.hltech.spec

import dev.hltech.pact.generation.FeignClientsFinder
import dev.hltech.pact.generation.PactFactory
import dev.hltech.pact.generation.model.Pact
import spock.lang.Specification
import spock.lang.Subject

class PactFactorySpec extends Specification {

    private FeignClientsFinder feignClientsFinder = new FeignClientsFinder()

    @Subject
    private PactFactory pactFactory = new PactFactory()

    def "should create object representing pact file out of feign client"() {
        when:
            final Pact pact = pactFactory.create(
                feignClientsFinder.findFeignClients('dev.hltech.spec')[0], 'SpecConsumer')

        then:
            with(pact) {
                consumer.name == 'SpecConsumer'
                provider.name == 'SpecProvider'
                interactions.size() == 2

                interactions[0].description == 'getTestObject'
                interactions[0].request.method == 'GET'
                interactions[0].request.path == '/test/objects'

                interactions[1].description == 'deleteTestObject'
                interactions[1].request.method == 'DELETE'
                interactions[1].request.path == '/test/objects'
            }
    }
}
