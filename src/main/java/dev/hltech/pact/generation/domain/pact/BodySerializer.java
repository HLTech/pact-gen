package dev.hltech.pact.generation.domain.pact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.domain.client.model.Body;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.io.IOException;

final class BodySerializer {

    private BodySerializer() {
    }

    static JsonNode serializeBody(Body body, ObjectMapper objectMapper) {
        JsonNode serializedBody = null;

        try {
            if (body.getBodyType() != null && !body.getBodyType().getSimpleName().equals("void")) {
                serializedBody = objectMapper.readTree(objectMapper.writeValueAsString(populateRequestObject(body)));
            }
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        return serializedBody;
    }

    private static Object populateRequestObject(Body body) {
        Class<?>[] types = body.getGenericArgumentTypes().toArray(new Class<?>[0]);
        return new PodamFactoryImpl().manufacturePojo(body.getBodyType(), types);
    }
}
