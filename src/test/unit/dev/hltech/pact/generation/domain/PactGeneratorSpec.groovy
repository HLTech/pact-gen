package dev.hltech.pact.generation.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.Lists
import com.google.common.collect.Sets
import dev.hltech.pact.generation.PactGenerator
import dev.hltech.pact.generation.domain.client.feign.FeignClientsFinder
import dev.hltech.pact.generation.domain.client.feign.sample.FirstEmptyFeignClient
import dev.hltech.pact.generation.domain.client.feign.sample.SecondEmptyFeignClient
import dev.hltech.pact.generation.domain.pact.PactFactory
import dev.hltech.pact.generation.domain.pact.PactJsonGenerator
import dev.hltech.pact.generation.domain.pact.Service
import dev.hltech.pact.generation.domain.pact.model.Interaction
import dev.hltech.pact.generation.domain.pact.model.Pact
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

class PactGeneratorSpec extends Specification {

    @Rule
    private TemporaryFolder temporaryFolder

    def feignClientsFinderMock = Mock(FeignClientsFinder)
    def pactFactoryMock = Mock(PactFactory)
    def pactJsonGeneratorMock = Mock(PactJsonGenerator)
    def objectMapperMock = Mock(ObjectMapper)

    @Subject
    private PactGenerator pactGenerator = new PactGenerator(feignClientsFinderMock, pactFactoryMock, pactJsonGeneratorMock)

    def "should merge 2 pacts with the same provider"() {
        given:
            def dstDir = temporaryFolder.newFolder('dst')

        and:
            def pact = Pact.builder()
                        .consumer(new Service('same-consumer'))
                        .provider(new Service('same-provider'))
                        .interactions(Lists.newArrayList(new Interaction('1', null, null)))
                        .build()

            def anotherPact = Pact.builder()
                                  .consumer(new Service('same-consumer'))
                                  .provider(new Service('same-provider'))
                                  .interactions(Lists.newArrayList(new Interaction('2', null, null)))
                                  .build()

        and:
            feignClientsFinderMock.findFeignClients(_) >> Sets.newHashSet(FirstEmptyFeignClient.class, SecondEmptyFeignClient.class)
            pactFactoryMock.createFromFeignClient(FirstEmptyFeignClient.class, 'same-consumer', objectMapperMock) >> pact
            pactFactoryMock.createFromFeignClient(SecondEmptyFeignClient.class, 'same-consumer', objectMapperMock) >> anotherPact

        when:
            pactGenerator.writePactFiles('/', 'same-consumer', objectMapperMock, dstDir)

        then:
            1 * pactJsonGeneratorMock.writePactFiles(_,_) >> { File actualDstDir, Collection<Pact> pacts ->

                actualDstDir == dstDir
                assert pacts.size() == 1
                with(pacts[0]) {
                    consumer.name == 'same-consumer'
                    provider.name == 'same-provider'
                    interactions.size() == 2

                    interactions[0].description != interactions[1].description
                    ['1', '2'].contains(interactions[0].description)
                    ['1', '2'].contains(interactions[1].description)
                }
            }
    }

    def "should not merge 2 pacts with different providers"() {
        given:
            def dstDir = temporaryFolder.newFolder('dst')

        and:
            def pact = Pact.builder()
                .consumer(new Service('same-consumer'))
                .provider(new Service('provider1'))
                .interactions(Lists.newArrayList(new Interaction('1', null, null)))
                .build()

            def anotherPact = Pact.builder()
                .consumer(new Service('same-consumer'))
                .provider(new Service('provider2'))
                .interactions(Lists.newArrayList(new Interaction('2', null, null)))
                .build()

        and:
            feignClientsFinderMock.findFeignClients(_) >> Sets.newHashSet(FirstEmptyFeignClient.class, SecondEmptyFeignClient.class)
            pactFactoryMock.createFromFeignClient(FirstEmptyFeignClient.class, 'same-consumer', objectMapperMock) >> pact
            pactFactoryMock.createFromFeignClient(SecondEmptyFeignClient.class, 'same-consumer', objectMapperMock) >> anotherPact

        when:
            pactGenerator.writePactFiles('/', 'same-consumer', objectMapperMock, dstDir)

        then:
            1 * pactJsonGeneratorMock.writePactFiles(_,_) >> { File actualDstDir, Collection<Pact> pacts ->

                actualDstDir == dstDir
                assert pacts.size() == 2
            }
    }
}
