package dev.hltech.pact.generation;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import dev.hltech.pact.generation.domain.client.feign.FeignClientsFinder;
import dev.hltech.pact.generation.domain.pact.PactFactory;
import dev.hltech.pact.generation.domain.pact.PactJsonGenerator;
import dev.hltech.pact.generation.domain.pact.Service;
import dev.hltech.pact.generation.domain.pact.model.Pact;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class PactGenerator {

    private final FeignClientsFinder feignClientsFinder;
    private final PactFactory pactFactory;
    private final PactJsonGenerator pactJsonGenerator;

    public PactGenerator() {
        this.feignClientsFinder = new FeignClientsFinder();
        this.pactFactory = new PactFactory();
        this.pactJsonGenerator = new PactJsonGenerator();
    }

    public PactGenerator(FeignClientsFinder feignClientsFinder,
                         PactFactory pactFactory,
                         PactJsonGenerator pactJsonGenerator) {
        this.feignClientsFinder = feignClientsFinder;
        this.pactFactory = pactFactory;
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

        feignClientsFinder.findFeignClients(packageRoot).stream()
            .map(feignClient -> pactFactory.createFromFeignClient(feignClient, consumerName, objectMapper))
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
