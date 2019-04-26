package com.hltech.pact.gen.domain.pact

import com.google.common.collect.Lists
import spock.lang.Specification
import spock.lang.Subject
import uk.co.jemos.podam.api.AttributeMetadata
import uk.co.jemos.podam.api.RandomDataProviderStrategyImpl

import java.lang.reflect.Field

class BigDecimalManufacturerUT extends Specification {

    @Subject
    def manufacturer = new BigDecimalManufacturer()

    def strategy = new RandomDataProviderStrategyImpl()


    def "Should correctly manufacture BigDecimal"() {
        expect:
        def regexMatchingDecimalAndScientificNotation = '[+-]?([0-9]*[.])?[0-9]+(E-?[0-9]+)?'
        manufacturer.getType(strategy, createAttributeMetadata('sampleBigDecimal'), new HashMap<>()) =~ /${regexMatchingDecimalAndScientificNotation}/
    }

    private AttributeMetadata createAttributeMetadata(String name) {
        Field field = BigDecimalManufacturerUT.getDeclaredField(name)

        new AttributeMetadata(
            field.getName(),
            field.getType(),
            field.getGenericType(),
            null,
            Lists.newArrayList(field.getAnnotations()),
            BigDecimalManufacturerUT.class,
            this)
    }

    private BigInteger sampleBigDecimal
}
