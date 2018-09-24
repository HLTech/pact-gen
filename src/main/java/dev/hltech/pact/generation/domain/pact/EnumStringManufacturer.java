package dev.hltech.pact.generation.domain.pact;

import com.fasterxml.jackson.annotation.JsonProperty;
import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.typeManufacturers.StringTypeManufacturerImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

public class EnumStringManufacturer extends StringTypeManufacturerImpl {

    @Override
    public String getType(DataProviderStrategy strategy,
                          AttributeMetadata attributeMetadata,
                          Map<String, Type> genericTypesArgumentsMap) {

        Optional<Annotation> jsonPropertyAnnotation = attributeMetadata.getAttributeAnnotations().stream()
            .filter(annotation -> annotation.annotationType().equals(JsonProperty.class))
            .findAny();

        return jsonPropertyAnnotation.filter(annotation -> !((JsonProperty) annotation).defaultValue().isEmpty())
            .map(annotation -> ((JsonProperty) annotation).defaultValue())
            .orElseGet(() -> super.getType(strategy, attributeMetadata, genericTypesArgumentsMap));
    }
}
