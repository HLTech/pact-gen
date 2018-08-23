package dev.hltech;

import dev.hltech.pact.generation.FeignClientsFinder;
import dev.hltech.pact.generation.PactFactory;
import dev.hltech.pact.generation.PactJsonFactory;
import dev.hltech.pact.generation.model.Pact;

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
        final PactJsonFactory pactJsonFactory = new PactJsonFactory();

        List<Pact> pacts = finder.findFeignClients("dev.hltech.feign").stream()
            .map(clazz -> pactFactory.create(clazz, "SomeConsumer"))
            .collect(Collectors.toList());

        pactJsonFactory.generatePactFiles(pacts);
    }
}
