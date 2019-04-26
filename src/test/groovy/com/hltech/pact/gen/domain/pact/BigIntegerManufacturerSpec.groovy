package com.hltech.pact.gen.domain.pact

import com.google.common.collect.Lists
import spock.lang.Specification
import spock.lang.Subject
import uk.co.jemos.podam.api.AttributeMetadata
import uk.co.jemos.podam.api.RandomDataProviderStrategyImpl

import java.lang.reflect.Field

class BigIntegerManufacturerSpec extends Specification {

    @Subject
    def manufacturer = new BigIntegerManufacturer()

    def strategy = new RandomDataProviderStrategyImpl()


    def "Should correctly manufacture BigInteger"() {
        expect:
            manufacturer.getType(strategy, createAttributeMetadata('sampleBigInteger'), new HashMap<>()) =~ /[0-9]+/
    }

    private AttributeMetadata createAttributeMetadata(String name) {
        Field field = BigIntegerManufacturerSpec.getDeclaredField(name)

        new AttributeMetadata(
            field.getName(),
            field.getType(),
            field.getGenericType(),
            null,
            Lists.newArrayList(field.getAnnotations()),
            BigIntegerManufacturerSpec.class,
            this)
    }

    private BigInteger sampleBigInteger
}
