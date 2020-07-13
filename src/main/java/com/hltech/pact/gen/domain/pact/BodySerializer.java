package com.hltech.pact.gen.domain.pact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hltech.pact.gen.PactGenerationException;
import com.hltech.pact.gen.domain.client.model.Body;
import lombok.extern.slf4j.Slf4j;
import uk.co.jemos.podam.api.PodamFactory;

import java.util.Optional;

@Slf4j
final class BodySerializer {

    private BodySerializer() {
    }

    static JsonNode serializeBody(Body body, ObjectMapper objectMapper, PodamFactory podamFactory) {
        String serializedBody = null;
        JsonNode bodyJsonNode = null;

        try {
            if (body.getType() != null && !body.getType().equals(Void.TYPE)) {
                serializedBody = objectMapper.writeValueAsString(populateRequestObject(body, podamFactory));
                bodyJsonNode = objectMapper.readTree(serializedBody);
            }
        } catch (JsonProcessingException ex) {
            log.error("Unable to write {} to json. Original error message '{}'",
                body, ex.getMessage());
            throw new PactGenerationException("Unable to serialize body", ex);
        }

        return bodyJsonNode;
    }

    private static Object populateRequestObject(Body body, PodamFactory podamFactory) {
        Class<?>[] types = body.getGenericArgumentTypes().toArray(new Class<?>[0]);

        Object manufacturedPojo = Optional.class.equals(body.getType())
            ? podamFactory.manufacturePojo(types[0])
            : podamFactory.manufacturePojo(body.getType(), types);

        if (manufacturedPojo == null) {
            throw new PactGenerationException("Podam manufacturing failed");
        }

        return manufacturedPojo;
    }
}
