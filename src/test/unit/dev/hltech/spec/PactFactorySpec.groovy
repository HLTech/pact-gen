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
                interactions.size() == 8
                println interactions
                interactions.any { interaction ->
                    interaction.description == 'deleteTestObject' &&
                    interaction.request.method == 'DELETE' &&
                    interaction.request.path == '/test/objects/1' &&
                    interaction.request.headers[0].name == 'key1' &&
                    interaction.request.headers[0].value == 'val1' &&
                    interaction.request.headers[1].name == 'key2' &&
                    interaction.request.headers[1].value == 'val2'
                }

                interactions.any { interaction ->
                    interaction.description == 'getTestObject' &&
                    interaction.request.method == 'GET' &&
                    interaction.request.path == '/test/objects/2'
                }

                interactions.any { interaction ->
                    interaction.description == 'headTestObject' &&
                    interaction.request.method == 'HEAD' &&
                    interaction.request.path == '/test/objects/3'
                }

                interactions.any { interaction ->
                    interaction.description == 'optionsTestObject' &&
                    interaction.request.method == 'OPTIONS' &&
                    interaction.request.path == '/test/objects/4'
                }

                interactions.any { interaction ->
                    interaction.description == 'patchTestObject' &&
                    interaction.request.method == 'PATCH' &&
                    interaction.request.path == '/test/objects/5'
                }

                interactions.any { interaction ->
                    interaction.description == 'createTestObject' &&
                    interaction.request.method == 'POST' &&
                    interaction.request.path == '/test/objects' &&
                    interaction.request.headers.get(0).name == 'key1' &&
                    interaction.request.headers.get(0).value == 'val1' &&
                    interaction.request.headers.get(1).name == 'key2' &&
                    interaction.request.headers.get(1).value == 'val2'
                }

                interactions.any { interaction ->
                    interaction.description == 'updateTestObject' &&
                    interaction.request.method == 'PUT' &&
                    interaction.request.path == '/test/objects/6'
                }

                interactions.any { interaction ->
                    interaction.description == 'traceTestObject' &&
                    interaction.request.method == 'TRACE' &&
                    interaction.request.path == '/test/objects/7'
                }
            }
    }
}
