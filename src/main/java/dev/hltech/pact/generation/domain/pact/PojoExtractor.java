package dev.hltech.pact.generation.domain.pact;

import dev.hltech.pact.generation.domain.client.model.Body;
import dev.hltech.pact.generation.domain.client.model.ClientMethodRepresentation;
import dev.hltech.pact.generation.domain.client.model.ResponseProperties;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

final class PojoExtractor {

    private PojoExtractor() {}

    static Set<Class<?>> extractPojoTypes(ClientMethodRepresentation methodRepresentation) {
        Set<Class<?>> pojoClasses = new HashSet<>();

        final Body requestBody = methodRepresentation.getRequestProperties().getBody();

        if (requestBody.getBodyType() != null) {
            pojoClasses.add(requestBody.getBodyType());
        }
        if (!CollectionUtils.isEmpty(requestBody.getGenericArgumentTypes())) {
            pojoClasses.addAll(requestBody.getGenericArgumentTypes());
        }

        final List<ResponseProperties> responsePropertiesList = methodRepresentation.getResponsePropertiesList();

        responsePropertiesList.forEach(responseProperties -> {
            final Body responsePropertyBody = responseProperties.getBody();

            if (responsePropertyBody.getBodyType() != null) {
                pojoClasses.add(responsePropertyBody.getBodyType());
            }
            if (!CollectionUtils.isEmpty(responsePropertyBody.getGenericArgumentTypes())) {
                pojoClasses.addAll(responsePropertyBody.getGenericArgumentTypes());
            }
        });

        return pojoClasses.stream()
            .filter(clazz -> clazz != void.class)
            .collect(Collectors.toSet());
    }
}
