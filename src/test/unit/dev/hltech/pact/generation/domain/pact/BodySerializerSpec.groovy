package dev.hltech.pact.generation.domain.pact

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.Lists
import dev.hltech.pact.generation.domain.GenericResponseType
import dev.hltech.pact.generation.domain.TestParam
import dev.hltech.pact.generation.domain.client.model.Body
import spock.lang.Specification
import uk.co.jemos.podam.api.PodamFactory
import uk.co.jemos.podam.api.PodamFactoryImpl

class BodySerializerSpec extends Specification {

    private ObjectMapper mapper = new ObjectMapper()
    private PodamFactory podamFactory = new PodamFactoryImpl()

    def "Should correctly serialize body basing on its type"() {
        given:
            Body body = new Body(GenericResponseType.class, Lists.newArrayList(TestParam.class))

        expect:
            BodySerializer.serializeBody(body, mapper, podamFactory) =~ /\{"data":\[(\{"testField":".+"},*)+]}/
    }
}
