package dev.hltech.pact.generation.domain.pact;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.domain.client.model.Body;
import lombok.extern.slf4j.Slf4j;
import uk.co.jemos.podam.api.PodamFactory;

import java.io.IOException;

@Slf4j
final class BodySerializer {

    private BodySerializer() {
    }

    static JsonNode serializeBody(Body body, ObjectMapper objectMapper, PodamFactory podamFactory) {
        JsonNode serializedBody = null;

        try {
            if (body.getBodyType() != null && !body.getBodyType().getSimpleName().equals("void")) {
                serializedBody = objectMapper.readTree(
                    objectMapper.writeValueAsString(populateRequestObject(body, podamFactory)));
            }
        } catch (IOException ex) {
            log.error("Unable to write {} to json. Original error message '{}'", body, ex.getMessage());
            throw new IllegalArgumentException("Not possible to serialize body", ex);
        }

        return serializedBody;
    }

    private static Object populateRequestObject(Body body, PodamFactory podamFactory) {
        Class<?>[] types = body.getGenericArgumentTypes().toArray(new Class<?>[0]);
        return podamFactory.manufacturePojo(body.getBodyType(), types);
    }
}
