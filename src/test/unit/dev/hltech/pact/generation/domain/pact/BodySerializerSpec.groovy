package dev.hltech.pact.generation.domain.pact

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.common.collect.Lists
import dev.hltech.pact.generation.domain.GenericResponseType
import dev.hltech.pact.generation.domain.TestParam
import dev.hltech.pact.generation.domain.client.model.Body
import spock.lang.Specification

class BodySerializerSpec extends Specification {

    private ObjectMapper mapper = new ObjectMapper()

    def "Should correctly serialize body basing on its type"() {
        given:
            Body body = new Body(GenericResponseType.class, Lists.newArrayList(TestParam.class))

        expect:
            BodySerializer.serializeBody(body, mapper) =~ /\{"data":\[(\{"testField":".+"},*)+]}/
    }
}
