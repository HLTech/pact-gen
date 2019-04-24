package com.hltech.pact.gen.domain.pact

import com.fasterxml.jackson.annotation.JsonProperty
import com.google.common.collect.Lists
import spock.lang.Specification
import spock.lang.Subject
import uk.co.jemos.podam.api.AttributeMetadata
import uk.co.jemos.podam.api.RandomDataProviderStrategyImpl

import java.lang.reflect.Field

class EnumStringManufacturerUT extends Specification {

    @Subject
    EnumStringManufacturer manufacturer = new EnumStringManufacturer()

    def strategy = new RandomDataProviderStrategyImpl()


    def "Should correctly manufacture annotated type having default value"() {
        given:
            def genericTypes = new HashMap<>()
            def metadata = createAttributeMetadata('annotatedFieldWithDefaultValue')

        expect:
            manufacturer.getType(strategy, metadata, genericTypes) == 'TEST_VALUE'
    }

    def "Should correctly manufacture annotated type not having default value"() {
        given:
            def genericTypes = new HashMap<>()
            def metadata = createAttributeMetadata('annotatedFieldWithoutDefaultValue')

        expect:
            manufacturer.getType(strategy, metadata, genericTypes) =~ /.+/
    }

    def "Should correctly manufacture not annotated type"() {
        given:
            def genericTypes = new HashMap<>()
            def metadata = createAttributeMetadata('notAnnotatedField')

        expect:
            manufacturer.getType(strategy, metadata, genericTypes) =~ /.+/
    }

    private AttributeMetadata createAttributeMetadata(String name) {
        Field field = EnumStringManufacturerUT.getDeclaredField(name)

        new AttributeMetadata(
            field.getName(),
            field.getType(),
            field.getGenericType(),
            null,
            Lists.newArrayList(field.getAnnotations()),
            EnumStringManufacturerUT.class,
            this)
    }

    @JsonProperty(defaultValue = "TEST_VALUE")
    private String annotatedFieldWithDefaultValue

    @JsonProperty
    private String annotatedFieldWithoutDefaultValue

    private String notAnnotatedField
}
