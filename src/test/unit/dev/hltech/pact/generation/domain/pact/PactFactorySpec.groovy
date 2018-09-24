package dev.hltech.pact.generation.domain.pact

import com.fasterxml.jackson.databind.ObjectMapper
import dev.hltech.pact.generation.domain.client.feign.FeignClientsFinder
import dev.hltech.pact.generation.domain.pact.model.Interaction
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
                interactions.size() == 9

                interactions.any { interaction ->
                    interaction.description == 'deleteTestObject' &&
                    interaction.request.method == 'DELETE' &&
                    !StringUtils.substringBetween(interaction.request.path, '/test/', '/objects/1').isEmpty() &&
                    interaction.request.headers.containsKey('key1') &&
                    interaction.request.headers.get('key1') == 'val1' &&
                    interaction.request.headers.containsKey('key2') &&
                    interaction.request.headers.get('key2') == 'val2' &&
                    interaction.response.status == '200' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":"responseReplacedBar"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'deleteTestObject' &&
                    interaction.request.method == 'DELETE' &&
                    !StringUtils.substringBetween(interaction.request.path, '/test/', '/objects/1').isEmpty() &&
                    interaction.request.headers.containsKey('key1') &&
                    interaction.request.headers.get('key1') == 'val1' &&
                    interaction.request.headers.containsKey('key2') &&
                    interaction.request.headers.get('key2') == 'val2' &&
                    interaction.response.status == '502' &&
                    interaction.response.headers.containsKey('key3') &&
                    interaction.response.headers.get('key3') == 'val3' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":"responseReplacedBar"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'getTestObject' &&
                    interaction.request.method == 'GET' &&
                    verifyMultiplePathVariables(interaction)
                    interaction.response.status == '200' &&
                    interaction.response.headers.containsKey('key3') &&
                    interaction.response.headers.get('key3') == 'val3' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":"responseReplacedBar"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'headTestObject' &&
                    interaction.request.method == 'HEAD' &&
                    interaction.request.path == '/test/objects/3' &&
                    interaction.response.status == '200' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":"responseReplacedBar"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'optionsTestObject' &&
                    interaction.request.method == 'OPTIONS' &&
                    interaction.request.path == '/test/objects/4' &&
                    interaction.request.headers.containsKey('key4') &&
                    interaction.request.headers.get('key4') == 'val4' &&
                    interaction.response.status == '200' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":"responseReplacedBar"}/
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
                    interaction.request.headers.containsKey('key1') &&
                    interaction.request.headers.get('key1') == 'val1' &&
                    interaction.request.headers.containsKey('key2') &&
                    interaction.request.headers.get('key2') == 'val2' &&
                    interaction.request.headers.containsKey('id') &&
                    !interaction.request.headers.get('id').isEmpty() &&
                    interaction.request.query.startsWith('parameters=') &&
                    interaction.request.query.length() > "parameters=".length() &&
                    interaction.request.body =~ /\{"requestFoo":".+","requestBar":".+"}/ &&
                    interaction.response.status == '202' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":"responseReplacedBar"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'updateTestObject' &&
                    interaction.request.method == 'PUT' &&
                    interaction.request.path == '/test/objects/6' &&
                    interaction.request.headers.containsKey('key1') &&
                    !interaction.request.headers.get('key1').isEmpty() &&
                    interaction.request.body =~ /\{"requestFoo":".+","requestBar":".+"}/ &&
                    interaction.response.status == '200' &&
                    interaction.response.headers.containsKey('key3') &&
                    interaction.response.headers.get('key3') == 'val3' &&
                    interaction.response.headers.containsKey('key4') &&
                    interaction.response.headers.get('key4') == 'val4' &&
                    interaction.response.body =~ /\{"responseFoo":".+","responseBar":"responseReplacedBar"}/
                }

                interactions.any { interaction ->
                    interaction.description == 'traceTestObject' &&
                    interaction.request.method == 'TRACE' &&
                    interaction.request.path == '/test/objects/7' &&
                    interaction.request.query.contains('param=') &&
                    interaction.request.headers.containsKey('type') &&
                    !interaction.request.headers.get('type').isEmpty() &&
                    interaction.response.status == '200' &&
                    interaction.response.body =~ /\{"data":\[(\{"testField":".+"},*)+]}/
                }
            }
    }

    boolean verifyMultiplePathVariables(Interaction interaction) {
        def path = interaction.request.path

        def firstPathVariable = StringUtils.substringsBetween(interaction.request.path,'/test/', '/objects')[0]
        def secondPathVariable = StringUtils.substringAfter(path, '/objects')

        !firstPathVariable.isEmpty() && firstPathVariable != {'testId'} && !secondPathVariable.isEmpty() && secondPathVariable != {'anotherTestId'}
    }
}
