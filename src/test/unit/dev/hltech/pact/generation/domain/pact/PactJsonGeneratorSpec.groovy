package dev.hltech.pact.generation.domain.pact

import com.google.common.collect.Lists
import dev.hltech.pact.generation.domain.pact.model.Interaction
import dev.hltech.pact.generation.domain.pact.model.InteractionRequest
import dev.hltech.pact.generation.domain.pact.model.InteractionResponse
import dev.hltech.pact.generation.domain.pact.model.Metadata
import dev.hltech.pact.generation.domain.pact.model.Pact
import groovy.json.JsonSlurper
import org.junit.Rule
import org.junit.rules.TemporaryFolder
import spock.lang.Specification
import spock.lang.Subject

class PactJsonGeneratorSpec extends Specification {

    @Rule
    private TemporaryFolder temporaryFolder

    @Subject
    private PactJsonGenerator generator = new PactJsonGenerator()

    def "should generate pact file"() {
        given: 'pact object'
            final Pact pact = createSamplePact()

        and: 'temporary directory for pact files'
            File pactsDirectory = temporaryFolder.newFolder('pactsDirectory')

        when:
            generator.writePactFiles(pactsDirectory, [pact])

        then: 'pact file exists'
            final File pactFile = pactsDirectory.listFiles().find { file ->
                file.name == 'Consumer-Provider.json'
            }
            pactFile

        and: 'pact file contains necessary info'
            def jsonRoot = new JsonSlurper().parse(pactFile)
            jsonRoot.provider.name == 'Provider'
            jsonRoot.consumer.name == 'Consumer'
            jsonRoot.interactions.size() == 1
            jsonRoot.interactions[0].description == 'createFromFeignClient test object'
            jsonRoot.interactions[0].request.method == 'POST'
            jsonRoot.interactions[0].request.path == '/test/objects'
            jsonRoot.interactions[0].request.headers.'Authorization' == 'Bearer T3VyUGFjdEdlbmVyYXRvcklzVG90YWxseUF3ZXNvbWU='
            jsonRoot.interactions[0].response.status == '200'
            jsonRoot.interactions[0].response.headers.'Location' == '/test/objects/123'
            jsonRoot.metadata.pactSpecificationVersion == '1.0.0'
    }

    private static Pact createSamplePact() {
        return Pact.builder()
            .provider(new Service('Provider'))
            .consumer(new Service('Consumer'))
            .interactions([
                    Interaction.builder()
                        .description('createFromFeignClient test object')
                        .request(InteractionRequest.builder()
                            .method('POST')
                            .path('/test/objects')
                            .headers(generateHeaders(Lists.newArrayList(new AbstractMap.SimpleEntry<String, String>('Authorization', 'Bearer T3VyUGFjdEdlbmVyYXRvcklzVG90YWxseUF3ZXNvbWU='))))
                            .build())
                        .response(InteractionResponse.builder()
                            .status('200')
                            .headers(generateHeaders(Lists.newArrayList(new AbstractMap.SimpleEntry<String, String>('Location', '/test/objects/123'))))
                            .build())
                        .build()
            ])
            .metadata(new Metadata('1.0.0'))
            .build()
    }

    private static Map<String, String> generateHeaders(List<Map.Entry<String, String>> entries) {
        def map = new HashMap<String, String>()
        map.putAll(entries)
    }
}
