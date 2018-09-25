package dev.hltech.pact.generation.domain.client.feign

import spock.lang.Specification
import spock.lang.Subject

class FeignClientsFinderSpec extends Specification {

    @Subject
    private FeignClientsFinder finder = new FeignClientsFinder()

    def "should find feign client"() {
        when:
            Set<Class<?>> feignClients = finder.findFeignClients('dev.hltech.pact.generation.domain.client.feign.sample')

        then:
            feignClients.size() == 11
            verifyFeignClient(feignClients, 'DescriptionFeignClient')
            verifyFeignClient(feignClients, 'FirstEmptyFeignClient')
            verifyFeignClient(feignClients, 'PathFeignClient')
            verifyFeignClient(feignClients, 'RequestBodyFeignClient')
            verifyFeignClient(feignClients, 'RequestHeadersFeignClient')
            verifyFeignClient(feignClients, 'RequestParamFeignClient')
            verifyFeignClient(feignClients, 'RequestTypeFeignClient')
            verifyFeignClient(feignClients, 'ResponseBodyFeignClient')
            verifyFeignClient(feignClients, 'ResponseHeadersFeignClient')
            verifyFeignClient(feignClients, 'ResponseInfoFeignClient')
            verifyFeignClient(feignClients, 'SecondEmptyFeignClient')
    }

    def "should not find feign clients"() {
        when:
            Set<Class<?>> feignClients = finder.findFeignClients('some.not.existing.package')

        then:
            feignClients.size() == 0
    }

    private static boolean verifyFeignClient(Set<Class<?>> feignClients, String name) {
        feignClients.any { client ->
            client.simpleName == name
        }
    }
}
