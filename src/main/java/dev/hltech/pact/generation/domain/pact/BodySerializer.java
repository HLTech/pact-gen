package dev.hltech.pact.generation.domain.pact;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.domain.client.model.Body;
import uk.co.jemos.podam.api.PodamFactoryImpl;

final class BodySerializer {

    private BodySerializer() {
    }

    static String serializeBody(Body body, ObjectMapper objectMapper) {
        String serializedBody = null;

        try {
            if (body.getBodyType() != null && !body.getBodyType().getSimpleName().equals("void")) {
                serializedBody = objectMapper.writeValueAsString(populateRequestObject(body));
            }
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }

        return serializedBody;
    }

    private static Object populateRequestObject(Body body) {
        Class<?>[] types = body.getGenericArgumentTypes().toArray(new Class<?>[0]);
        return new PodamFactoryImpl().manufacturePojo(body.getBodyType(), types);
    }
}
