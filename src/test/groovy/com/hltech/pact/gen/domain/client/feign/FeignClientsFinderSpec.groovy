package com.hltech.pact.gen.domain.client.feign

import spock.lang.Specification
import spock.lang.Subject

class FeignClientsFinderSpec extends Specification {

    @Subject
    private FeignClientsFinder finder = new FeignClientsFinder()

    def "should find feign client"() {
        when:
            Set<Class<?>> feignClients = finder.findFeignClients('com.hltech.pact.gen.domain.client.feign.sample')

        then:
            feignClients.size() == 17
            verifyFeignClient(feignClients, 'BrokenRequestFeignClient')
            verifyFeignClient(feignClients, 'BrokenNestedRequestFeignClient')
            verifyFeignClient(feignClients, 'BrokenResponseFeignClient')
            verifyFeignClient(feignClients, 'BrokenResponseFeignClient')
            verifyFeignClient(feignClients, 'DescriptionFeignClient')
            verifyFeignClient(feignClients, 'ExpectedEmptyResponseBodyFeignClient')
            verifyFeignClient(feignClients, 'FirstEmptyFeignClient')
            verifyFeignClient(feignClients, 'PathFeignClient')
            verifyFeignClient(feignClients, 'RequestBodyFeignClient')
            verifyFeignClient(feignClients, 'RequestHeadersFeignClient')
            verifyFeignClient(feignClients, 'RequestParamFeignClient')
            verifyFeignClient(feignClients, 'RequestTypeFeignClient')
            verifyFeignClient(feignClients, 'ResponseBodyFeignClient')
            verifyFeignClient(feignClients, 'ResponseHeadersFeignClient')
            verifyFeignClient(feignClients, 'InteractionInfoFeignClient')
            verifyFeignClient(feignClients, 'SecondEmptyFeignClient')
            verifyFeignClient(feignClients, 'OptionalResponseFeignClient')
            verifyFeignClient(feignClients, 'AdditionalNotAnnotatedMethodsFeignClient')
            !verifyFeignClient(feignClients, 'ExcludedFeignClient')
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
