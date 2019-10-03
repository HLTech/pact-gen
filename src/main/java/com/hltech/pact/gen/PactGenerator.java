package com.hltech.pact.gen;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.hltech.pact.gen.domain.client.feign.FeignClientsFinder;
import com.hltech.pact.gen.domain.client.jaxrs.JaxRsClientsFinder;
import com.hltech.pact.gen.domain.pact.PactFactoryForFeign;
import com.hltech.pact.gen.domain.pact.PactFactoryForJaxRs;
import com.hltech.pact.gen.domain.pact.PactJsonGenerator;
import com.hltech.pact.gen.domain.pact.Service;
import com.hltech.pact.gen.domain.pact.model.Pact;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PactGenerator {

    private final FeignClientsFinder feignClientsFinder;
    private final JaxRsClientsFinder jaxRsClientsFinder;
    private final PactFactoryForFeign pactFactoryForFeign;
    private final PactFactoryForJaxRs pactFactoryForJaxRs;
    private final PactJsonGenerator pactJsonGenerator;

    public PactGenerator() {
        this.feignClientsFinder = new FeignClientsFinder();
        this.jaxRsClientsFinder = new JaxRsClientsFinder();
        this.pactFactoryForFeign = new PactFactoryForFeign();
        this.pactFactoryForJaxRs = new PactFactoryForJaxRs();
        this.pactJsonGenerator = new PactJsonGenerator();
    }

    public PactGenerator(FeignClientsFinder feignClientsFinder,
                         JaxRsClientsFinder jaxRsClientsFinder,
                         PactFactoryForFeign pactFactoryForFeign,
                         PactFactoryForJaxRs pactFactoryForJaxRs,
                         PactJsonGenerator pactJsonGenerator) {
        this.feignClientsFinder = feignClientsFinder;
        this.jaxRsClientsFinder = jaxRsClientsFinder;
        this.pactFactoryForFeign = pactFactoryForFeign;
        this.pactFactoryForJaxRs = pactFactoryForJaxRs;
        this.pactJsonGenerator = pactJsonGenerator;
    }

    public void writePactFiles(@NotNull String packageRoot,
                               @NotNull String consumerName,
                               @NotNull ObjectMapper objectMapper) {
        this.write(packageRoot, consumerName, objectMapper, null);
    }

    public void writePactFiles(@NotNull String packageRoot,
                               @NotNull String consumerName,
                               @NotNull ObjectMapper objectMapper,
                               @NotNull File pactFilesDestinationDir) {
        this.write(packageRoot, consumerName, objectMapper, pactFilesDestinationDir);
    }

    private void write(String packageRoot, String consumerName, ObjectMapper mapper, File pactFilesDestinationDir) {
        Multimap<Service, Pact> providerToPactMap = generatePacts(packageRoot, consumerName, mapper);

        List<Pact> pacts = providerToPactMap.keySet().stream()
            .map(providerToPactMap::get)
            .map(this::combinePactsToOne)
            .collect(Collectors.toList());

        pactJsonGenerator.writePactFiles(pactFilesDestinationDir, pacts);
    }

    private Multimap<Service, Pact> generatePacts(String packageRoot, String consumerName, ObjectMapper objectMapper) {
        Multimap<Service, Pact> providerToPactMap = HashMultimap.create();




        jaxRsClientsFinder.findJaxRsClients(packageRoot).stream()
            .map(jaxRsClient -> pactFactoryForJaxRs.createFromJaxRsClient(jaxRsClient, consumerName, objectMapper))
            .forEach(pact -> providerToPactMap.put(pact.getProvider(), pact));

        feignClientsFinder.findFeignClients(packageRoot).stream()
            .map(jaxRsClient -> pactFactoryForFeign.createFromFeignClient(jaxRsClient, consumerName, objectMapper))
            .forEach(pact -> providerToPactMap.put(pact.getProvider(), pact));

        return providerToPactMap;
    }

    private Pact combinePactsToOne(Collection<Pact> pacts) {
        if (pacts == null || pacts.isEmpty()) {
            return null;
        }

        Pact referencePact = pacts.iterator().next();

        Pact combinedPact = Pact.builder()
                                .metadata(referencePact.getMetadata())
                                .consumer(referencePact.getConsumer())
                                .provider(referencePact.getProvider())
                                .interactions(new ArrayList<>())
                                .build();

        pacts.forEach(pact -> combinedPact.getInteractions().addAll(pact.getInteractions()));

        return combinedPact;
    }

}
