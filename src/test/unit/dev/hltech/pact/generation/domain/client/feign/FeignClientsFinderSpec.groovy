package dev.hltech.pact.generation.domain.client.feign

import spock.lang.Specification
import spock.lang.Subject

class FeignClientsFinderSpec extends Specification {

    @Subject
    private FeignClientsFinder finder = new FeignClientsFinder()

    def "should find feign client"() {
        when:
            Set<Class<?>> feignClients = finder.findFeignClients('dev.hltech.pact.generation.domain')

        then:
            feignClients.size() == 1
            feignClients[0].simpleName == 'SampleSpecFeignClient'
    }

    def "should not find feign clients"() {
        when:
            Set<Class<?>> feignClients = finder.findFeignClients('some.not.existing.package')

        then:
            feignClients.size() == 0
    }
}
