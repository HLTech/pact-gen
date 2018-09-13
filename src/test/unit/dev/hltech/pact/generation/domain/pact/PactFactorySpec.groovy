package dev.hltech.pact.generation.domain.pact

import com.fasterxml.jackson.databind.ObjectMapper
import dev.hltech.pact.generation.domain.client.feign.FeignClientsFinder
import dev.hltech.pact.generation.domain.pact.model.Pact
import org.apache.commons.lang.StringUtils
import spock.lang.Specification
import spock.lang.Subject

class PactFactorySpec extends Specification {

    private FeignClientsFinder feignClientsFinder = new FeignClientsFinder()

    @Subject
    private PactFactory pactFactory = new PactFactory()

    def "should create object representing pact file out of feign client"() {
        given:
            final ObjectMapper objectMapper = new ObjectMapper()

        when:
            final Pact pact = pactFactory.createFromFeignClient(
                feignClientsFinder.findFeignClients('dev.hltech.pact.generation.domain')[0], 'SpecConsumer', objectMapper)

        then:
            with(pact) {
                consumer.name == 'SpecConsumer'
                provider.name == 'SpecProvider'
                interactions.size() == 8

                interactions.any { interaction ->
                    interaction.description == 'deleteTestObject' &&
                    interaction.request.method == 'DELETE' &&
                    !StringUtils.substringBetween(interaction.request.path, '/test/', '/objects/1').isEmpty() &&
                    interaction.request.headers[0].name == 'key1' &&
                    interaction.request.headers[0].value == 'val1' &&
                    interaction.request.headers[1].name == 'key2' &&
                    interaction.request.headers[1].value == 'val2' &&
                    interaction.response.status == '200' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":".+"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'getTestObject' &&
                    interaction.request.method == 'GET' &&
                    !StringUtils.substringBetween(interaction.request.path,'/test/', '/objects/2').isEmpty() &&
                    interaction.response.status == '200' &&
                    interaction.response.headers[0].name == 'key3' &&
                    interaction.response.headers[0].value == 'val3' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":".+"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'headTestObject' &&
                    interaction.request.method == 'HEAD' &&
                    interaction.request.path == '/test/objects/3' &&
                    interaction.response.status == '200' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":".+"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'optionsTestObject' &&
                    interaction.request.method == 'OPTIONS' &&
                    interaction.request.path == '/test/objects/4' &&
                    interaction.request.headers[0].name == 'key4' &&
                    interaction.request.headers[0].value == 'val4' &&
                    interaction.response.status == '200' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":".+"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'patchTestObject' &&
                    interaction.request.method == 'PATCH' &&
                    interaction.request.path == '/test/objects/5' &&
                    interaction.request.query == 'longP=abc&very_long_name=def' &&
                    interaction.response.status == '202' &&
                    interaction.response.body == null
                }

                interactions.any { interaction ->
                    interaction.description == 'createTestObject' &&
                    interaction.request.method == 'POST' &&
                    interaction.request.path == '/test/objects' &&
                    interaction.request.headers.get(0).name == 'key1' &&
                    interaction.request.headers.get(0).value == 'val1' &&
                    interaction.request.headers.get(1).name == 'key2' &&
                    interaction.request.headers.get(1).value == 'val2' &&
                    interaction.request.headers.get(2).name == 'id' &&
                    !interaction.request.headers.get(2).value.isEmpty() &&
                    interaction.request.query.startsWith('parameters=') &&
                    interaction.request.query.length() > "parameters=".length() &&
                    interaction.request.body =~ /\{"requestFoo":".+","requestBar":".+"}/ &&
                    interaction.response.status == '202' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":".+"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'updateTestObject' &&
                    interaction.request.method == 'PUT' &&
                    interaction.request.path == '/test/objects/6' &&
                    interaction.request.headers[0].name == 'key1' &&
                    !interaction.request.headers[0].value.isEmpty() &&
                    interaction.request.body =~ /\{"requestFoo":".+","requestBar":".+"}/ &&
                    interaction.response.status == '200' &&
                    interaction.response.headers[0].name == 'key3' &&
                    interaction.response.headers[0].value == 'val3' &&
                    interaction.response.headers[1].name == 'key4' &&
                    interaction.response.headers[1].value == 'val4' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":".+"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'traceTestObject' &&
                    interaction.request.method == 'TRACE' &&
                    interaction.request.path == '/test/objects/7' &&
                    interaction.request.query.contains('param=') &&
                    interaction.request.headers.get(0).name == 'type' &&
                    !interaction.request.headers.get(0).value.isEmpty() &&
                    interaction.response.status == '200' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":".+"}/
                }
            }
    }
}
