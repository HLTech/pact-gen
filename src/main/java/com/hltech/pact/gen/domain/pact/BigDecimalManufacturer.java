package com.hltech.pact.gen.domain.pact;

import uk.co.jemos.podam.api.AttributeMetadata;
import uk.co.jemos.podam.api.DataProviderStrategy;
import uk.co.jemos.podam.typeManufacturers.AbstractTypeManufacturer;
import uk.co.jemos.podam.typeManufacturers.IntTypeManufacturerImpl;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;

public class BigDecimalManufacturer extends AbstractTypeManufacturer<BigDecimal> {

    public static final Integer MINIMAL_SCALE = 0;
    public static final Integer MAXIMAL_SCALE = 100;

    @Override
    public BigDecimal getType(DataProviderStrategy strategy,
                              AttributeMetadata attributeMetadata,
                              Map<String, Type> genericTypesArgumentsMap) {
        return new BigDecimal(
            new BigIntegerManufacturer().getType(strategy, attributeMetadata, genericTypesArgumentsMap),
            new IntTypeManufacturerImpl().getIntegerInRange(MINIMAL_SCALE, MAXIMAL_SCALE, attributeMetadata));
    }
}
