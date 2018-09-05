package dev.hltech.pact.generation.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.hltech.pact.generation.domain.client.feign.FeignClientsFinder;
import dev.hltech.pact.generation.domain.pact.PactFactory;
import dev.hltech.pact.generation.domain.pact.PactJsonGenerator;
import dev.hltech.pact.generation.domain.pact.model.Pact;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.util.List;
import java.util.Set;
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

    private void write(String packageRoot,
                       String consumerName,
                       ObjectMapper objectMapper,
                       File pactFilesDestinationDir) {
        final Set<Class<?>> feignClients = feignClientsFinder.findFeignClients(packageRoot);
        final List<Pact> pacts = feignClients.stream()
            .map(feignClient -> pactFactory.createFromFeignClient(feignClient, consumerName, objectMapper))
            .collect(Collectors.toList());

        pactJsonGenerator.writePactFiles(pactFilesDestinationDir, pacts);
    }
}
