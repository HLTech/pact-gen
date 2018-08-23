package dev.hltech;

import dev.hltech.pact.generation.FeignClientsFinder;
import dev.hltech.pact.generation.PactFactory;
import dev.hltech.pact.generation.PactJsonGenerator;
import dev.hltech.pact.generation.model.Pact;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main executable
 */
public class Application {

    public static void main(String[] args) throws IOException {
        final FeignClientsFinder finder = new FeignClientsFinder();
        final PactFactory pactFactory = new PactFactory();
        final PactJsonGenerator pactJsonGenerator = new PactJsonGenerator();

        List<Pact> pacts = finder.findFeignClients("dev.hltech.feign").stream()
            .map(clazz -> pactFactory.create(clazz, "SomeConsumer"))
            .collect(Collectors.toList());

        pactJsonGenerator.generatePactFiles(new File("build/pacts"), pacts);
    }
}
