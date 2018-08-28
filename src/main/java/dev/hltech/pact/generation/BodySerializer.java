package dev.hltech.pact.generation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import uk.co.jemos.podam.api.PodamFactoryImpl;

final class BodySerializer {

    private BodySerializer() {
    }

    static String serializeBody(Class<?> type, ObjectMapper objectMapper) {
        String body = null;

        try {
            if (type != null && !type.getSimpleName().equals("void")) {
                body = objectMapper.writeValueAsString(populateRequestObject(type));
            }
        } catch (JsonProcessingException ex) {
            System.out.println(ex.getMessage());
        }

        return body;
    }

    private static Object populateRequestObject(Class<?> type) {
        return new PodamFactoryImpl().manufacturePojo(type);
    }
}
