package dev.hltech.spec

import dev.hltech.pact.generation.FeignClientsFinder
import org.springframework.cloud.openfeign.FeignClient
import spock.lang.Specification
import spock.lang.Subject

class FeignClientsFinderSpec extends Specification {

    @Subject
    private FeignClientsFinder finder = new FeignClientsFinder()

    def "should find feign client"() {
        when:
            Set<Class<?>> feignClients = finder.findFeignClients('dev.hltech.spec')

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
