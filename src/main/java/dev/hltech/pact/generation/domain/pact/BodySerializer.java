package dev.hltech.pact.generation.domain.pact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.PactGenerationException;
import dev.hltech.pact.generation.domain.client.model.Body;
import lombok.extern.slf4j.Slf4j;
import uk.co.jemos.podam.api.PodamFactory;

import java.io.IOException;

@Slf4j
final class BodySerializer {

    private BodySerializer() {
    }

    static JsonNode serializeBody(Body body, ObjectMapper objectMapper, PodamFactory podamFactory) {
        String serializedBody = null;
        JsonNode bodyJsonNode = null;

        try {
            if (body.getBodyType() != null && !body.getBodyType().getSimpleName().equals("void")) {
                serializedBody = objectMapper.writeValueAsString(populateRequestObject(body, podamFactory));
                bodyJsonNode = objectMapper.readTree(serializedBody);
            }
        } catch (JsonProcessingException ex) {
            log.error("Unable to write {} to json. Original error message '{}'",
                body, ex.getMessage());
            throw new PactGenerationException("Unable to serialize body", ex);
        } catch (IOException ex) {
            log.error("Unable to convert {} to json node. Original error message '{}'",
                serializedBody, ex.getMessage());
            throw new PactGenerationException("Unable to convert serialized body to json node", ex);
        }

        return bodyJsonNode;
    }

    private static Object populateRequestObject(Body body, PodamFactory podamFactory) {
        Class<?>[] types = body.getGenericArgumentTypes().toArray(new Class<?>[0]);
        Object manufacturedPojo = podamFactory.manufacturePojo(body.getBodyType(), types);

        if (manufacturedPojo == null) {
            throw new PactGenerationException("Podam manufacturing failed");
        }
        
        return manufacturedPojo;
    }
}
