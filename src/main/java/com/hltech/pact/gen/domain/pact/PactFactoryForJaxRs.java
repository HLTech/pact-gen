package com.hltech.pact.gen.domain.pact;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hltech.pact.gen.domain.pact.model.Metadata;
import com.hltech.pact.gen.domain.pact.model.Pact;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.math.BigDecimal;
import java.math.BigInteger;

public class PactFactoryForJaxRs {

    private static final PodamFactory podamFactory;

    static {
        podamFactory = new PodamFactoryImpl();
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(String.class, new EnumStringManufacturer());
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(BigInteger.class, new BigIntegerManufacturer());
        podamFactory.getStrategy().addOrReplaceTypeManufacturer(BigDecimal.class, new BigDecimalManufacturer());
        podamFactory.getStrategy().setDefaultNumberOfCollectionElements(1);
    }

    public Pact createFromJaxRsClient(Class<?> jaxRsClient, String consumerName, ObjectMapper objectMapper) {

        //TODO: implement
        return Pact.builder()
            .provider(new Service("a"))
            .consumer(new Service("a"))
            .interactions(null)
            .metadata(new Metadata("1.0.0"))
            .build();
    }
}
